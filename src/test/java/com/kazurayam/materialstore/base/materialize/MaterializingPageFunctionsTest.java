package com.kazurayam.materialstore.base.materialize;

import com.kazurayam.materialstore.core.filesystem.FileType;
import com.kazurayam.materialstore.core.filesystem.JobName;
import com.kazurayam.materialstore.core.filesystem.JobTimestamp;
import com.kazurayam.materialstore.core.filesystem.Material;
import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata;
import com.kazurayam.materialstore.core.filesystem.Store;
import com.kazurayam.materialstore.core.filesystem.Stores;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaterializingPageFunctionsTest {

    private static Path outputDir =
            Paths.get(System.getProperty("user.dir"))
                    .resolve("build/tmp/testOutput")
                    .resolve(MaterializingPageFunctionsTest.class.getName());

    private static Store store;
    private WebDriver driver;

    @BeforeAll
    public static void beforeAll() throws IOException {
        if (Files.exists(outputDir)) {
            FileUtils.deleteDirectory(outputDir.toFile());
        }
        Files.createDirectories(outputDir);
        Path root = outputDir.resolve("store");
        store = Stores.newInstance(root);
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("headless");
        driver = new ChromeDriver(opt);
        driver.manage().window().setSize(new Dimension(1024, 768));
    }

    @Test
    void test_storeHTMLSource() throws MaterialstoreException {
        Target target = new Target.Builder("https://www.google.com")
                .handle(By.cssSelector("input[name=\"q\"]"))
                .build();
        JobName jobName = new JobName("test_storeHTMLSource");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        StorageDirectory storageDirectory = new StorageDirectory(store, jobName, jobTimestamp);
        // open the page in browser
        driver.navigate().to(target.getUrl());
        // get HTML source of the page, save it into the store
        Map<String, String> attribute = Collections.singletonMap("step", "01");
        Material createdMaterial  = MaterializingPageFunctions.storeHTMLSource.accept(target, driver, storageDirectory, attribute);
        assertNotNull(createdMaterial);
        // assert that a material has been created
        Material selectedMaterial = store.selectSingle(jobName, jobTimestamp, FileType.HTML, QueryOnMetadata.ANY);
        assertTrue(Files.exists(selectedMaterial.toPath(store.getRoot())));
        assertEquals(createdMaterial, selectedMaterial);
    }

    @Test
    void test_storeEntirePageScreenshot() throws MaterialstoreException {
        Target target = new Target.Builder("https://github.com/kazurayam")
                .handle(By.cssSelector("div.application-main main"))
                .build();
        JobName jobName = new JobName("test_storeEntirePageScreenshot");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        StorageDirectory storageDirectory = new StorageDirectory(store, jobName, jobTimestamp);
        // open the page in browser
        driver.navigate().to(target.getUrl());
        // take an entire page screenshot, write the image into the store
        Map<String, String> attribute = Collections.singletonMap("step", "01");
        Material createdMaterial = MaterializingPageFunctions.storeEntirePageScreenshot.accept(target, driver, storageDirectory, attribute);
        assertNotNull(createdMaterial);
        // assert that a material has been created
        Material selectedMaterial = store.selectSingle(jobName, jobTimestamp, FileType.PNG, QueryOnMetadata.ANY);
        assertTrue(Files.exists(selectedMaterial.toPath(store.getRoot())));
        assertEquals(createdMaterial, selectedMaterial);
    }


    @AfterEach
    public void afterEach() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
