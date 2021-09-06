package com.xpandit.xray.tutorials;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private String loginPageUrl = "http://robotwebdemo.herokuapp.com/";

    @FindBy(id = "username_field")
    private WebElement usernameElement;

    @FindBy(id = "password_field")
    private WebElement passwordElement;

    @FindBy(id = "login_button")
    private WebElement submitButtonElement;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public LoginPage open()
    {
        driver.navigate().to(loginPageUrl);
        return this;
    }

    public void setUsername(String username) {
        usernameElement.sendKeys(username);
    }

    public void setPassword(String password) {
        passwordElement.sendKeys(password);
    }
    public LoginResultsPage submit()
    {
       submitButtonElement.submit();
       return new LoginResultsPage(driver);
    }

    public LoginResultsPage login(String username, String password)
    {
        setUsername(username);
        setPassword(password);
        return submit();
        //return new LoginResultsPage(driver);
    }

    public Boolean contains(String text) {
        return driver.getPageSource().contains(text);
    }

    public String getTitle()
    {

        return driver.getTitle();
    }

    public Boolean isVisible()
    {
        WebDriverWait wait = new WebDriverWait(driver, 30000);
        return wait.until(ExpectedConditions.elementToBeClickable(submitButtonElement)).isDisplayed();
    }

}
