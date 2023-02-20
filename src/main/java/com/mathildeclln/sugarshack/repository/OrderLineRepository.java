package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine,String> {
    OrderLine findByProductId(String productId);

    boolean existsByProductId(String productId);

    void deleteByProductId(String productId);
}
