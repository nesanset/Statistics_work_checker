package statisticschecker.domain.validation;

import java.math.BigDecimal;
import java.util.List;

public final class DomainValidation {

    private DomainValidation() {
    }

    public static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    public static String trimNullableText(String value) {
        if (value == null) {
            return null;
        }
        String trimmedValue = value.trim();
        if (trimmedValue.isBlank()) {
            return null;
        }
        return trimmedValue;
    }

    public static int requirePositive(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static BigDecimal requirePositive(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value.stripTrailingZeros();
    }

    public static <T> List<T> requireNotEmptyList(List<T> values, String message) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return List.copyOf(values);
    }

    public static <T> List<T> copyNullableList(List<T> values) {
        if (values == null) {
            return List.of();
        }
        return List.copyOf(values);
    }
}