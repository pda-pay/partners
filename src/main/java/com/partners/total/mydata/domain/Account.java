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

    public Account(String accountNumber, int deposit, User user, String companyCode, String category) {
        this.accountNumber = accountNumber;
        this.deposit = deposit;
        this.user = user;
        this.companyCode = companyCode;
        this.category = category;
    }
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
    @Column(name = "company_code")
    private String companyCode;
    private String category;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private List<Stocks> stocksList;
}
