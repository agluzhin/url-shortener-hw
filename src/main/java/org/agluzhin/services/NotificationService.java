package org.agluzhin.services;

import org.agluzhin.entities.ShortLink;

public class NotificationService {
    public void notifyUser(ShortLink link) throws IllegalStateException {
        if (link.getClickCount() >= link.getClickLimit()) {
            throw new IllegalStateException(
                    String.format(
                            "ссылка '%s' недоступна - лимит переходов исчерпан",
                            link.getShortUrl()
                    )
            );
        } else {
            throw new IllegalStateException(
                    String.format(
                            "ссылка '%s' недоступна - срок жизни истек\n",
                            link.getShortUrl()
                    )
            );
        }
    }
}
