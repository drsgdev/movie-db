package com.github.drsgdev.repository;

import java.util.List;
import java.util.Optional;

import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBObjectRepository extends JpaRepository<DBObject, Long> {
  public Optional<List<DBObject>> findAllByType(DBObjectType type);

  public Optional<List<DBObject>> findAllByTypeName(String typeName);

  public Optional<List<DBObject>> findAllByTypeNameAndAttributesTypeNameAndAttributesVal(
      String typeName, String attributeType, String val);

  public Optional<DBObject> findByTypeNameAndAttributesTypeNameAndAttributesVal(String typeName,
      String attributeType, String val);

  public Optional<DBObject> findByDescrAndTypeName(String descr, String typeName);

  public boolean existsByDescr(String descr);
}
