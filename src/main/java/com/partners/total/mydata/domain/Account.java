package com.partners.total.mydata.domain;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Account")
public class Account {
    public Account() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String account_number;
    private int deposit;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String companyCode;
    private String category;

    @OneToMany(mappedBy = "account")
    private List<Stocks> stocks;

}
