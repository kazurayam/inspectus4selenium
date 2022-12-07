package com.kazurayam.inspectus.discovery;

import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SitemapTest {

    private static final Logger logger =
            LoggerFactory.getLogger(SitemapTest.class);

    private static Target baseTopPage;
    private static Target twinTopPage;
    private static Target baseIndex;
    private static Target baseRepositories;
    private static Target baseProverbs;

    @BeforeAll
    public static void beforeAll() throws MaterialstoreException {
        baseTopPage = Target.builder("http://myadmin.kazurayam.com").build();
        twinTopPage = Target.builder("http://devadmin.kazurayam.com").build();
        baseIndex = Target.builder("http://myadmin.kazurayam.com/index.html").build();
        baseRepositories = Target.builder("http://myadmin.kazurayam.com/repositories.html").build();
        baseProverbs = Target.builder("http://myadmin.kazurayam.com/proverbs.html").build();
    }

    @Test
    public void test_constructorSingle(){
        Sitemap sm = new Sitemap(baseTopPage);
        assertEquals(1, sm.size());
        assertEquals(baseTopPage, sm.getBaseTopPage());
        assertEquals(Target.NULL_OBJECT, sm.getTwinTopPage());
    }

    @Test
    public void test_constructorTwin() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        assertEquals(1, sm.size());
        assertEquals(baseTopPage, sm.getBaseTopPage());
        assertEquals(twinTopPage, sm.getTwinTopPage());
    }

    @Test
    public void test_add_target() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        sm.add(baseIndex);
        sm.add(baseRepositories);
        sm.add(baseProverbs);
        assertEquals(4, sm.size());
    }

    @Test
    public void test_deriveTwinTarget() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        sm.add(baseIndex);
        assertEquals("devadmin.kazurayam.com",
                sm.deriveTwinTarget(baseIndex).getUrl().getHost());
    }

    @Test
    public void test_getTwinTarget() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        sm.add(baseIndex);
        assertEquals("devadmin.kazurayam.com",
                sm.getTwinTarget(1).getUrl().getHost());
    }

    @Test
    public void test_getBaseTargetList() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        sm.add(baseIndex);
        sm.add(baseRepositories);
        sm.add(baseProverbs);
        List<Target> list = sm.getBaseTargetList();
        assertEquals(4, list.size());
        for (Target t : list) {
            logger.info("[test_getBaseTargetList] " + t.toJson());
        }
    }

    @Test
    public void test_getTwinTargetList() {
        Sitemap sm = new Sitemap(baseTopPage, twinTopPage);
        sm.add(baseIndex);
        sm.add(baseRepositories);
        sm.add(baseProverbs);
        List<Target> list = sm.getTwinTargetList();
        assertEquals(4, list.size());
        for (Target t : list) {
            logger.info("[test_getTwinTargetList] " + t.toJson());
        }
    }
}
