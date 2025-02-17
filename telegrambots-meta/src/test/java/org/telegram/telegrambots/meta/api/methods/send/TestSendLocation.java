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
        sendLocation.setHorizontalAccuracy(0.0); // 最小允許值
        assertDoesNotThrow(sendLocation::validate, "0.0 should be valid for horizontal accuracy");

        sendLocation.setHorizontalAccuracy(1500.0); // 最大允許值
        assertDoesNotThrow(sendLocation::validate, "1500.0 should be valid for horizontal accuracy");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeHorizontalAccuracy() {
        sendLocation.setHorizontalAccuracy(-1.0); // 低於範圍（不合法）
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Horizontal Accuracy parameter must be between 0 and 1500"));

        sendLocation.setHorizontalAccuracy(2000.0); // 超過範圍（不合法）
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Horizontal Accuracy parameter must be between 0 and 1500"));
    }


    @Test
    void testValidate_ValidHeading() {
        sendLocation.setHeading(1); // 最小允許值
        assertDoesNotThrow(sendLocation::validate, "1 should be valid for heading");

        sendLocation.setHeading(360); // 最大允許值
        assertDoesNotThrow(sendLocation::validate, "360 should be valid for heading");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeHeading() {
        sendLocation.setHeading(0); // 低於範圍（不合法）
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Heading Accuracy parameter must be between 1 and 360"));

        sendLocation.setHeading(400); // 超過範圍（不合法）
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Heading Accuracy parameter must be between 1 and 360"));
    }

    @Test
    void testValidate_ValidProximityAlertRadius() {
        sendLocation.setProximityAlertRadius(1); // 最小允許值
        assertDoesNotThrow(sendLocation::validate, "1 should be valid for proximity alert radius");

        sendLocation.setProximityAlertRadius(100000); // 最大允許值
        assertDoesNotThrow(sendLocation::validate, "100000 should be valid for proximity alert radius");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeProximityAlertRadius() {
        sendLocation.setProximityAlertRadius(0); // 低於範圍（不合法）
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Proximity alert radius parameter must be between 1 and 100000"));

        sendLocation.setProximityAlertRadius(200000); // 超過範圍（不合法）
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Proximity alert radius parameter must be between 1 and 100000"));
    }

    @Test
    void testValidate_ValidLivePeriod() {
        sendLocation.setLivePeriod(60); // 最小允許值
        assertDoesNotThrow(sendLocation::validate, "60 should be valid for live period");

        sendLocation.setLivePeriod(86400); // 最大允許值
        assertDoesNotThrow(sendLocation::validate, "86400 should be valid for live period");

        sendLocation.setLivePeriod(0x7FFFFFFF); // 特殊允許值
        assertDoesNotThrow(sendLocation::validate, "0x7FFFFFFF should be valid for live period");
    }

    @Test
    void testValidate_ThrowsExceptionForOutOfRangeLivePeriod() {
        sendLocation.setLivePeriod(30); // 低於範圍（不合法）
        Exception exceptionLow = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionLow.getMessage().contains("Live period parameter must be between 60 and 86400 or be 0x7FFFFFFF"));

        sendLocation.setLivePeriod(90000); // 超過範圍（不合法）
        Exception exceptionHigh = assertThrows(TelegramApiValidationException.class, sendLocation::validate);
        assertTrue(exceptionHigh.getMessage().contains("Live period parameter must be between 60 and 86400 or be 0x7FFFFFFF"));
    }

    @Test
    void testSetChatId_WithLong() {
        sendLocation.setChatId(987654321L);
        assertEquals("987654321", sendLocation.getChatId(), "ChatId should be converted to String");
    }

    @Test
    void testSetReplyParameters() {
        ReplyParameters replyParameters = new ReplyParameters();
        sendLocation.setReplyParameters(replyParameters);
        assertEquals(replyParameters, sendLocation.getReplyParameters(), "ReplyParameters should be set correctly");
    }

    @Test
    void testSetReplyMarkup() {
        ReplyKeyboard replyKeyboard = new ReplyKeyboard() {}; // 假設有個 ReplyKeyboard 介面
        sendLocation.setReplyMarkup(replyKeyboard);
        assertEquals(replyKeyboard, sendLocation.getReplyMarkup(), "ReplyKeyboard should be set correctly");
    }

    @Test
    void testBuilder_Valid() {
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
