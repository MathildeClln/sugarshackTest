package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine,Integer> {
    OrderLine findByProductId(String productId);

    OrderLine findFirstByOrderByIdDesc();

    boolean existsByProductId(String productId);

    void deleteByProductId(String productId);
}
