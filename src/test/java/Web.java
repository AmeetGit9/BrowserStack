import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class Web {
    public static void main(String[] args) throws InterruptedException {


        WebDriver driver = new ChromeDriver();
        driver.get("https://testautomationpractice.blogspot.com/"); //lounh website
        String URL=driver.getCurrentUrl(); //print URL
        System.out.println(URL);
        String Headline = driver.getTitle();
        System.out.println(Headline);      //print website headline
        WebElement Ele =driver.findElement(By.xpath("//*[@id=\"name\"]"));//sendKeys("Test");
        if (Ele.isDisplayed()){              //check button selected
            Ele.sendKeys("Hello");
        }
        else {
            System.out.println("element not present");
        }
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("digitizecart@gmail.com");
        WebElement Ele2=  driver.findElement(By.xpath("//*[@id=\"male\"]"));
        if (Ele2.isSelected()){
            System.out.println("Radio button selected");
        }
        else {
            Ele2.click();
        }
        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"country\"]"));
        Select select = new Select(dropdown); //select dropdown
        select.selectByVisibleText("India");
        driver.manage().window().maximize(); //maximise window

        driver.navigate().to("https://rahulshettyacademy.com/AutomationPractice/"); //navigatinng from one web to another web
        WebElement TragetElement=driver.findElement(By.xpath("/html/body/header/div/button[3]"));//double click
        Actions actions = new Actions(driver);//double click
        actions.doubleClick(TragetElement).perform();//double click

        //Right click
        WebElement rightClickButton = driver.findElement(By.xpath("//*[@id=\"opentab\"]"));
        Actions actions2 = new Actions(driver);
        actions2.contextClick(rightClickButton).perform();

        Thread.sleep(5000); //wait
        List<WebElement> Alllinks = driver.findElements(By.xpath("a")); //To print all links from web
        System.out.println("Total number of links on the page: " + Alllinks.size());
        driver.navigate().back(); //Navigate back to previous web
        Thread.sleep(5000);
        driver.navigate().refresh(); //Refresh the webpage
        driver.close();//close website
        driver.manage().deleteAllCookies(); // clear cookies



    }
}
