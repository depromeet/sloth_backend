package com.sloth.domain.site;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "site")
public class Site {

    @Id @GeneratedValue
    @Column(name = "site_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;
}
