package org.agluzhin;

import org.agluzhin.entities.ShortLink;
import org.agluzhin.entities.User;
import org.agluzhin.repositories.ShortLinkRepository;
import org.agluzhin.services.NotificationService;
import org.agluzhin.services.ShortLinkService;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ShortLinkRepository shortLinkRepository = new ShortLinkRepository();
        NotificationService notificationService = new NotificationService();
        ShortLinkService shortLinkService = new ShortLinkService(shortLinkRepository, notificationService);

        User user = new User();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("1. Создать короткую ссылку;");
            System.out.println("2. Перейти по короткой ссылке;");
            System.out.println("3. Выйти.");
            System.out.print("Выберите действие: ");
            int userChoice = Integer.parseInt(scanner.nextLine());

            switch(userChoice) {
                case 1:
                    System.out.print("\nВведите оригинальную ссылку: ");
                    String originalUrl = scanner.nextLine();
                    System.out.print("Введите лимит переходов: ");
                    int clickLimit = Integer.parseInt(scanner.nextLine());
                    System.out.print("Введите время жизни ссылки в секундах: ");
                    long lifeLimit = Long.parseLong(scanner.nextLine());

                    ShortLink shortLink = shortLinkService.createShortLink(originalUrl, user, clickLimit, lifeLimit);
                    System.out.printf("Ваша короткая ссылка: %s%n", shortLink.getURL());
                    break;
                case 2:
                    System.out.print("\nВведите код короткой ссылки (запись после https://clck.ru/): ");
                    String shortCode = scanner.nextLine();
                    shortLinkService.visitLink(shortCode);
                    break;
                case 3:
                    System.out.println("\nДо свидания!");
                    System.exit(0);
                    break;
            }
        }
    }
}