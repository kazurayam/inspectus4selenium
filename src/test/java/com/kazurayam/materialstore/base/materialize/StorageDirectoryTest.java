package com.kazurayam.materialstore.base.materialize;

import com.kazurayam.materialstore.core.filesystem.FileType;
import com.kazurayam.materialstore.core.filesystem.JobName;
import com.kazurayam.materialstore.core.filesystem.JobTimestamp;
import com.kazurayam.materialstore.core.filesystem.Material;
import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;
import com.kazurayam.materialstore.core.filesystem.Metadata;
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata;
import com.kazurayam.materialstore.core.filesystem.Store;
import com.kazurayam.materialstore.core.filesystem.Stores;
import com.kazurayam.materialstore.core.util.JsonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StorageDirectoryTest {

    private static Path outputDir =
            Paths.get(System.getProperty("user.dir"))
                    .resolve("build/tmp/testOutput")
                    .resolve(StorageDirectoryTest.class.getName());
    static Store store;

    @BeforeAll
    public static void beforeClass() {
        Path root = outputDir.resolve("root");
        store = Stores.newInstance(root);
    }

    @Test
    public void test_toJson() {
        StorageDirectory dir =
                new StorageDirectory(store, new JobName("test_toJson"), JobTimestamp.now());
        String json = dir.toJson();
        System.out.println(json);
        String prettyPrinted = JsonUtil.prettyPrint(json);
        System.out.println(prettyPrinted);
    }

    @Test
    public void test_write_json() throws MaterialstoreException {
        JobName jobName = new JobName("test_write_json");
        JobTimestamp jobTimestamp = JobTimestamp.now();
        StorageDirectory dir =
                new StorageDirectory(store, jobName, jobTimestamp);
        String json = dir.toJson();
        System.out.println(json);
        Metadata metadata = Metadata.builder().put("foo","bar").build();
        store.write(jobName, jobTimestamp, FileType.JSON, metadata, json);
        QueryOnMetadata query = QueryOnMetadata.builder(metadata).build();
        Material material = store.selectSingle(jobName, jobTimestamp, FileType.JSON, query);
        assertNotNull(material);
    }

}
