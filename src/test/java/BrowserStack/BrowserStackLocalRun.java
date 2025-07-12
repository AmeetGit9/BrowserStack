package BrowserStack;

import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

public class BrowserStackLocalRun {

    public static void main(String[] args) {
        WebDriver driver = null;
        Map<String, String> translatedToSpanishTitle = new LinkedHashMap<>();

        try {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            driver.get("https://elpais.com");

            // Accept cookies popup
            try {
                WebElement popup = driver.findElement(By.id("didomi-notice-agree-button"));
                popup.click();
            } catch (NoSuchElementException ignored) {}

            // Check language
            String lang = driver.findElement(By.tagName("html")).getAttribute("lang");
            if (lang == null || !lang.startsWith("es")) {
                System.out.println("Website is not in Spanish");
                return;
            }
            System.out.println("Website is in Spanish");

            // Click on Opinión link
            driver.findElement(By.linkText("Opinión")).click();

            // Get articles and filter valid ones
            List<WebElement> articles = driver.findElements(By.cssSelector("article"));
            int count = 0;
            Set<String> seenUrls = new HashSet<>();

            for (WebElement article : articles) {
                if (count >= 5) break;

                try {
                    WebElement anchor = article.findElement(By.cssSelector("a"));
                    String url = anchor.getAttribute("href");

                    // Only process valid opinion articles
                    if (url == null || !url.matches(".*/opinion/\\d{4}-\\d{2}-\\d{2}/.*")) {
                        continue;
                    }

                    if (!seenUrls.add(url)) continue;

                    WebElement header = article.findElement(By.cssSelector("h2, h3"));
                    String title = header.getText().trim();
                    if (title.isEmpty()) continue;

                    String content = "";
                    List<WebElement> paragraphs = article.findElements(By.cssSelector("p"));
                    for (WebElement p : paragraphs) content += p.getText() + "\n";

                    System.out.println("\nTitle (Spanish): " + title);
                    System.out.println("URL: " + url);
                    System.out.println("Content: " + (content.isBlank() ? "N/A" : content.trim()));

                    try {
                        WebElement img = article.findElement(By.cssSelector("img"));
                        String imgUrl = img.getAttribute("src");
                        if (imgUrl != null && !imgUrl.isEmpty()) {
                            saveImage(imgUrl, "article_" + (count + 1) + ".jpg");
                            System.out.println("Image saved: article_" + (count + 1) + ".jpg");
                        }
                    } catch (Exception e) {
                        System.out.println("No image found.");
                    }

                    try {
                        Thread.sleep(1200); // To avoid 429 rate limit
                        String translated = translateText(title);
                        System.out.println("Translated Title: " + translated);
                        translatedToSpanishTitle.put(translated, title);
                    } catch (Exception e) {
                        System.out.println("Skipped translation: " + e.getMessage());
                    }

                    count++;

                } catch (Exception e) {
                    System.out.println(" Skipping due to error: " + e.getMessage());
                }
            }

            // Word frequency analysis
            System.out.println("\n Repeated Words in Translated Titles:");
            Map<String, Integer> freq = new HashMap<>();
            for (String engTitle : translatedToSpanishTitle.keySet()) {
                for (String word : engTitle.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+")) {
                    if (word.length() >= 3) {
                        freq.put(word, freq.getOrDefault(word, 0) + 1);
                    }
                }
            }

            Map<String, Integer> repeated = new LinkedHashMap<>();
            freq.entrySet().stream()
                    .filter(e -> e.getValue() > 2)
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(e -> repeated.put(e.getKey(), e.getValue()));

            if (repeated.isEmpty()) {
                System.out.println("No words are repeated.");
            } else {
                repeated.forEach((word, c) -> System.out.println(word + ": " + c));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    public static String translateText(String text) throws IOException {
        text = text.replace("\"", "\\\"").replace("\n", " ");
        String encodedText = URLEncoder.encode(text, "UTF-8");

        String apiUrl = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=es|en";

        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Translation API failed with code: " + responseCode);
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONObject("responseData").getString("translatedText");
    }

    public static void saveImage(String url, String filename) {
        try (InputStream in = new URL(url).openStream()) {
            File folder = new File("./images");
            if (!folder.exists()) folder.mkdir();

            try (FileOutputStream out = new FileOutputStream(new File(folder, filename))) {
                out.write(in.readAllBytes());
            }
        } catch (IOException e) {
            System.out.println("Image download failed: " + e.getMessage());
        }
    }
}