package com.github.drsgdev.repository;

import java.util.List;
import java.util.Optional;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBObjectRepository extends JpaRepository<DBObject, Long> {
    Optional<List<DBObject>> findAllByType(DBObjectType type);

    Optional<List<DBObject>> findAllByTypeName(String typeName);

    Optional<List<DBObject>> findAllByTypeNameAndAttributesTypeNameAndAttributesVal(String typeName,
            String attributeType, String val);

    Optional<DBObject> findByTypeNameAndAttributesTypeNameAndAttributesVal(String typeName,
            String attributeType, String val);

    Optional<DBObject> findByDescrAndTypeName(String descr, String typeName);

    boolean existsByDescr(String descr);

    void deleteByDescrAndTypeName(String descr, String typeName);
}
