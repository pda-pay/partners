package com.partners.total.mydata.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "Stocks")
public class Stocks {
    public Stocks() {}

    public Stocks(int quantity, Account account, String stockCode) {
        this.quantity = quantity;
        this.account = account;
        this.stockCode = stockCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(name = "stock_code")
    private String stockCode;
}
