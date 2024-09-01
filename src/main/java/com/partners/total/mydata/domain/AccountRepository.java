package com.partners.total.mydata.domain;

import com.partners.total.mydata.dto.AccountForRepaymentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByUser(User user);

    @Query("SELECT a.deposit FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Integer> findDepositByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT new com.partners.total.mydata.dto.AccountForRepaymentResponse(a.deposit, a.companyCode)" +
            " FROM Account a " +
            "WHERE a.accountNumber = :accountNumber")
    Optional<AccountForRepaymentResponse> findDepositAndCompanyCodeByAccountNumber(@Param("accountNumber") String accountNumber);
    
    Optional<Account> findAccountByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE Account a SET a.deposit = :deposit WHERE a.accountNumber = :accountNumber")
    Optional<Integer> updateDepositByAccountNumber(@Param("accountNumber") String accountNumber, @Param("deposit") Integer deposit);
}
