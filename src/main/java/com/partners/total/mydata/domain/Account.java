package com.partners.total.mydata.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "Account")
public class Account {
    public Account() {}

    public Account(String accountNumber, int deposit, User user, String category) {
        this.accountNumber = accountNumber;
        this.deposit = deposit;
        this.user = user;
        this.category = category;
    }
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "account_number")
    private String accountNumber;
    private int deposit;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String category;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Stocks> stocksList;
}
