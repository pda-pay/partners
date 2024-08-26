package com.partners.total.mydata.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "Stocks")
public class Stocks {
    public Stocks() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private String stockCode;
}
