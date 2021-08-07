package com.muizz.sajooq.resource;

public class ResourceSort {

    private String sortBy;
    private SortOrder order;


    public ResourceSort() { }

    public ResourceSort(
        String sortBy,
        SortOrder order
    ) {
        this.sortBy = sortBy;
        this.order = order;
    }


    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }

    public SortOrder getOrder() {
        return order;
    }

}
