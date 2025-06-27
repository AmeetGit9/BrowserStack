package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//span[normalize-space()='My Account']")
    WebElement inkMyaccount;

    @FindBy(xpath = "//a[normalize-space()='Register']")
    WebElement inkMyRegister;

    public void clickMyAccount()
    {
        inkMyaccount.click();
    }
    public void clickRegister()
    {
        inkMyRegister.click();
    }

}
