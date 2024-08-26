package com.partners.total.mydata.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUser(User user);

    @Query("SELECT a.deposit FROM Account a WHERE a.id = :id")
    Optional<Integer> findDepositById(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Account a SET a.deposit = :deposit WHERE a.id = :id")
    Optional<Integer> updateDepositById(@Param("id") Integer id, @Param("deposit") Integer deposit);
}
