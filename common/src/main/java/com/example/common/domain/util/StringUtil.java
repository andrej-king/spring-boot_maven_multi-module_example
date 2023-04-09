package com.example.common.domain.util;

import lombok.experimental.UtilityClass;

/**
 * Utils for with string type
 */
@UtilityClass
public class StringUtil {

    /**
     * Check if string contain only numbers
     */
    public boolean isValidNumber(String value) {
        return value.matches("\\d*");
    }

    /**
     * Check if string in not valid long
     */
    public boolean isInvalidLongType(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return true;
        }

        return false;
    }

    /**
     * Trim whitespaces and with lowercase
     */
    public String cleanString(String value, boolean toLowerCase) {
        String cleanedString = value.trim();

        return toLowerCase ? cleanedString.toLowerCase() : cleanedString;
    }

    /**
     * Trim whitespaces
     */
    public String cleanString(String value) {
        return cleanString(value, false);
    }
}
