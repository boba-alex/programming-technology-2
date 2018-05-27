package org.techforumist.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.techforumist.jwt.domain.Catalog;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    public List<Catalog> findAll();
    public List<Catalog> findAllByCreatorName(String creatorName);
    public List<Catalog> findOneById(Long id);
}