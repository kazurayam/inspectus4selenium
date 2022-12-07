package com.kazurayam.inspectus.net;

import com.kazurayam.inspectus.TestHelper;
import com.kazurayam.inspectus.discovery.Target;
import com.kazurayam.materialstore.core.filesystem.FileType;
import com.kazurayam.materialstore.core.filesystem.JobName;
import com.kazurayam.materialstore.core.filesystem.JobTimestamp;
import com.kazurayam.materialstore.core.filesystem.Material;
import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata;
import com.kazurayam.materialstore.core.filesystem.Store;
import com.kazurayam.materialstore.core.filesystem.Stores;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class URLMaterializingFunctionsTest {

    private static Store store;

    @BeforeAll
    public static void beforeAll() {
        Path testCaseOutputDir =
                TestHelper.createTestClassOutputDir(URLMaterializingFunctionsTest.class);
        Path root = testCaseOutputDir.resolve("store");
        store = Stores.newInstance(root);
    }

    @Test
    public void test_storeWebResource_jpg() throws MaterialstoreException {
        Target target =
                new Target.Builder("http://myadmin.kazurayam.com/umineko-1960x1960.jpg")
                        .put("step", "1")
                        .build();
        JobName jobName = new JobName("test_storeWebResource_jpg");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URLMaterializingFunctions umf = new URLMaterializingFunctions(store, jobName, jobTimestamp);
        umf.storeURL.accept(target);
        //
        Material material = store.selectSingle(jobName, jobTimestamp, QueryOnMetadata.ANY);
        assertEquals(FileType.JPG, material.getFileType());
    }

    @Test
    public void test_storeWebResource_js() throws MaterialstoreException {
        Target target = new Target.Builder("https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.js")
                .put("step", "2")
                .build();
        JobName jobName = new JobName("test_storeWebResource_js");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URLMaterializingFunctions umf = new URLMaterializingFunctions(store, jobName, jobTimestamp);
        umf.storeURL.accept(target);
        //
        Material material = store.selectSingle(jobName, jobTimestamp);
        assertEquals(FileType.JS, material.getFileType());
    }

    @Test
    public void test_storeWebResource_xls() throws MaterialstoreException {
        Target target = new Target.Builder("https://filesamples.com/samples/document/xls/sample1.xls")
                .put("step", "3")
                .build();
        JobName jobName = new JobName("test_storeWebResource_xls");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URLMaterializingFunctions umf = new URLMaterializingFunctions(store, jobName, jobTimestamp);
        umf.storeURL.accept(target);
        //
        Material material = store.selectSingle(jobName, jobTimestamp);
        assertEquals(FileType.XLS, material.getFileType());
    }

    @Test
    public void test_storeWebResource_pdf() throws MaterialstoreException {
        Target target = new Target.Builder("https://unric.org/en/wp-content/uploads/sites/15/2020/01/sdgs-eng.pdf")
                .put("step", "4")
                .build();
        JobName jobName = new JobName("test_storeWebResource_pdf");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URLMaterializingFunctions umf = new URLMaterializingFunctions(store, jobName, jobTimestamp);
        umf.storeURL.accept(target);
        //
        Material material = store.selectSingle(jobName, jobTimestamp);
        assertEquals(FileType.PDF, material.getFileType());
    }



    @Test
    public void test_storeWebResource_css() throws MaterialstoreException {
        Target target = new Target.Builder("https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css")
                .put("step", "5")
                .build();
        JobName jobName = new JobName("test_storeWebResource_css");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        URLMaterializingFunctions umf = new URLMaterializingFunctions(store, jobName, jobTimestamp);
        umf.storeURL.accept(target);
        //
        Material material = store.selectSingle(jobName, jobTimestamp);
        assertEquals(FileType.CSS, material.getFileType());
    }

}
