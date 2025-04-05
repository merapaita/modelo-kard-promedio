package com.rosist.kardex.security4.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;
    private String httpMethod;
    private boolean permitAll;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

}
