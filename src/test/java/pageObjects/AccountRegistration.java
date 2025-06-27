package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountRegistration extends BasePage{


    public AccountRegistration(WebDriver driver) {
        super(driver);
    }
    @FindBy(xpath = "//input[@id='input-firstname']")
    WebElement txtfirstname;
    @FindBy(xpath = "//input[@id='input-lastname']")
    WebElement txtlastname;
    @FindBy(xpath = "//input[@id='input-email']")
    WebElement txtemail;
    @FindBy(xpath = "//input[@id='input-telephone']")
    WebElement txttelphone;
    @FindBy(xpath = "//input[@id='input-password']")
    WebElement txtpwd;
    @FindBy(xpath = "//input[@id='input-confirm']")
    WebElement txtconfirmpwd;
    @FindBy(xpath = "//input[@name='agree']")
    WebElement checkpolicy;
    @FindBy(xpath = "//input[@value='Continue']")
    WebElement ContinueButton;
    @FindBy(xpath = "//h1[normalize-space()='Your Account Has Been Created!']")
    WebElement msgconfirmation;

    public void setFirstName(String fname){
        txtfirstname.sendKeys(fname);
    }
    public void setLastname(String lname){
        txtlastname.sendKeys(lname);
    }
    public void setEmail(String Email){
        txtemail.sendKeys(Email);
    }
    public void setTelephone(String TNumber){
        txttelphone.sendKeys(TNumber);
    }
    public void setPwd(String Pwd){
        txtpwd.sendKeys(Pwd);
    }
    public void setCpwd(String Cpwd){
        txtconfirmpwd.sendKeys(Cpwd);
    }
    public void setPrivacyPolicy(){
        checkpolicy.click();
    }
    public void setcontButton(){
        ContinueButton.click();
    }
    public String getconfirmationmsg(){
        try{
            return (msgconfirmation.getText());
        }catch (Exception e){
            return (e.getMessage());
        }

    }


}
