package org.telegram.telegrambots.meta.api.methods.groupadministration;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.meta.util.Validations;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ruben Bermudez
 * @version 5.5
 * Extended test class with mutation testing considerations
 */
public class TestBanChatSenderChat {

    @Test
    public void testBanChatSenderChatWithAllSet() {
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .untilDate(1651234567)
                .build();
        assertEquals("banChatSenderChat", banChatSenderChat.getMethod());
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testBanChatSenderChatWithEmptyChatId() {
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("")
                .senderChatId(987654321L)
                .untilDate(1651234567)
                .build();
        assertEquals("banChatSenderChat", banChatSenderChat.getMethod());
        Throwable thrown = assertThrows(TelegramApiValidationException.class, banChatSenderChat::validate);
        assertEquals(Validations.CHAT_ID_VALIDATION, thrown.getMessage());
    }

    @Test
    public void testBanChatSenderChatWithInvalidSenderChatId() {
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(0L)
                .untilDate(1651234567)
                .build();
        assertEquals("banChatSenderChat", banChatSenderChat.getMethod());
        Throwable thrown = assertThrows(TelegramApiValidationException.class, banChatSenderChat::validate);
        assertEquals("SenderChatId can't be null or 0", thrown.getMessage());
    }

    @Test
    public void testBanChatSenderChatWithEmptyUntilDate() {
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .untilDate(0)
                .build();
        assertEquals("banChatSenderChat", banChatSenderChat.getMethod());
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testSetUntilDateInstant() {
        Instant now = Instant.now();
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .build();
        banChatSenderChat.setUntilDateInstant(now);
        assertEquals((int)now.getEpochSecond(), banChatSenderChat.getUntilDate().intValue());
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testSetUntilDateDateTime() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .build();
        banChatSenderChat.setUntilDateDateTime(now);
        assertEquals((int)now.toInstant().getEpochSecond(), banChatSenderChat.getUntilDate().intValue());
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testForTimePeriodDuration() {
        Duration duration = Duration.ofDays(1);
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .build();
        banChatSenderChat.forTimePeriodDuration(duration);
        int expectedTime = (int)Instant.now().plusMillis(duration.toMillis()).getEpochSecond();
        int actualTime = banChatSenderChat.getUntilDate();

        assertTrue(Math.abs(expectedTime - actualTime) < 5);
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testUntilDateBoundaryTooLong() {
        int over366Days = (int)(Instant.now().getEpochSecond() + 367 * 24 * 60 * 60);
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .untilDate(over366Days)
                .build();
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testUntilDateBoundaryTooShort() {
        int under30Seconds = (int)(Instant.now().getEpochSecond() + 29);
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId("123456789")
                .senderChatId(987654321L)
                .untilDate(under30Seconds)
                .build();
        assertDoesNotThrow(banChatSenderChat::validate);
    }

    @Test
    public void testLongChatIdSetter() {
        BanChatSenderChat banChatSenderChat = BanChatSenderChat
                .builder()
                .chatId(123456789L)
                .senderChatId(987654321L)
                .build();
        assertEquals("123456789", banChatSenderChat.getChatId());
        assertDoesNotThrow(banChatSenderChat::validate);
    }
}
