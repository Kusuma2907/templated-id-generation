package com.example.id_generator.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "id_counter",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_counter_scope",
                        columnNames = {
                                "counter_key",
                                "scope_key"
                        }
                )
        }
)
public class CounterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "counter_key",
            nullable = false,
            length = 100
    )
    private String counterKey;

    @Column(
            name = "scope_key",
            nullable = false,
            length = 100
    )
    private String scopeKey;

    @Column(
            name = "next_value",
            nullable = false
    )
    private long nextValue;

    protected CounterEntity() {
    }

    public CounterEntity(
            String counterKey,
            String scopeKey,
            long nextValue) {

        this.counterKey = counterKey;
        this.scopeKey = scopeKey;
        this.nextValue = nextValue;
    }

    public Long getId() {
        return id;
    }

    public String getCounterKey() {
        return counterKey;
    }

    public String getScopeKey() {
        return scopeKey;
    }

    public long getNextValue() {
        return nextValue;
    }

    public void setNextValue(long nextValue) {
        this.nextValue = nextValue;
    }
}