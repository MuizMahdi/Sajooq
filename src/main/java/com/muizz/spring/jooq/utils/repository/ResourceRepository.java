package com.muizz.spring.jooq.utils.repository;

import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;

import com.muizz.spring.jooq.utils.entities.BaseEntity;
import com.muizz.spring.jooq.utils.resource.ResourceQuery;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ResourceRepository<R extends BaseEntity, Y extends UpdatableRecord<Y>, T extends Table<Y>> {
    public Mono<R> insert(R entity, Table<Y> entityTable);
    public Mono<R> findById(Long id, Table<Y> entityTable);
    public Mono<R> findFirstByField(TableField<Y, ?> field, String fieldValue);
    public Flux<R> findAllByField(TableField<Y, ?> field, String fieldValue);
    public Flux<R> findAllByQuery(ResourceQuery query, Table<Y> entityTable);
    public Mono<R> update(R entity, Table<Y> entityTable);
    public Mono<Integer> deleteById(Long id, Table<Y> entityTable);
}
