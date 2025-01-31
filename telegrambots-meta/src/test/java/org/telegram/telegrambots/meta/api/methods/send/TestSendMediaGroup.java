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

        
    /**
     * Test that a TelegramApiValidationException is thrown when no media is provided to SendMediaGroup.
     * This ensures that the system does not accept a SendMediaGroup object without any media.
     */
    @Test
    void testEmptyMedia() {
        assertThrows(TelegramApiValidationException.class, () -> {
            // Build a SendMediaGroup object without any media (empty media list)
            SendMediaGroup sendMediaGroup = SendMediaGroup
                    .builder()
                    .chatId("12345") // Set chatId (required)
                    .build(); // No media provided in the builder
            
            // Validate the SendMediaGroup object. This should throw a TelegramApiValidationException
            // because the media list is empty (it doesn't contain any valid media).
            sendMediaGroup.validate(); // Should throw exception for empty media list
        });
    }

    @Test
    /**
     * Test that a TelegramApiValidationException is thrown when an invalid media type (InputMediaAnimation) is added to SendMediaGroup.
     * This ensures that the system rejects unsupported media types like animations.
     */
    void testInvalidMediaType() {
        assertThrows(TelegramApiValidationException.class, () -> {
            // Create a valid InputMediaAnimation object (which should not be allowed)
            InputMediaAnimation invalidMedia = new InputMediaAnimation(
            "fileId",                   // media identifier
            "caption",                  // caption for the media
            "HTML",                     // parseMode (optional)
            null,                       // captionEntities (optional, can be null)
            false,                      // isNewMedia (optional)
            "fileName",                 // mediaName (optional)
            null,                       // newMediaFile (optional)
            null,                       // newMediaStream (optional)
            320,                        // width (optional)
            240,                        // height (optional)
            10,                         // duration (optional)
            null,                       // thumbnail (optional)
            false                       // hasSpoiler (optional)
            );

            // Build the SendMediaGroup object and try to add the invalid media type
            SendMediaGroup sendMediaGroup = SendMediaGroup
                    .builder()
                    .chatId("12345") // Set chatId (required)
                    .media(invalidMedia) // Add invalid media type (InputMediaAnimation)
                    .build();

            // Validate the SendMediaGroup object. This should throw a TelegramApiValidationException
            // because InputMediaAnimation is not allowed in a SendMediaGroup.
            sendMediaGroup.validate(); // Should throw exception for invalid media type
        });
    }
}
