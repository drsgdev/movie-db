package com.github.drsgdev.repository;

import java.util.Optional;

import com.github.drsgdev.model.AttributeValue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
  public Optional<AttributeValue> findByTypeNameAndValAndObjectId(String typeName, String val, Long id);
}
