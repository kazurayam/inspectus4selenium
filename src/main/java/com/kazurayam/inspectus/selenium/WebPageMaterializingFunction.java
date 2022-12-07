package com.kazurayam.inspectus.selenium;

import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;

@FunctionalInterface
public interface PageMaterializingFunction<WebDriver, Target, Map, Material> {

    Material accept(WebDriver driver, Target target, Map attributes) throws MaterialstoreException;
}
