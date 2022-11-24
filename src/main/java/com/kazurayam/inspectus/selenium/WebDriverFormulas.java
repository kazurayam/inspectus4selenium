package com.kazurayam.inspectus.selenium;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WebDriverFormulas {

    public WebDriverFormulas() {}

    public final void navigateByClick(WebDriver driver,
                                      By clickThis,
                                      By handleInTheNext,
                                      int timeout) {
        this.waitForElementPresent(driver, clickThis, timeout);
        driver.findElement(clickThis).click();
        this.waitForElementPresent(driver, handleInTheNext, timeout);
    }

    public final void navigateTo(WebDriver driver,
                                 URL url,
                                 By handle,
                                 int timeout) {
        driver.get(url.toExternalForm());
        this.waitForElementPresent(driver, handle, timeout);
    }

    public final void waitForElementPresent(WebDriver driver,
                                            By handle,
                                            long timeout) {
        WebDriverWait wait = createWebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(handle));
    }

    /**
     * Create an instance of org.openqa.selenium.WebDriverWait.
     * Please note that Selenium3 and Selenium4 has different signature of
     * the constructor of WebDriverWait. They are incompatible.
     * This method absorbs this incompatibility and silently returns
     * the result both given with either of Selenium3 or Selenium4
     * in the CLASSPATH.
     *
     * @param driver WebDriver
     * @param timeout seconds
     * @return an instance of WebDriverWait
     */
    final WebDriverWait createWebDriverWait(WebDriver driver, long timeout) {
        //return new WebDriverWait(driver, timeout);
        Class<?> clazz;
        String fullyQualifiedClassName = "org.openqa.selenium.support.ui.WebDriverWait";
        try {
            clazz = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        Constructor<?> selenium4constructor;
        try {
            selenium4constructor = clazz.getConstructor(WebDriver.class, Duration.class);
            return (WebDriverWait)selenium4constructor.newInstance(driver, Duration.ofSeconds(timeout));
        } catch (NoSuchMethodException e) {
            // no Selenium4 jar is found in the CLASSPATH, so let me try to find Selenium3
            Constructor<?> selenium3constructor;
            try {
                selenium3constructor = clazz.getConstructor(WebDriver.class, Long.class);
                return (WebDriverWait)selenium3constructor.newInstance(driver, timeout);
            } catch (NoSuchMethodException x) {
                throw new IllegalStateException(
                        String.format("%s is not found in the CLASSPATH", fullyQualifiedClassName),
                        x);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
