package org.agluzhin.repositories;

import org.agluzhin.entities.ShortLink;

import java.util.HashMap;
import java.util.Map;

public class ShortLinkRepository {
    private final Map<String, ShortLink> LINKS = new HashMap<>();

    public void addLink(ShortLink link) {
        LINKS.put(link.getShortCode(), link);
    }

    public void deleteLink(String code) {
        LINKS.remove(code);
    }

    public ShortLink getLinkByCode(String code) {
        return LINKS.get(code);
    }

    public Map<String, ShortLink> getAllLinks() {
        return LINKS;
    }
}
