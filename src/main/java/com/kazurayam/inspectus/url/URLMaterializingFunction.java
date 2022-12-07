package com.kazurayam.inspectus.net;

import com.kazurayam.materialstore.core.filesystem.MaterialstoreException;

public interface URLMaterializingFunction<Target, Material> {

    Material accept(Target target) throws MaterialstoreException;
}
