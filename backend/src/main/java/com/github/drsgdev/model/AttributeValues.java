package com.github.drsgdev.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attr_values")
@DynamicUpdate
@Data
@NoArgsConstructor
public class AttributeValues {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "attr_id", nullable = false)
  private Attributes attr;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "obj_id")
  private Objects obj;

  @Column
  private String val;

  @Column
  private Date date_val;
}
