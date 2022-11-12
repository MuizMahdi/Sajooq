package com.muizz.sajooq.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.muizz.sajooq.entities.BaseEntity;
import com.muizz.sajooq.exceptions.InvalidOperationException;
import com.muizz.sajooq.exceptions.TableFieldNotFoundException;
import com.muizz.sajooq.resource.ResourceQuery;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Transactional
@SuppressWarnings("unchecked")
public abstract class DefaultResourceRepository<R extends BaseEntity, Y extends UpdatableRecord<Y>, T extends Table<Y>>
implements ResourceRepository<R, Y, T> {


    @Autowired private DSLContext db;
    private Class<R> entityType;

    public DefaultResourceRepository() {
        initializeEntityType();
    }


    
    /**
     * Inserts an entity into the specified table 
     * 
     * @param entity        Entity model which extends BaseModel
     * @param entityTable   JOOQ table
     * @return Mono<R>      Inserted entity model with its ID
     */
    public Mono<R> insert(R entity, Table<Y> entityTable) {

        var record = new UpdatableRecordImpl<>(entityTable);
        record.from(entity);

        return Mono.from(

            // Insert entity and return its id field (first field)
            db
            .insertInto(entityTable)
            .set(record)
            .returningResult(entityTable.fields("id"))

        ).flatMap(r -> {

            // Return entity with it's saved/created id
            entity.setId(Long.parseLong(r.toString()));
            return Mono.just(entity);

        });

    }


    
    /**
     * Selects an entity by ID from the specified table 
     * 
     * @param id            Entity's ID value
     * @param entityTable   JOOQ table
     * @return Mono<R>      Selected entity
     */
    public Mono<R> findById(Long id, Table<Y> entityTable) {

        return Mono.from(
        
            // Select by id
            db.selectFrom(entityTable).where(getIdField(entityTable).like(Long.toString(id))).limit(1)
            
        ).flatMap(r -> {

            // Map into the specified type
            return Mono.just(r.into(entityType));

        });

    }


    
    /** 
     * Selects an entity using the specified field (column) and its value
     * 
     * @param field         JOOQ table field (table column)
     * @param fieldValue    Value to match for the Where clause
     * @return Mono<R>      Selected entity
     */
    public Mono<R> findFirstByField(TableField<Y, ?> field, String fieldValue) {

        return Mono.from(
        
            // Select by field and value
            db.selectFrom(field.getTable()).where(field.like(fieldValue)).limit(1)

        ).flatMap(r -> {

            // Map into the specified type
            return Mono.just(r.into(entityType));

        });

    }


    
    /**
     * Selects all entities by specified field and its value 
     * 
     * @param field         JOOQ table field (table column)
     * @param fieldValue    Value to match for the Where clause
     * @return Flux<R>      Selected entities
     */
    public Flux<R> findAllByField(TableField<Y, ?> field, String fieldValue) {

        return Flux.from(
        
            // Select by field
            db.selectFrom(field.getTable()).where(field.like(fieldValue))

        ).map(r -> r.into(entityType)); // Map into specified type

    }


    
    /**
     * Selects entities by query
     * 
     * @param entityTable   JOOQ table
     * @param query         Query which contains pagination parameters, sorting parameters, and filters
     * @return Flux<R>      Selected entities
     */
    public Flux<R> findAllByQuery(ResourceQuery query, Table<Y> entityTable) throws InvalidOperationException {

        var hasSortFields = query.getSortFieldsOptional().isPresent() && query.getSortFields().size() > 0;
        var hasPage = query.getPageOptional().isPresent();
        var hasSeek = hasPage && query.getPage().getSeekIdOptional().isPresent();
        var hasFilters = query.getFiltersOptional().isPresent();
        var usesSeek = (hasPage && hasSeek && !hasSortFields);
        var usesOffset = (hasPage && query.getPage().getOffsetOptional().isPresent());

        // Validate pagination methods intersection
        if (usesSeek && usesOffset) throw new InvalidOperationException("Use either Seek or Offset method for pagination");

        // Construct initial query
        var resourceQuery = db
        .selectFrom(entityTable);

        // Select by filters
        if (hasFilters) resourceQuery.where(query.getFilters());

        // Order by custom sort fields if specified
        if (hasSortFields) resourceQuery.orderBy(query.getSortFields());

        // Using seek for pagination (requires Id ordering, and no custom sort fields)
        if (usesSeek) {

            resourceQuery
            .orderBy(getIdField(entityTable).asc())
            .seek(query.getPage().getSeekId())
            .limit(query.getPage().getLimit());

        }
        // Using offset for pagination
        else if (usesOffset) {

            resourceQuery
            .offset(query.getPage().getOffset())
            .limit(query.getPage().getLimit());

        }

        return Flux.from(resourceQuery).map(r -> r.into(entityType));

    }



    /** 
     * @param entity        Entity model which extends BaseModel
     * @param entityTable   JOOQ table
     * @return Mono<R>      Updated entity
     */
    public Mono<R> update(R entity, Table<Y> entityTable) {

        var record = new UpdatableRecordImpl<>(entityTable);
        record.from(entity);

        return Mono.from(

            // Update entity
            db
            .update(entityTable)
            .set(record)

        ).flatMap(id -> {

            // Return updated entity
            return Mono.just(entity);

        });

    }

    
    /** 
     * @param id                Entity's ID value
     * @param entityTable       JOOQ table
     * @return Mono<Integer>    Deleted entity's ID value
     */
    public Mono<Integer> deleteById(Long id, Table<Y> entityTable) {
        return Mono.from(
            db.deleteFrom(entityTable).where(getIdField(entityTable).like(Long.toString(id)))
        );
    }


    //#region Helpers 


    /**
     * Finds the repository's generic entity type used for mapping query results into the specified resource's entity class
     */
    private void initializeEntityType() {
        Type genericEntityType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (genericEntityType instanceof ParameterizedType) entityType = (Class<R>) ((ParameterizedType) genericEntityType).getRawType();
        else entityType = (Class<R>) genericEntityType;
    }


    
    /**
     * Finds the ID field of the specified JOOQ table 
     * 
     * @param entityTable   JOOQ table
     * @return Field<Long>  JOOQ field of the ID
     */
    private Field<Long> getIdField(Table<Y> entityTable) {

        // Get the Id field of the entity
        var idField = Arrays.asList(entityTable.fields("id")).stream()
        .filter(f -> f.getName().equals("id"))
        .findFirst();

        // Verify Id field
        if (!idField.isPresent()) throw new TableFieldNotFoundException("Table has no Id field");

        return (Field<Long>) idField.get();

    }


    //#endregion
}
