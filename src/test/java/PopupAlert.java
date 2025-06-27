import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PopupAlert {
    public static void main(String[]args) throws InterruptedException {
        WebDriver driver= new ChromeDriver();
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
        driver.findElement(By.xpath("//*[@id=\"alertbtn\"]")).click();
        Alert alert = driver.switchTo().alert(); //Switch to alert
        alert.accept(); //Accept the alert (OK)
        Thread.sleep(5000);
        driver.findElement(By.xpath("//*[@id=\"confirmbtn\"]")).click();
        Alert alert1= driver.switchTo().alert();
        alert1.dismiss();// Cancel the alert (for confirmation popups)
        // OR: Enter text (for prompt popups)
        // alert.sendKeys("John");
        driver.close();
    }
}
