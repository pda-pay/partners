package com.partners.total.mydata.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StocksRepository extends JpaRepository<Stocks, Integer> {
    List<Stocks> findByAccount(Account account);

    Optional<Stocks> findStocksByAccountIdAndStockCode(int accountId, String stockCode);
}
