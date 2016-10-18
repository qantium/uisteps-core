package com.qantium.uisteps.core.utils.grid.client;

/**
 * Created by Anton Solyankin
 */
public enum NodeType {

    HUB("grid/admin"), NODE("extra");

    private final String url;

    NodeType(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }
}
