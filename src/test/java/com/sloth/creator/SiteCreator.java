package com.sloth.creator;

import com.sloth.domain.nickname.Nickname;
import com.sloth.domain.site.Site;

public class SiteCreator {

    public static Site create (String name) {
        return Site.builder()
                .siteName(name)
                .build();
    }
}
