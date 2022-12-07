package com.kazurayam.materialstore.base.materialize;

import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;

import java.io.File;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

public abstract class TargetCSVParser {

    public static TargetCSVParser newSimpleParser() {
        return new SimpleTargetCSVParser();
    }

    public abstract List<Target> parse(File file) throws MaterialstoreException;

    public abstract List<Target> parse(Path path) throws MaterialstoreException;

    public abstract List<Target> parse(Reader reader) throws MaterialstoreException;

    public abstract List<Target> parse(String string) throws MaterialstoreException;

}
