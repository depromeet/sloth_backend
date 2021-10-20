package com.sloth.domain.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "categoryId")
@Table(name = "category")
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    private int categoryLvl;

    private Long rootCategoryId;

    private String rootCategoryName;

    public Category(String categoryName, int categoryLvl, Long rootCategoryId, String rootCategoryName) {
        this.categoryName = categoryName;
        this.categoryLvl = categoryLvl;
        this.rootCategoryId = rootCategoryId;
        this.rootCategoryName = rootCategoryName;
    }
}
