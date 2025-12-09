package org.agluzhin.services;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.entities.User;
import org.agluzhin.repositories.ShortLinkRepository;

import java.awt.*;
import java.net.URI;
import java.security.SecureRandom;

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
        String shortCode = generateShortCode();
        ShortLink shortLink = new ShortLink(originalURL, shortCode, user.getId(), clickLimit, totalSeconds);
        SHORT_LINK_REPOSITORY.addLink(shortLink);
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

    private void visitLink(String shortCode) {
        try {
            ShortLink shortLink = SHORT_LINK_REPOSITORY.getLinkByCode(shortCode);

            if (shortLink == null) {
                throw new Exception(String.format("ссылка с кодом '%s' не найдена", shortCode));
            }

            if (shortLink.isExpired()) {
                SHORT_LINK_REPOSITORY.deleteLink(shortCode);
                NOTIFICATION_SERVICE.notifyUser(shortLink);
            }

            shortLink.incrementClicks();
            Desktop.getDesktop().browse(new URI(shortLink.getOriginalURL()));

            if (shortLink.isExpired()) {
                SHORT_LINK_REPOSITORY.deleteLink(shortCode);
                NOTIFICATION_SERVICE.notifyUser(shortLink);
            }
        } catch (Exception ex) {
            System.err.printf("\n%s.\n", ex.getMessage());
        }
    }
}
