package org.telegram.telegrambots.meta.api.methods.send;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAnimation;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;


public class TestSendMediaGroup {
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void test() {
        try {
            String expectedJson = "{\"chatId\":\"12345\",\"medias\":[{\"media\":\"attach://321.png\",\"caption_entities\":[],\"type\":\"photo\"}," +
                    "{\"media\":\"attach://123.png\",\"caption_entities\":[],\"type\":\"photo\"}]," +
                    "\"method\":\"sendMediaGroup\"}";
            InputStream is = new ByteArrayInputStream("RandomFileContent".getBytes());
            InputStream is2 = new ByteArrayInputStream("RandomFileContent2".getBytes());

            SendMediaGroup sendMediaGroup = SendMediaGroup
                    .builder()
                    .chatId("12345")
                    .media(InputMediaPhoto.builder().media(is, "321.png").build())
                    .media(InputMediaPhoto.builder().media(is2, "123.png").build())
                    .build();

            String json = mapper.writeValueAsString(sendMediaGroup);
            assertEquals(expectedJson, json);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testEmptyMediaList() {
        assertThrows(TelegramApiValidationException.class, () -> {
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId("12345")
                    .build();
            sendMediaGroup.validate();
        }, "Expected validation to fail for empty media list");
    }

    @Test
    public void testLessThanTwoMediaItems() {
        InputStream is = new ByteArrayInputStream("RandomFileContent".getBytes());

        assertThrows(TelegramApiValidationException.class, () -> {
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId("12345")
                    .media(InputMediaPhoto.builder().media(is, "321.png").build())
                    .build();
            sendMediaGroup.validate();
        }, "Expected validation to fail for less than two media items");
    }

    @Test
    public void testMoreThanTenMediaItems() {
        List<InputMedia> mediaList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            InputStream is = new ByteArrayInputStream(("RandomFileContent" + i).getBytes());
            mediaList.add(InputMediaPhoto.builder().media(is, "321" + i + ".png").build());
        }

        assertThrows(TelegramApiValidationException.class, () -> {
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId("12345")
                    .medias(mediaList)
                    .build();
            sendMediaGroup.validate();
        }, "Expected validation to fail for more than ten media items");
    }

    @Test
    public void testValidMediaList() {
        try {
            List<InputMedia> mediaList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                InputStream is = new ByteArrayInputStream(("RandomFileContent" + i).getBytes());
                mediaList.add(InputMediaPhoto.builder().media(is, "321" + i + ".png").build());
            }

            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId("12345")
                    .medias(mediaList)
                    .build();
            sendMediaGroup.validate();

        } catch (TelegramApiValidationException e) {
            fail("Validation should have passed for a valid media list");
        }
    }
}
