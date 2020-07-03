package com.github.drsgdev.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonValue;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attr")
@DynamicUpdate
@Data
@NoArgsConstructor
public class Attribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @ManyToOne
  @JoinColumn(name = "type_id")
  @JsonManagedReference
  private AttributeType type;

  @Column
  @JsonValue
  private String descr;

  @OneToMany(mappedBy = "attribute")
  @JsonBackReference
  private Set<AttributeValue> value;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Attribute other = (Attribute) obj;
    return id == other.id && Objects.equals(descr, other.descr) && Objects.equals(type, other.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descr, type);
  }
}
