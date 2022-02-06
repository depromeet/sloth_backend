package com.sloth.creator;

import com.sloth.domain.site.Site;

public class SiteCreator {

    public static Site create (String name) {
        return Site.builder()
                .siteName(name)
                .build();
    }

    public static Site create (Long siteId, String name) {
        return Site.builder()
                .siteName(name)
                .siteId(siteId)
                .build();
    }

}
