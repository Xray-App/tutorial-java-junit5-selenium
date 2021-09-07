package com.xpandit.xray.tutorials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xpandit.xray.junit.customjunitxml.XrayTestReporterParameterResolver;
import com.xpandit.xray.junit.customjunitxml.annotations.Requirement;
import com.xpandit.xray.junit.customjunitxml.annotations.XrayTest;

@ExtendWith(XrayTestReporterParameterResolver.class)
public class LoginTests {
    WebDriver driver;

    @BeforeEach
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model, to run in Docker
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
    }
    
    @Test
    @XrayTest(key = "XT-12")
    @Requirement("XT-10")
    public void validLogin()
    {
        LoginPage loginPage = new LoginPage(driver).open();
        LoginResultsPage loginResultsPage = loginPage.login("demo", "mode");
        assertEquals(loginResultsPage.getTitle(), "Welcome Page");
        assertTrue(loginResultsPage.contains("Login succeeded"));
    }

    @Test
    @XrayTest(summary = "invalid login test", description = "login attempt with invalid credentials")
    public void invalidLogin()
    {
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isVisible());
        LoginResultsPage loginResultsPage = loginPage.login("demo", "invalid");
        assertEquals(loginResultsPage.getTitle(), "Error Page");
        assertTrue(loginResultsPage.contains("Login failed"));
    }

}
