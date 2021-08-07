package com.muizz.sajooq.resource;

import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.SortField;


public class ResourceQuery {

    private ResourcePage page;
    private List<SortField<?>> sortFields;
    private List<Condition> filters;


    public Optional<ResourcePage> getPageOptional() {
        return Optional.ofNullable(page);
    }


    public Optional<List<SortField<?>>> getSortFieldsOptional() {
        return Optional.ofNullable(sortFields);
    }


    public Optional<List<Condition>> getFiltersOptional() {
        return Optional.ofNullable(filters);
    }

    public void setPage(ResourcePage page) {
        this.page = page;
    }

    public ResourcePage getPage() {
        return page;
    }

    public void setSortFields(List<SortField<?>> sortFields) {
        this.sortFields = sortFields;
    }

    public List<SortField<?>> getSortFields() {
        return sortFields;
    }

    public void setFilters(List<Condition> filters) {
        this.filters = filters;
    }

    public List<Condition> getFilters() {
        return filters;
    }

}
