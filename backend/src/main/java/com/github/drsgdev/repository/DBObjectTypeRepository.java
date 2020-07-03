package com.github.drsgdev.repository;

import java.util.Optional;

import com.github.drsgdev.model.DBObjectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBObjectTypeRepository extends JpaRepository<DBObjectType, Long> {
  public Optional<DBObjectType> findByType(String type);
}
