package com.example.productmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
