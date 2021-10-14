package com.sloth.domain.category;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "category")
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private int categoryLvl;

    private String rootCategoryId;

    private String rootCategoryName;

}
