package com.idera.xray.tutorials;

import org.openqa.selenium.WebDriver;

public class LoginResultsPage {
    private WebDriver driver;

    public LoginResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public Boolean contains(String text) {
        return driver.getPageSource().contains(text);
    }

    public String getTitle()
    {
        return driver.getTitle();
    }
    
}
