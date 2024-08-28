package com.example.demo.model;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName name;
    protected Role() {}
    public Role(RoleName name) {
        this.name = name;
    }
}
