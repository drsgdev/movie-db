package com.github.drsgdev.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attr_types")
@DynamicUpdate
@Data
@NoArgsConstructor
public class AttributeType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @Column(name = "descr")
  @JsonValue
  private String type;

  @OneToMany(mappedBy = "type")
  @JsonBackReference
  private Set<Attribute> attr;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AttributeType other = (AttributeType) obj;
    return id == other.id && Objects.equals(type, other.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type);
  }
}
