package com.tsystems.banking.repository;

import com.tsystems.banking.models.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findByUserId(Long userId);

  List<Account> findByBalanceLessThan(Double amount);
}
