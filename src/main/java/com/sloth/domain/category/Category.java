package com.sloth.domain.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Table(name = "category")
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private int categoryLvl;

    private Long rootCategoryId;

    private String rootCategoryName;

    public Category(String name, int categoryLvl, Long rootCategoryId, String rootCategoryName) {
        this.name = name;
        this.categoryLvl = categoryLvl;
        this.rootCategoryId = rootCategoryId;
        this.rootCategoryName = rootCategoryName;
    }
}