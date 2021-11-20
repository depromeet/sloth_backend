package com.sloth.domain.nickname;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="nickname")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nickname {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long nicknameId;

    private String name;

    private boolean isUsed;

    public void updateUsed() {
        this.isUsed = true;
    }

}
