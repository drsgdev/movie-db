package com.github.drsgdev.repository;

import java.util.Optional;
import com.github.drsgdev.model.Attribute;
import com.github.drsgdev.model.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    public Optional<Attribute> findByName(String name);

    public Optional<Attribute> findByNameAndType(String name, AttributeType type);
}
