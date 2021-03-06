package com.github.drsgdev.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.drsgdev.util.AttrTypes;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "objects")
@DynamicUpdate
@Data
@NoArgsConstructor
public class DBObject {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private long id;

  @ManyToOne
  @JoinColumn(name = "type_id")
  @JsonManagedReference
  private DBObjectType type;

  @Column
  private String descr;

  @OneToMany(mappedBy = "object", fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<AttributeValue> attributes = new HashSet<>();

  @Transient
  private Map<String, String> attributeMap = new HashMap<>();

  @JsonAnyGetter
  public Map<String, String> getAttributeMap() {
    return this.attributeMap;
  }

  @JsonAnySetter
  public void setAttribute(String name, String value) {
    this.attributeMap.put(name, value);
  }

  public void addAttribute(AttrTypes type, String name, String value) {
    AttributeType attrType = new AttributeType();
    attrType.setName(type.getValue());

    Attribute attr = new Attribute();
    attr.setName(name);
    attr.setType(attrType);

    AttributeValue attrVal = new AttributeValue();
    attrVal.setType(attr);
    attrVal.setObject(this);
    attrVal.setVal(value);

    this.attributes.add(attrVal);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DBObject other = (DBObject) obj;
    return id == other.id && Objects.equals(descr, other.descr) && Objects.equals(type, other.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descr, type);
  }

  @Override
  public String toString() {
    return "DBObject [id=" + id + ", type=" + type.getName() + ", descr=" + descr + "]";
  }
}
