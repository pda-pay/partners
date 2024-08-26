package com.partners.total.mydata.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StocksRepository extends JpaRepository<Stocks, Integer> {
    List<Stocks> findByAccount(Account account);

    Optional<Stocks> findStocksByAccountIdAndStockCode(int accountId, String stockCode);

    @Modifying
    @Query("UPDATE Stocks s SET s.quantity = :quantity WHERE s.id = :id")
    Optional<Integer> updateQuantityByAccountIdAndQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

//    @Query("SELECT Stocks FROM Stocks WHERE account.id = :accountId")
    List<Stocks> findStocksByAccountId(int accountId);
}
