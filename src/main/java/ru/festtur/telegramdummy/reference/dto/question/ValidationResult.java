package ru.festtur.telegramdummy.reference.dto.question;

public record ValidationResult(boolean isValid, String errorMessage) {

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(false, message);
    }

}