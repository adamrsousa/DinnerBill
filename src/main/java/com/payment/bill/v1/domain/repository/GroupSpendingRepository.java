package com.payment.bill.v1.domain.repository;

import com.payment.bill.v1.domain.model.GroupSpending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupSpendingRepository extends JpaRepository<GroupSpending, Long> {
}
