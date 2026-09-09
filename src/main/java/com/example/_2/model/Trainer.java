package com.example._2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // INTERNAL, EXTERNAL

    private String expertise;

    public Trainer() {
    }

    public Trainer(Long id, String name, String type, String expertise) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.expertise = expertise;
    }

    public Trainer(String name, String type, String expertise) {
        this.name = name;
        this.type = type;
        this.expertise = expertise;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}
