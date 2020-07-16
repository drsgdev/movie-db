package com.github.drsgdev.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.github.drsgdev.model.Attribute;
import com.github.drsgdev.model.AttributeType;
import com.github.drsgdev.model.AttributeValue;
import com.github.drsgdev.model.DBObject;
import com.github.drsgdev.model.DBObjectType;
import com.github.drsgdev.repository.AttributeRepository;
import com.github.drsgdev.repository.AttributeTypeRepository;
import com.github.drsgdev.repository.AttributeValueRepository;
import com.github.drsgdev.repository.DBObjectRepository;
import com.github.drsgdev.repository.DBObjectTypeRepository;
import com.github.drsgdev.util.AttrTypes;
import com.github.drsgdev.util.Types;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DBObjectService {

    private final DBObjectRepository objects;
    private final DBObjectTypeRepository types;
    private final AttributeRepository attributes;
    private final AttributeTypeRepository attrTypes;
    private final AttributeValueRepository attrValues;

    public void mapAttributes(List<DBObject> list) {
        list.forEach((object) -> mapAttributes(object));
    }

    public void mapAttributes(DBObject obj) {
        Map<String, String> fields = obj.getAttributes().parallelStream().collect(Collectors.toMap(
                (attr) -> attr.getType().getName(),
                (attr) -> attr.getVal() == null ? attr.getDate_val().toString() : attr.getVal()));

        obj.setAttributeMap(fields);
    }

    public Optional<DBObject> findObjectById(Long id) {
        Optional<DBObject> object = objects.findById(id);

        if (!object.isPresent()) {
            return Optional.empty();
        }

        log.info("Found object: {}", object.get());

        mapAttributes(object.get());

        return object;
    }

    public Optional<List<DBObject>> findAllByType(Types type) {
        Optional<List<DBObject>> objectList = objects.findAllByTypeName(type.getValue());

        if (!objectList.isPresent()) {
            return Optional.empty();
        }

        log.info("Found {} objects: {}", objectList.get().size(), objectList.get());

        mapAttributes(objectList.get());

        return objectList;
    }

    public DBObject findObjOrCreate(Types typeName, String descr) {
        DBObjectType type = findObjTypeByNameOrCreate(typeName);

        Optional<DBObject> objFromDB = objects.findByDescrAndTypeName(descr, typeName.getValue());

        if (!objFromDB.isPresent()) {
            log.warn("Object {} of type {} not found!", descr, typeName.getValue());

            DBObject newObject = new DBObject();
            newObject.setDescr(descr);
            newObject.setType(type);

            objects.save(newObject);
            log.info("Saved new object {} of type {}", descr, typeName.getValue());

            objFromDB = Optional.of(newObject);
        }

        return objFromDB.get();
    }

    public DBObjectType findObjTypeByNameOrCreate(Types typeName) {
        Optional<DBObjectType> objTypeFromDB = types.findByName(typeName.getValue());

        if (!objTypeFromDB.isPresent()) {
            log.warn("Object type {} not found!", typeName.getValue());

            DBObjectType newType = new DBObjectType();
            newType.setName(typeName.getValue());

            types.save(newType);
            log.info("Saved new type: {}", typeName.getValue());

            objTypeFromDB = Optional.of(newType);
        }

        return objTypeFromDB.get();
    }

    public AttributeType findAttrTypeByNameOrCreate(String name) {
        Optional<AttributeType> attrTypeFromDB = attrTypes.findByName(name);

        if (!attrTypeFromDB.isPresent()) {
            log.warn("Attribute type {} not found!", name);

            AttributeType newType = new AttributeType();
            newType.setName(name);

            attrTypes.save(newType);
            log.info("Saved new type: {}", name);

            attrTypeFromDB = Optional.of(newType);
        }

        return attrTypeFromDB.get();
    }

    public Attribute findAttrByNameAndTypeOrCreate(String name, AttributeType type) {
        Optional<Attribute> attributeFromDB = attributes.findByNameAndType(name, type);

        if (!attributeFromDB.isPresent()) {
            log.warn("Attribute {} of type {} not found!", name, type.getName());

            Attribute newAttribute = new Attribute();
            newAttribute.setName(name);
            newAttribute.setType(type);

            attributes.save(newAttribute);
            log.info("Saved new attribute: {} of type: {}", name, type.getName());

            attributeFromDB = Optional.of(newAttribute);
        }

        return attributeFromDB.get();
    }

    public void saveOrUpdateNewAttributeValue(String value, AttrTypes attrTypeName, String attrName,
            DBObject obj) {
        AttributeType attrType = findAttrTypeByNameOrCreate(attrTypeName.getValue());
        Attribute attr = findAttrByNameAndTypeOrCreate(attrName, attrType);

        Optional<AttributeValue> attrVal =
                attrValues.findByTypeNameAndObjectId(attrName, obj.getId());
        if (attrVal.isPresent()) {
            attrVal.get().setVal(value);

            attrValues.save(attrVal.get());
        } else {
            AttributeValue newAttrVal = new AttributeValue();
            newAttrVal.setType(attr);
            newAttrVal.setObject(obj);
            newAttrVal.setVal(value);

            attrValues.save(newAttrVal);
        }
    }
}
