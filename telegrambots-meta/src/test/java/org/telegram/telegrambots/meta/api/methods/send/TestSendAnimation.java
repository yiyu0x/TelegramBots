package org.telegram.telegrambots.meta.api.methods.send;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the SendAnimation class
 */
public class TestSendAnimation {

    @Test
    public void testBasicSendAnimation() {
        // 使用建造者創建基本的 SendAnimation 對象
        SendAnimation sendAnimation = SendAnimation.builder()
                .chatId("123456789")
                .animation(new InputFile("animation-file-id"))
                .build();
        
        // 驗證方法名稱
        assertEquals("sendAnimation", sendAnimation.getMethod());
    }
    
    @Test
    public void testDeserializeResponse() throws TelegramApiRequestException, IOException {
        // 創建 mock 對象
        ObjectMapper mockMapper = mock(ObjectMapper.class);
        ApiResponse<Message> mockResponse = new ApiResponse<>();
        Message mockMessage = mock(Message.class);
        
        // 設置 mock 行為
        when(mockMessage.getMessageId()).thenReturn(12345);
        
        // 使用 builder 創建 SendAnimation 實例
        SendAnimation sendAnimation = SendAnimation.builder()
                .chatId("123456789")
                .animation(new InputFile("animation-file-id"))
                .build();
        
        // 設置 mockMapper 的行為，使其返回 mockResponse
        when(mockMapper.readValue(anyString(), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(mockResponse);
        
        // 模擬 ApiResponse 的行為
        // mockResponse.setResult(mockMessage); // 移除這一行
        
        // 測試 deserializeResponse 方法
        Message result = sendAnimation.deserializeResponse("{\"ok\":true,\"result\":{\"message_id\":12345}}");
        
        // 驗證結果
        assertNotNull(result);
        assertEquals(12345, result.getMessageId());
    }
}
