package com.company.model;

import static java.util.Objects.requireNonNull;

public enum Side {
    BUY(0, "BUY"), SELL(1, "SELL");

    private int ord;
    private String name;

    Side(int ord, String name) {
        this.ord = requireNonNull(ord);
        this.name = requireNonNull(name);
    }


    public String getName() {
        return new String(name);
    }
}
