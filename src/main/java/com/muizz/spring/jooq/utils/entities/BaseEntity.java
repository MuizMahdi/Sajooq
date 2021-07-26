package com.muizz.spring.jooq.utils.entities;

import java.time.OffsetDateTime;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data public class BaseEntity {

    private Long Id;
    @Column("created_on") private OffsetDateTime createdOn;
    @Column("updated_on") private OffsetDateTime updatedOn;

}
