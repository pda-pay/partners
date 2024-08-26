package com.partners.total.mydata.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StocksRepository extends JpaRepository<Stocks, Integer> {
    List<Stocks> findByAccount(Account account);
}
