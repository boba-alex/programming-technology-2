package org.techforumist.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.techforumist.jwt.domain.step.ProductBlock;

public interface ProductBlockRepository extends JpaRepository<ProductBlock, Long> {

}