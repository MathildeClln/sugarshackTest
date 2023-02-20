package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,String> {
    Stock findByProductId(String productId);
}
