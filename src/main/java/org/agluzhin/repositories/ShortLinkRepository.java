package org.agluzhin.repositories;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.utils.ValidationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ShortLinkRepository {
    private final Map<String, Map<String, ShortLink>> SHORT_LINKS = new HashMap<>();

    public void addLink(String userId, ShortLink shortLink) {
        ValidationUtil.requireNonNullOrEmpty("userId", userId);
        ValidationUtil.requireNonNull("shortLink", shortLink);

        Map<String, ShortLink> userShortLink;
        if (SHORT_LINKS.containsKey(userId)) {
            userShortLink = SHORT_LINKS.get(userId);
            userShortLink.put(shortLink.getShortUrl(), shortLink);
        } else {
            userShortLink = new HashMap<>();
            userShortLink.put(shortLink.getShortUrl(), shortLink);
            SHORT_LINKS.put(userId, userShortLink);
        }
    }

    public void deleteLink(String userId, String shortUrl) throws NoSuchElementException {
        ValidationUtil.requireNonNullOrEmpty("userId", userId);
        ValidationUtil.requireNonNullOrEmpty("shortUrl", shortUrl);

        Map<String, ShortLink> userLinks;
        if (SHORT_LINKS.containsKey(userId)) {
            userLinks = SHORT_LINKS.get(userId);
            if (userLinks.containsKey(shortUrl)) {
                userLinks.remove(shortUrl);
            } else {
                throw new NoSuchElementException(
                        String.format(
                                "ссылка '%s' у пользователя с id '%s' не найдена",
                                shortUrl,
                                userId
                        )
                );
            }
        } else {
            throw new NoSuchElementException(
                    String.format(
                            "пользователь с id '%s' не найден",
                            userId
                    )
            );
        }
    }

    public ShortLink getLink(String userId, String shortUrl) throws NoSuchElementException {
        ValidationUtil.requireNonNullOrEmpty("userId", userId);
        ValidationUtil.requireNonNullOrEmpty("shortUrl", shortUrl);

        Map<String, ShortLink> userLinks;
        if (SHORT_LINKS.containsKey(userId)) {
            userLinks = SHORT_LINKS.get(userId);
            if (userLinks.containsKey(shortUrl)) {
                return userLinks.get(shortUrl);
            } else {
                throw new NoSuchElementException(
                        String.format(
                                "ссылка '%s' у пользователя с id '%s' не найдена",
                                shortUrl,
                                userId
                        )
                );
            }
        } else {
            throw new NoSuchElementException(
                    String.format(
                            "пользователь с id '%s' не найден",
                            userId
                    )
            );
        }
    }

    public Map<String, ShortLink> getAllUserLinks(String userId) throws NoSuchElementException {
        ValidationUtil.requireNonNullOrEmpty("userId", userId);

        if (SHORT_LINKS.containsKey(userId)) {
            return SHORT_LINKS.get(userId);
        } else {
            throw new NoSuchElementException(
                    String.format(
                            "у пользователя '%s' пока нет ссылок",
                            userId
                    )
            );
        }
    }
}
