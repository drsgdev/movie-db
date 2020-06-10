package com.github.drsgdev.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @ManyToOne
  @JoinColumn(name = "type_id")
  @JsonManagedReference
  private DBObjectType type;

  @Column
  private String descr;

  @OneToMany(mappedBy = "object", fetch = FetchType.EAGER)
  @JsonManagedReference
  private Set<AttributeValue> attributes;

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
}
