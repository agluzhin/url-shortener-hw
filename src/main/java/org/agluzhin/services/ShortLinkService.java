package org.agluzhin.services;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.entities.User;
import org.agluzhin.repositories.ShortLinkRepository;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.NoSuchElementException;

public class ShortLinkService {
    private static final String BASE_URL = "https://clck.ru/";

    private final ShortLinkRepository SHORT_LINK_REPOSITORY;
    private final NotificationService NOTIFICATION_SERVICE;
    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    public ShortLinkService(ShortLinkRepository shortLinkRepository, NotificationService notificationService) {
        SHORT_LINK_REPOSITORY = shortLinkRepository;
        NOTIFICATION_SERVICE = notificationService;
    }

    public ShortLink createShortLink(String originalURL, User user, int clickLimit, long totalSeconds) {
        String shortUrl = BASE_URL + generateShortCode();
        ShortLink shortLink = new ShortLink(originalURL, shortUrl, user.getId(), clickLimit, totalSeconds);
        SHORT_LINK_REPOSITORY.addLink(user.getId(), shortLink);
        return shortLink;
    }

    private String generateShortCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(7);
        for (int i = 0; i < 7; i++) {
            sb.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void visitLink(String userId, String shortUrl)
            throws NoSuchElementException, URISyntaxException, IOException {
        ShortLink shortLink = SHORT_LINK_REPOSITORY.getLink(userId, shortUrl);

        if (shortLink == null) {
            throw new NoSuchElementException(
                    String.format(
                            "ссылка '%s' для пользователя '%s' не найдена",
                            shortUrl,
                            userId
                    )
            );
        }

        if (shortLink.isExpired()) {
            SHORT_LINK_REPOSITORY.deleteLink(userId, shortUrl);
            NOTIFICATION_SERVICE.notifyUser(shortLink);
        }

        shortLink.incrementClicks();
        Desktop.getDesktop().browse(new URI(shortLink.getOriginalURL()));
    }
}
