package com.partners.total.mydata.domain;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "User")
public class User {
    public User() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Account> account;
}
