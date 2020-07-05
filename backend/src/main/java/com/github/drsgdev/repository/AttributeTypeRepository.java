package com.github.drsgdev.repository;

import java.util.Optional;

import com.github.drsgdev.model.AttributeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long> {
  public Optional<AttributeType> findByName(String name);
}
