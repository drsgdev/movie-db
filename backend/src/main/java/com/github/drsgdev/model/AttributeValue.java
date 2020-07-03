package com.github.drsgdev.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attr_values")
@DynamicUpdate
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AttributeValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private long id;

  @ManyToOne
  @JoinColumn(name = "attr_id")
  @JsonManagedReference
  private Attribute attribute;

  @ManyToOne
  @JoinColumn(name = "obj_id")
  @JsonBackReference
  private DBObject object;

  @Column
  private String val;

  @Column
  private Date date_val;
}
