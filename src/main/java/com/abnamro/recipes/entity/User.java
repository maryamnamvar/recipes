package com.abnamro.recipes.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "users")
@Getter
@Setter
public class User {

    @Id
    private String userId;

    private String firstName;
    private String lastName;
}
