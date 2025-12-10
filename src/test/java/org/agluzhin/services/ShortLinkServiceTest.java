package org.agluzhin.services;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.entities.User;
import org.agluzhin.repositories.ShortLinkRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Сервис коротких ссылок")
class ShortLinkServiceTest {

    private ShortLinkService shortLinkService;
    private ShortLinkRepository shortLinkRepository;

    @BeforeEach
    void setUp() {
        NotificationService notificationService = new NotificationService();
        shortLinkRepository = new ShortLinkRepository();
        shortLinkService = new ShortLinkService(shortLinkRepository, notificationService);
    }

    @Test
    @DisplayName("Генерация уникальных коротких ссылок для двух разных пользователей (источник общий).")
    public void generateShortLinks() {
        String originalUrl = "https://stackoverflow.com/";

        User user1 = new User();
        User user2 = new User();

        ShortLink shortLink1 = shortLinkService.createShortLink(originalUrl, user1, 0, 0);
        ShortLink shortLink2 = shortLinkService.createShortLink(originalUrl, user2, 0, 0);

        assertNotEquals(shortLink1.getShortUrl(), shortLink2.getShortUrl());
    }

    @Test
    @DisplayName("Блокировка ссылки по достижению лимита переходов + проверка удаления.")
    public void blockShortLinkAfterClickLimitReached() {
        User user = new User();
        String originalUrl = "https://stackoverflow.com/";
        int clickLimit = 1;

        ShortLink shortLink = shortLinkService.createShortLink(originalUrl, user, clickLimit, 500);

        assertDoesNotThrow(() -> shortLinkService.visitLink(user.getId(), shortLink.getShortUrl()));

        Exception illegalStateException = assertThrows(
                IllegalStateException.class, () -> shortLinkService.visitLink(
                        user.getId(),
                        shortLink.getShortUrl()
                )
        );
        assertTrue(illegalStateException.getMessage().contains("лимит переходов исчерпан"));

        Exception noSuchElementException = assertThrows(
                NoSuchElementException.class, () -> shortLinkRepository.getLink(
                        user.getId(),
                        shortLink.getShortUrl()
                )
        );
        String targetErrorMessage = String.format(
                "ссылка '%s' у пользователя с id '%s' не найдена",
                shortLink.getShortUrl(),
                user.getId()
        );
        assertEquals(noSuchElementException.getMessage(), targetErrorMessage);
    }

    @Test
    @DisplayName("Блокировка ссылки по достижению лимита срока жизни ссылки + проверка удаления.")
    public void blockShortLinkAfterLifeLimitReached() throws InterruptedException {
        User user = new User();
        String originalUrl = "https://stackoverflow.com/";
        int clickLimit = 3;
        long lifeLimit = 5;

        ShortLink shortLink = shortLinkService.createShortLink(originalUrl, user, clickLimit, lifeLimit);

        Thread.sleep(6000);

        Exception ex = assertThrows(
                IllegalStateException.class, () -> shortLinkService.visitLink(
                        user.getId(),
                        shortLink.getShortUrl()
                )
        );
        assertTrue(ex.getMessage().contains("срок жизни истек"));

        Exception noSuchElementException = assertThrows(
                NoSuchElementException.class, () -> shortLinkRepository.getLink(
                        user.getId(),
                        shortLink.getShortUrl()
                )
        );
        String targetErrorMessage = String.format(
                "ссылка '%s' у пользователя с id '%s' не найдена",
                shortLink.getShortUrl(),
                user.getId()
        );
        assertEquals(noSuchElementException.getMessage(), targetErrorMessage);
    }



}