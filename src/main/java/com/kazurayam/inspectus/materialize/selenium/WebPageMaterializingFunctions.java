package com.kazurayam.inspectus.materialize.selenium;

import com.kazurayam.inspectus.materialize.discovery.Target;
import com.kazurayam.materialstore.core.FileType;
import com.kazurayam.materialstore.core.JobName;
import com.kazurayam.materialstore.core.JobTimestamp;
import com.kazurayam.materialstore.core.Material;
import com.kazurayam.materialstore.core.Metadata;
import com.kazurayam.materialstore.core.Store;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

public class WebPageMaterializingFunctions {

    private Store store;
    private JobName jobName;
    private JobTimestamp jobTimestamp;

    public WebPageMaterializingFunctions(Store store, JobName jobName, JobTimestamp jobTimestamp) {
        Objects.requireNonNull(store);
        Objects.requireNonNull(jobName);
        Objects.requireNonNull(jobTimestamp);
        this.store = store;
        this.jobName = jobName;
        this.jobTimestamp = jobTimestamp;
    }

    /**
     * get HTML source of the target web page, pretty-print it, save it into
     * the store
     */
    public WebPageMaterializingFunction<WebDriver, Target, Map<String,String>, Material>
            storeHTMLSource = (driver, target, attributes) -> {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(target);
        Objects.requireNonNull(attributes);
        //-------------------------------------------------------------
        // get the HTML source from browser
        String rawHtmlSource = driver.getPageSource();
        // pretty print HTML source
        Document doc = Jsoup.parse(rawHtmlSource, "", Parser.htmlParser());
        doc.outputSettings().indentAmount(2);
        String ppHtml = doc.toString();
        //-------------------------------------------------------------
        // write the HTML source into the store
        Metadata metadata = Metadata.builder(target.getUrl())
                .putAll(target.getAttributes())
                .putAll(attributes)
                .build();
        return this.store.write(this.jobName, this.jobTimestamp,
                FileType.HTML, metadata, ppHtml);
    };

    /**
     *
     */
    public WebPageMaterializingFunction<WebDriver, Target, Map<String,String>, Material>
            storeEntirePageScreenshot = (driver, target, attributes) -> {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(target);
        Objects.requireNonNull(attributes);
        //-------------------------------------------------------------
        int timeout = 500;  // milli-seconds
        // look up the device-pixel-ratio of the current machine
        JavascriptExecutor js = (JavascriptExecutor)driver;
        float dpr = (Long)js.executeScript("return window.devicePixelRatio;") * 1.0f;
        AShot aShot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .shootingStrategy(ShootingStrategies.viewportPasting(
                                ShootingStrategies.scaling(dpr),
                                timeout));
        // take a screenshot of entire view of the page
        Screenshot screenshot = aShot.takeScreenshot(driver);
        BufferedImage bufferedImage = screenshot.getImage();
        // scroll the view to the top of the page
        js.executeScript("window.scrollTo(0, 0);");
        //-------------------------------------------------------------
        // write the PNG image into the store
        Metadata metadata = Metadata.builder(target.getUrl())
                .putAll(target.getAttributes())
                .putAll(attributes)
                .build();
        return this.store.write(this.jobName, this.jobTimestamp,
                FileType.PNG, metadata, bufferedImage);
    };

}
