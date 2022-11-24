package com.kazurayam.inspectus.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebDriverFormulasTest {

    WebDriver driver;
    WebDriverManager manager;
    WebDriverFormulas formulas;

    @BeforeAll
    public static void beforeClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("headless");
        driver = new ChromeDriver(opt);
        formulas = new WebDriverFormulas();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test_createWebDriverWait() {
        WebDriverWait wait = formulas.createWebDriverWait(driver, 3);
        assertNotNull(wait);
        assertTrue(wait instanceof WebDriverWait);
    }
}
