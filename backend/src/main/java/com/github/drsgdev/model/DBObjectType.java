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

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "object_types")
@DynamicUpdate
@Data
@NoArgsConstructor
public class DBObjectType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @Column
  private String descr;

  @OneToMany(mappedBy = "type")
  @JsonBackReference
  private Set<DBObject> objects;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DBObjectType other = (DBObjectType) obj;
    return Objects.equals(descr, other.descr) && id == other.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(descr, id);
  }
}
