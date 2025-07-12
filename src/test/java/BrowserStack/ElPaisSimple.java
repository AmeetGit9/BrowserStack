package BrowserStack;

import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.*;

public class ElPaisSimple {

    public static void runTest(WebDriver driver) throws Exception {
        Map<String, String> translatedToSpanishTitle = new LinkedHashMap<>();

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

        // Click on Opinion link
        driver.findElement(By.linkText("Opinión")).click();

        List<WebElement> articles = driver.findElements(By.cssSelector("article"));
        int count = 0;
        Set<String> seenUrls = new HashSet<>();

        for (WebElement article : articles) {
            if (count >= 5) break;

            try {
                WebElement anchor = article.findElement(By.cssSelector("a"));
                String url = anchor.getAttribute("href");
                if (url == null || !url.matches(".*/opinion/\\d{4}-\\d{2}-\\d{2}/.*")) continue;
                if (!seenUrls.add(url)) continue;

                WebElement header = article.findElement(By.cssSelector("h2, h3"));
                String title = header.getText().trim();
                if (title.isEmpty()) continue;

                StringBuilder content = new StringBuilder();
                for (WebElement p : article.findElements(By.cssSelector("p"))) {
                    content.append(p.getText()).append("\n");
                }

                System.out.println("\nTitle (Spanish): " + title);
                System.out.println("URL: " + url);
                System.out.println("ontent: " + (content.isEmpty() ? "N/A" : content.toString().trim()));

                try {
                    WebElement img = article.findElement(By.cssSelector("img"));
                    String imgUrl = img.getAttribute("src");
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        saveImage(imgUrl, "article_" + (count + 1) + ".jpg");
                        System.out.println("Image saved: article_" + (count + 1) + ".jpg");
                    }
                } catch (Exception ignored) {}

                try {
                    Thread.sleep(1000);
                    String translated = translateText(title);
                    System.out.println("Translated Title: " + translated);
                    translatedToSpanishTitle.put(translated, title);
                } catch (Exception e) {
                    System.out.println("Translation skipped: " + e.getMessage());
                }

                count++;
            } catch (Exception e) {
                System.out.println("Article skipped: " + e.getMessage());
            }
        }

        // Word frequency analysis
        System.out.println("\nRepeated Words in Translated Titles:");
        Map<String, Integer> freq = new HashMap<>();
        for (String engTitle : translatedToSpanishTitle.keySet()) {
            for (String word : engTitle.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+")) {
                if (word.length() >= 3) {
                    freq.put(word, freq.getOrDefault(word, 0) + 1);
                }
            }
        }

        freq.entrySet().stream()
                .filter(e -> e.getValue() > 2)
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    public static String translateText(String text) throws IOException {
        String encodedText = URLEncoder.encode(text.replace("\"", "\\\"").replace("\n", " "), "UTF-8");
        String apiUrl = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=es|en";
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("Translation failed with code " + conn.getResponseCode());
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            while (scanner.hasNext()) response.append(scanner.nextLine());
        }

        return new JSONObject(response.toString())
                .getJSONObject("responseData")
                .getString("translatedText");
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
