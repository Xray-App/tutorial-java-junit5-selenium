package com.idera.xray.tutorials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import app.getxray.xray.junit.customjunitxml.XrayTestReporter;
import app.getxray.xray.junit.customjunitxml.XrayTestReporterParameterResolver;
import app.getxray.xray.junit.customjunitxml.annotations.Requirement;
import app.getxray.xray.junit.customjunitxml.annotations.XrayTest;

@ExtendWith(XrayTestReporterParameterResolver.class)
public class LoginTests {
    WebDriver driver;
    RepositoryParser repo;

    @BeforeEach
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model, to run in Docker
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        repo = new RepositoryParser("./src/configs/object.properties");
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        driver = null;
        repo = null;
    }
    
    @Test
    @XrayTest(key = "XT-307")
    @Requirement("XT-10")
    public void successLogin()
    {
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isVisible());
        LoginResultsPage loginResultsPage = loginPage.login("demo", "mode");
        assertEquals(loginResultsPage.getTitle(), repo.getBy("expected.login.title"));
        assertTrue(loginResultsPage.contains(repo.getBy("expected.login.success")));
    }

    @Test
    @XrayTest(summary = "invalid login test", description = "login attempt with invalid credentials")
    public void nosuccessLogin(XrayTestReporter xrayReporter)
    {
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isVisible());
        LoginResultsPage loginResultsPage = loginPage.login("demo", "invalid");
        TakesScreenshot screenshotTaker =((TakesScreenshot)driver);
        File screenshot = screenshotTaker.getScreenshotAs(OutputType.FILE);
        xrayReporter.addTestRunEvidence(screenshot.getAbsolutePath());
        xrayReporter.addComment("auth should have failed");
        assertEquals(loginResultsPage.getTitle(), repo.getBy("expected.error.title"));
        assertTrue(loginResultsPage.contains(repo.getBy("expected.login.failed")));
    }

}
