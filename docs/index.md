# inspectus4selenium

Back to the [project’s GitHub repository](https://github.com/kazurayam/inspectus4selenium)

## Overview

The inspectus4selenium provides a small Java class

-   [selenium.com.kazurayam.inspectus.materialize.WebDriverFormulas](https://github.com/kazurayam/inspectus4selenium/blob/master/src/main/java/com/kazurayam/inspectus/selenium/WebDriverFormulas.java)

When you write a web UI test on top of [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/), you need to be aware of timing issues: you have to repeat writing codes so that your tests wait for pages to be loaded into the browser completely; you have to wait for web element to be present in the DOM before doing any action on it. You will write codes to wait for conditions to meet repeatedly. Your UI tests tend to be verbose and difficult to maintain.

This `WebDriverFormulas` class supports automated web UI tests on top of Selenium WebDriver. This class implements several helper methods:

-   `createWebDriverWait(WebDriver driver, long timemout)`

-   `waitForElementPresent(WebDriver driver, By handle, long timeout)`

-   `navigateTo(WebDriver driver, URL url, By handle, long timeout)`

-   `navigateByClick(WebDriver, By clickThis, By handle, long timeout)`

You can use these method in your UI tests. Your code will be much more concise.

## How to use `inspectus4selenium`

The jar is distributed at the Maven Central repository.

-   <https://mvnrepository.com/artifact/com.kazurayam/inspectus4selenium>

Gradle build.xml example:

    dependencies {
        testImplementation group: "com.kazurayam" name: "inspectus4selenium" version: "0.1.1"
    }

Please find more info at the following resources:

-   [a sample JUnit5 Testcase](https://github.com/kazurayam/inspectus4selenium/blob/master/src/test/java/com/kazurayam/inspectus/selenium/WebDriverFormulasTest.java)

-   [API Javadoc](https://kazurayam.github.io/inspectus4selenium/api/index.html)

## Note

### Backward incompatibility of Selenium4 to Selenium3 is managed

The Selenium4 introduced several changes from Selenium3 which are backward-incompatible. One the significant changes was that the constructor of `org.openqa.selenium.WebDriverWait` class used `java.time.Duration` instead of `long` time for `timeout` parameter. See the following sample snippets:

    // Selenium3 signature
    new WebDriverWait(driver, 3)
                           // ^
        .until(ExpectedConditions.elementToBeClickable(By.cssSelector("#id")));

and

    // Selenium4 signature
    new WebDriverWait(driver, Duration.ofSeconds(3))
                           // ^^^^^^^^^^^^^^^^^^^^^
        .until(ExpectedConditions.elementToBeClickable(By.cssSelector("#id")));

The Selenium4 does NOT have the signature with `long timeout` argument. The test scripts that assume the Selenium3 signature won’t compile with Selenium4 any longer. This incompatibility annoys us, Selenium users.

The `createWebDriverWait(WebDriver, long)` method would help. This method works in both cases where you have the jar of Selenium3 in the classpath, or the jar of Selenium4. In either case, the method returns an instance of `WebDriverWait` class silently. You can read the [implementation of this method](https://github.com/kazurayam/inspectus4selenium/blob/master/src/main/java/com/kazurayam/inspectus/selenium/WebDriverFormulas.java). It employs Java Reflection API. It checks which of Selenium3 or Selenium4 is given in the runtime classpath, and instantiate the `WebDriverWiat` object as appropriate.
