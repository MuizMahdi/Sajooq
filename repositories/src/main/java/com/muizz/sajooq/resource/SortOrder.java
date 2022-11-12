package com.muizz.sajooq.resource;

public enum SortOrder {

    asc("ASCENDING"),
    desc("DESCENDING");

    public final String label;
    private SortOrder(String label) { this.label = label; }
    public String toString() { return label; }

}
