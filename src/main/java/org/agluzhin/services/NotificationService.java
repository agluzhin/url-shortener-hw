package org.agluzhin.services;

import org.agluzhin.entities.ShortLink;

public class NotificationService {
    public void notifyUser(ShortLink link) throws Exception {
        if (link.getClickCount() >= link.getClickLimit()) {
            throw new Exception(String.format("ссылка '%s' недоступна - лимит переходов исчерпан\n", link.getShortCode()));
        } else {
            throw new Exception(String.format("ссылка '%s' недоступна - срок жизни истек\n", link.getShortCode()));
        }
    }
}
