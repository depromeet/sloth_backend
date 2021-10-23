package com.sloth.domain.site;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "siteId")
@Table(name = "site")
public class Site {

    @Id @GeneratedValue
    @Column(name = "site_id")
    private Long siteId;

    @Column(nullable = false, length = 100)
    private String siteName;

    public Site(String siteName) {
        this.siteName = siteName;
    }
}
