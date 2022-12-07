package com.kazurayam.inspectus.discovery;

import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Sitemap {

    private final Logger logger = LoggerFactory.getLogger(Sitemap.class);

    private final Target baseTopPage;
    private final Target twinTopPage;
    private final List<Target> targetList;

    public Sitemap(Target baseTopPage) {
        this(baseTopPage, Target.NULL_OBJECT);
    }

    public Sitemap(Target baseTopPage, Target twinTopPage) {
        this.baseTopPage = baseTopPage;
        this.twinTopPage = twinTopPage;
        targetList = new ArrayList<>();
        this.add(baseTopPage);
    }

    public Target getBaseTopPage() {
        return baseTopPage;
    }

    public Target getTwinTopPage() {
        return twinTopPage;
    }

    public void add(Target target) {
        Objects.requireNonNull(target);
        if (target.getUrl().getHost().equals(baseTopPage.getUrl().getHost())) {
            targetList.add(target);
        } else {
            logger.warn(String.format("host names differ. target=%s, baseTopPage=%s.",
                    target.toJson(), baseTopPage.toJson()));
        }
    }

    public void add(String spec, By handle) throws IllegalArgumentException {
        Objects.requireNonNull(spec);
        Objects.requireNonNull(handle);
        try {
            URL url = new URL(baseTopPage.getUrl(), spec);
            Target target = new Target.Builder(url).handle(handle).build();
            this.targetList.add(target);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public int size() {
        return targetList.size();
    }

    public Target getBaseTarget(int index) {
        return targetList.get(index);
    }

    public List<Target> getBaseTargetList() {
        return Collections.unmodifiableList(targetList);
    }

    public Target getTwinTarget(int index) {
        if (this.twinTopPage != Target.NULL_OBJECT) {
            return deriveTwinTarget(targetList.get(index));
        } else {
            logger.warn("getTwinTarget() was called but twinTopPage is not set");
            return Target.NULL_OBJECT;
        }
    }

    public List<Target> getTwinTargetList() {
        List<Target> result = new ArrayList<>();
        for (Target t : targetList) {
            result.add(new Target.Builder(deriveTwinTarget(t)).build());
        }
        return Collections.unmodifiableList(result);
    }

    public Target deriveTwinTarget(Target base) {
        String baseProtocol = base.getUrl().getProtocol();
        //String baseHost = base.getUrl().getHost();
        String twinHost = this.twinTopPage.getUrl().getHost();
        int basePort = base.getUrl().getPort();
        String baseFile = base.getUrl().getFile();
        try {
            URL twinUrl = new URL(baseProtocol, twinHost, basePort, baseFile);
            Target twinTarget = new Target.Builder(twinUrl).handle(base.getHandle()).build();
            return twinTarget;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
