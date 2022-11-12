package com.muizz.sajooq.entities;

import java.time.OffsetDateTime;

import org.springframework.data.relational.core.mapping.Column;


public class BaseEntity {

    private Long Id;
    @Column("created_on") private OffsetDateTime createdOn;
    @Column("updated_on") private OffsetDateTime updatedOn;


    public BaseEntity() { }


    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setUpdatedOn(OffsetDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn;
    }

}
