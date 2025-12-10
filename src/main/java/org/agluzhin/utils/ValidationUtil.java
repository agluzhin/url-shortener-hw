package org.agluzhin.utils;

public class ValidationUtil {
    public static void requireNonNullOrEmpty(String entityFieldName, String entityFieldValue)
            throws IllegalArgumentException {
        if (entityFieldValue == null || entityFieldValue.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format(
                            "поле '%s' не может быть равным 'null' или быть 'пустым'",
                            entityFieldName
                    )
            );
        }
    }

    public static void requireNonNull(String entityName, Object entity)
            throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException(
                    String.format(
                            "объект '%s' не может быть равным 'null'",
                            entityName
                    )
            );
        }
    }

    public static void requirePositiveAndNonZeroInt(String entityFieldName, int entityFieldValue) {
        if (entityFieldValue <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "объект '%s' не может быть меньше нуля или равным нулю",
                            entityFieldName
                    )
            );
        }
    }

    public static void requirePositiveAndNonZeroLong(String entityFieldName, long entityFieldValue) {
        if (entityFieldValue <= 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "объект '%s' не может быть меньше нуля или равным нулю",
                            entityFieldName
                    )
            );
        }
    }
}
