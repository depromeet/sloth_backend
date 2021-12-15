package com.sloth.domain.nickname;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="nickname")
@Getter
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
