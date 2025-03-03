package org.telegram.telegrambots.meta.api.methods;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StopMessageLiveLocation class
 */
public class TestStopMessageLiveLocation {

    @Test
    public void testDefaultObjectMapper() throws Exception {
        // Create an instance using the default ObjectMapper
        StopMessageLiveLocation stopLocation = new StopMessageLiveLocation();
        stopLocation.setChatId("123456789");
        stopLocation.setMessageId(42);
        
        // Verify method name is correct
        assertEquals("stopMessageLiveLocation", stopLocation.getMethod());
        
        // Create a valid API response
        String validResponse = "{\"ok\":true,\"result\":true}";
        
        // Call the method and verify the result
        Serializable result = stopLocation.deserializeResponse(validResponse);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testValidation() {
        // Test validation with no parameters
        StopMessageLiveLocation emptyLocation = new StopMessageLiveLocation();
        
        TelegramApiValidationException thrown = assertThrows(
            TelegramApiValidationException.class,
            () -> emptyLocation.validate()
        );
        
        assertTrue(thrown.getMessage().contains("ChatId parameter can't be empty"));
        
        // Test validation with only chatId
        StopMessageLiveLocation chatIdOnly = new StopMessageLiveLocation();
        chatIdOnly.setChatId("123456789");
        
        thrown = assertThrows(
            TelegramApiValidationException.class,
            () -> chatIdOnly.validate()
        );
        
        assertTrue(thrown.getMessage().contains("MessageId parameter can't be empty"));
        
        // Test validation with inlineMessageId
        StopMessageLiveLocation inlineLocation = new StopMessageLiveLocation();
        inlineLocation.setInlineMessageId("inline123456789");
        
        // Should not throw exception
        assertDoesNotThrow(() -> inlineLocation.validate());
    }
    
    @Test
    public void testBuilderWithLongChatId() {
        // Test builder with Long chatId
        StopMessageLiveLocation location = StopMessageLiveLocation.builder()
            .chatId(123456789L)
            .messageId(42)
            .build();
        
        assertEquals("123456789", location.getChatId());
        assertEquals(Integer.valueOf(42), location.getMessageId());
    }
    
    @Test
    public void testBusinessConnectionId() {
        // Test with business connection ID
        StopMessageLiveLocation location = StopMessageLiveLocation.builder()
            .chatId("123456789")
            .messageId(42)
            .businessConnectionId("business123")
            .build();
        
        assertEquals("business123", location.getBusinessConnectionId());
    }
}