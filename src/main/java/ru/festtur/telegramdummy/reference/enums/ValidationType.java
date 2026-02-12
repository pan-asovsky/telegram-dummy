package ru.festtur.telegramdummy.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.festtur.telegramdummy.reference.dto.ValidationResult;

import java.util.regex.Pattern;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum ValidationType {

    FULL_NAME {
        @Override
        public ValidationResult validate(String v, String errMsg) {
            var parts = v.split(" ");
            if (parts.length < 2) {
                return ValidationResult.error(errMsg);
            }
            return ValidationResult.success();
        }
    },
    PHONE {
        @Override
        public ValidationResult validate(String v, String errMsg) {
            var phone = DIGITS_ONLY.matcher(v).replaceAll(EMPTY);
            log.info("parsed phone is {}", phone);
            if (phone.length() < 10) {
                log.error("cannot parse {} to phone number", v);
                return ValidationResult.error(errMsg);
            }
            return ValidationResult.success();
        }
    },
    DATE {
        @Override
        public ValidationResult validate(String v, String errMsg) {
            var m = DATE_REGEX.matcher(v);
            if (m.matches()) {
                return ValidationResult.success();
            }
            return ValidationResult.error("%s: %s".formatted(errMsg, v));
        }
    },
    PASSPORT {
        @Override
        public ValidationResult validate(String v, String errMsg) {
            var cleaned = DIGITS_AND_SPEC_CHARS.matcher(v).replaceAll(EMPTY);
            if (cleaned.length() != 10) {
                return ValidationResult.error(errMsg);
            }
            return ValidationResult.success();
        }
    },
    NONE {
        @Override
        public ValidationResult validate(String v, String errMsg) {
            return ValidationResult.success();
        }
    };

    private static final String EMPTY = "";
    private static final Pattern DATE_REGEX = Pattern.compile("(\\d{1,2})[-./ ](\\d{1,2})[-./ ](\\d{2,4})");
    private static final Pattern DIGITS_AND_SPEC_CHARS = Pattern.compile("[^0-9./-]");
    private static final Pattern DIGITS_ONLY = Pattern.compile("[^0-9+]");

    public abstract ValidationResult validate(String v, String errMsg);
}