package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product findById(int id);

    ArrayList<Product> findAllByType(MapleType type);
}
