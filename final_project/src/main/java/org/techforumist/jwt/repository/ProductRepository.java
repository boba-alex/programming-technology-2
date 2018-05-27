package org.techforumist.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.techforumist.jwt.domain.step.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}