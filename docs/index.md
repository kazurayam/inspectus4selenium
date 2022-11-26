# inspectus4selenium

The inspectus4selenium provides a small Java class

-   [com.kazurayam.inspectus.selenium.WebDriverFormulas](https://github.com/kazurayam/inspectus4selenium/blob/master/src/main/java/com/kazurayam/inspectus/selenium/WebDriverFormulas.java)

When you write a web UI test on top of Selenium, you need to be aware of timing issues: you have to repeat writing codes so that your tests wait for pages to be loaded into the browser completely; you have to wait for web element to be present in the DOM before doing any action on ti. You will write "code to wait xxxx" repeately. Consequently your UI tests tend to be verbose and difficult to maintain.

This class supports automated web UI tests on top of [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/). The `WebDriverFormulas` class implements the following helper methods:

-   `createWebDriverWait(WebDriver driver, long timemout)`

-   `waitForElementPresent(WebDriver driver, By handle, long timeout)`

-   `navigateTo(WebDriver driver, URL url, By handl, long timeout)`

-   `navigateByClick(WebDriver, By clickThis, By handle, long timeout)`

You can use these method in your UI tests. Your code will be much more concise.

How to use them? Please find more info at the following resources:

-   [a sample JUnit5 Testcase](https://github.com/kazurayam/inspectus4selenium/blob/master/src/test/java/com/kazurayam/inspectus/selenium/WebDriverFormulasTest.java)

-   [API Javadoc](https://inspectus4selenium.kazurayam.github.com/api/index.html)