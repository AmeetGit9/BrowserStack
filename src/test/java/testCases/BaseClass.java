package testCases;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.time.Duration;
import java.util.Locale;

public class BaseClass {
    public WebDriver driver;

    @BeforeClass(groups = "sanity")
    @Parameters({"os","browser"})
    public void setup(String os, String br)//we are passing parameter to launch website
    {
        switch (br.toLowerCase())
        {
            case "chrome" : driver= new ChromeDriver(); break; // based on condition respective browser will launch
            case "edge": driver= new EdgeDriver(); break;
            default: System.out.println("Invald browser name"); return; //if no browser is available it will stop the execution here and returs msg as invalid broser name
        }



        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://tutorialsninja.com/demo/");
        driver.manage().window().maximize();
    }
    @AfterClass(groups = "sanity")
    public void tearDown()
    {
        driver.quit();
    }

}
