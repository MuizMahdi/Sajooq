package com.muizz.spring.jooq.utils.resource;

import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.SortField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
