package org.telegram.telegrambots.meta.api.methods.send;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static org.junit.jupiter.api.Assertions.*;

class TestSendLocation {
    private SendLocation sendLocation;

    @BeforeEach
    void setUp() {
        sendLocation = new SendLocation("12345", 37.7749, -122.4194);
    }

    @Test
    void testEnableNotification() {
        sendLocation.enableNotification();
        assertFalse(sendLocation.getDisableNotification(), "Notification should be enabled (false)");
    }

    @Test
    void testDisableNotification() {
        sendLocation.disableNotification();
        assertTrue(sendLocation.getDisableNotification(), "Notification should be disabled (true)");
    }

    @Test
    void testGetMethod() {
        assertEquals("sendlocation", sendLocation.getMethod(), "Method name should match API path");
    }

    @Test
    void testValidate_Success() {
        assertDoesNotThrow(() -> sendLocation.validate(), "Valid parameters should not throw an exception");
    }

    @Test
    void testValidate_ValidHorizontalAccuracy() {
        // Test the minimum and maximum valid values for horizontal accuracy
        sendLocation.setHorizontalAccuracy(0.0);
        assertDoesNotThrow(sendLocation::validate, "0.0 should be valid for horizontal accuracy");

        sendLocation.setHorizontalAccuracy(1500.0);
        assertDoesNotThrow(sendLocation::validate, "1500.0 should be valid for horizontal accuracy");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeHorizontalAccuracy() {
        // Test values outside the valid range for horizontal accuracy
        sendLocation.setHorizontalAccuracy(-1.0);
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Horizontal Accuracy parameter must be between 0 and 1500"));

        sendLocation.setHorizontalAccuracy(2000.0);
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Horizontal Accuracy parameter must be between 0 and 1500"));
    }

    @Test
    void testValidate_ValidHeading() {
        // Test the minimum and maximum valid values for heading
        sendLocation.setHeading(1);
        assertDoesNotThrow(sendLocation::validate, "1 should be valid for heading");

        sendLocation.setHeading(360);
        assertDoesNotThrow(sendLocation::validate, "360 should be valid for heading");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeHeading() {
        // Test values outside the valid range for heading
        sendLocation.setHeading(0);
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Heading Accuracy parameter must be between 1 and 360"));

        sendLocation.setHeading(400);
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Heading Accuracy parameter must be between 1 and 360"));
    }

    @Test
    void testValidate_ValidProximityAlertRadius() {
        // Test the minimum and maximum valid values for proximity alert radius
        sendLocation.setProximityAlertRadius(1);
        assertDoesNotThrow(sendLocation::validate, "1 should be valid for proximity alert radius");

        sendLocation.setProximityAlertRadius(100000);
        assertDoesNotThrow(sendLocation::validate, "100000 should be valid for proximity alert radius");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeProximityAlertRadius() {
        // Test values outside the valid range for proximity alert radius
        sendLocation.setProximityAlertRadius(0);
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Proximity alert radius parameter must be between 1 and 100000"));

        sendLocation.setProximityAlertRadius(200000);
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Proximity alert radius parameter must be between 1 and 100000"));
    }

    @Test
    void testValidate_ValidLivePeriod() {
        // Test the minimum, maximum, and special valid values for live period
        sendLocation.setLivePeriod(60);
        assertDoesNotThrow(sendLocation::validate, "60 should be valid for live period");

        sendLocation.setLivePeriod(86400);
        assertDoesNotThrow(sendLocation::validate, "86400 should be valid for live period");

        sendLocation.setLivePeriod(0x7FFFFFFF);
        assertDoesNotThrow(sendLocation::validate, "0x7FFFFFFF should be valid for live period");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeLivePeriod() {
        // Test values outside the valid range for live period
        sendLocation.setLivePeriod(30);
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Live period parameter must be between 60 and 86400 or be 0x7FFFFFFF"));

        sendLocation.setLivePeriod(90000);
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Live period parameter must be between 60 and 86400 or be 0x7FFFFFFF"));
    }

    @Test
    void testSetChatId_WithLong() {
        // Test setting chat ID as a Long value
        sendLocation.setChatId(987654321L);
        assertEquals("987654321", sendLocation.getChatId(), "ChatId should be converted to String");
    }

    @Test
    void testSetReplyParameters() {
        // Test setting reply parameters
        ReplyParameters replyParameters = new ReplyParameters();
        sendLocation.setReplyParameters(replyParameters);
        assertEquals(replyParameters, sendLocation.getReplyParameters(), "ReplyParameters should be set correctly");
    }

    @Test
    void testSetReplyMarkup() {
        // Test setting reply markup
        ReplyKeyboard replyKeyboard = new ReplyKeyboard() {};
        sendLocation.setReplyMarkup(replyKeyboard);
        assertEquals(replyKeyboard, sendLocation.getReplyMarkup(), "ReplyKeyboard should be set correctly");
    }

    @Test
    void testBuilder_Valid() {
        // Test building a SendLocation object using the builder pattern
        SendLocation sendLocationBuilt = SendLocation.builder()
                .chatId("12345")
                .latitude(37.7749)
                .longitude(-122.4194)
                .disableNotification(true)
                .build();

        assertNotNull(sendLocationBuilt);
        assertEquals("12345", sendLocationBuilt.getChatId());
        assertEquals(37.7749, sendLocationBuilt.getLatitude());
        assertEquals(-122.4194, sendLocationBuilt.getLongitude());
        assertTrue(sendLocationBuilt.getDisableNotification());
    }
}
