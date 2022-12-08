package com.kazurayam.inspectus.materialize.discovery;

import com.kazurayam.inspectus.materialize.TestHelper;
import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SiteMapLoaderTest {

    private Path fixtureDir = TestHelper.getFixturesDirectory().resolve("com/kazurayam/inspectus/materialize/discovery/SiteMapLoaderTest");

    @BeforeEach
    public void setup() {
        assert Files.exists(fixtureDir);
    }

    @Test
    public void test_parseJson_Path() throws MaterialstoreException, IOException {
        Path json = fixtureDir.resolve("sitemap.json");
        assert Files.exists(json);
        Target baseTopPage = Target.builder("http://myadmin.kazurayam.com").build();
        Target twinTopPage = Target.builder("http://devadmin.kazurayam.com").build();
        SitemapLoader loader = new SitemapLoader(baseTopPage, twinTopPage);
        Sitemap sitemap = loader.parseJson(json);
        System.out.println(sitemap.toJson(true));
    }

    @Test
    public void test_parseCSV_File() {
        throw new RuntimeException("TODO");
    }
}
