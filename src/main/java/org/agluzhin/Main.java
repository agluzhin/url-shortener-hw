package org.agluzhin;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.entities.User;
import org.agluzhin.repositories.ShortLinkRepository;
import org.agluzhin.services.NotificationService;
import org.agluzhin.services.ShortLinkService;
import org.agluzhin.utils.ValidationUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ShortLinkRepository shortLinkRepository = new ShortLinkRepository();
        NotificationService notificationService = new NotificationService();
        ShortLinkService shortLinkService = new ShortLinkService(shortLinkRepository, notificationService);

        User user = new User();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println();
                System.out.println("1. Создать короткую ссылку;");
                System.out.println("2. Перейти по короткой ссылке;");
                System.out.println("3. Просмотреть все короткие ссылки;");
                System.out.println("4. Выйти.");
                System.out.print("Выберите действие: ");
                int userChoice = Integer.parseInt(scanner.nextLine());

                switch (userChoice) {
                    case 1:
                        System.out.println();
                        handleCreateLink(scanner, user, shortLinkService);
                        break;
                    case 2:
                        System.out.println();
                        handleVisitLink(scanner, user, shortLinkService);
                        break;
                    case 3:
                        System.out.println();
                        handleShowLinks(user, shortLinkRepository);
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("До свидания!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("\nОшибка: неверный выбор действия. Пожалуйста, выберите от 1 до 4.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\nОшибка: введите число от 1 до 4 для выбора действия.");
            } catch (Exception ex) {
                System.out.println("\nОшибка: " + ex.getMessage());
            }
        }
    }

    private static void handleCreateLink(Scanner scanner, User user, ShortLinkService shortLinkService) {
        try {
            System.out.print("Введите оригинальную ссылку: ");
            String originalUrl = scanner.nextLine();
            ValidationUtil.requireNonNullOrEmpty("originalUrl", originalUrl);

            System.out.print("Введите лимит переходов: ");
            String clickLimit = scanner.nextLine();
            ValidationUtil.requireNonNullOrEmpty("clickLimit", clickLimit);

            System.out.print("Введите время жизни ссылки в секундах: ");
            String lifeLimit = scanner.nextLine();
            ValidationUtil.requireNonNullOrEmpty("lifeLimit", lifeLimit);

            ShortLink shortLink = shortLinkService.createShortLink(
                    originalUrl,
                    user,
                    Integer.parseInt(clickLimit),
                    Long.parseLong(lifeLimit)
            );

            System.out.printf("Ваша короткая ссылка: %s%n", shortLink.getShortUrl());
        } catch (NumberFormatException ex) {
            System.out.println("\nОшибка: поля 'clickLimit' и 'lifeLimit' должны быть целыми числами");
        } catch (IllegalArgumentException ex) {
            System.out.println("\nОшибка: " + ex.getMessage());
        }
    }

    private static void handleVisitLink(Scanner scanner, User user, ShortLinkService shortLinkService) {
        try {
            System.out.print("Введите короткую ссылку: ");
            String shortUrl = scanner.nextLine();
            ValidationUtil.requireNonNullOrEmpty("shortUrl", shortUrl);

            shortLinkService.visitLink(user.getId(), shortUrl);
        } catch (IllegalArgumentException ex) {
            System.out.println("\nОшибка: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.out.println("\nОшибка: проблема с ссылкой 'originalLink'");
        } catch (IOException ex) {
            System.out.println("\nОшибка: проблема с открытием ссылки 'originalLink'");
        }
    }

    private static void handleShowLinks(User user, ShortLinkRepository shortLinkRepository) {
        try {
            System.out.printf("Все короткие ссылки для пользователя %s:%n", user.getId());

            Map<String, ShortLink> allUserLinks = shortLinkRepository.getAllUserLinks(user.getId());
            for (String url : allUserLinks.keySet()) {
                if (!allUserLinks.get(url).isExpired()) {
                    System.out.println(url);
                }
            }
        } catch (NoSuchElementException ex) {
            System.out.println("\nОшибка: " + ex.getMessage());
        }
    }
}