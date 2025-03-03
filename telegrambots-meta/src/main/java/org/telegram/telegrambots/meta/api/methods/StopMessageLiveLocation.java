package org.telegram.telegrambots.meta.api.methods;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Use this method to stop updating a live location message sent by the bot or via the bot (for inline bots)
 * before live_period expires. On success, if the message was sent by the bot, the sent Message is returned,
 * otherwise True is returned.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StopMessageLiveLocation extends BotApiMethodSerializable {
    public static final String PATH = "stopMessageLiveLocation";

    private static final String CHAT_ID_FIELD = "chat_id";
    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String INLINE_MESSAGE_ID_FIELD = "inline_message_id";
    private static final String REPLY_MARKUP_FIELD = "reply_markup";
    private static final String BUSINESS_CONNECTION_ID_FIELD = "business_connection_id";

    // Default ObjectMapper instance
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    
    // Injectable ObjectMapper instance
    private ObjectMapper objectMapper;

    /**
     * Required if inline_message_id is not specified. Unique identifier for the chat to send the
     * message to (Or username for channels)
     */
    @JsonProperty(CHAT_ID_FIELD)
    private String chatId;
    /**
     * Required if inline_message_id is not specified. Unique identifier of the sent message
     */
    @JsonProperty(MESSAGE_ID_FIELD)
    private Integer messageId;
    /**
     * Required if chat_id and message_id are not specified. Identifier of the inline message
     */
    @JsonProperty(INLINE_MESSAGE_ID_FIELD)
    private String inlineMessageId;
    /**
     * Optional.
     * A JSON-serialized object for an inline keyboard.
     */
    @JsonProperty(REPLY_MARKUP_FIELD)
    private InlineKeyboardMarkup replyMarkup;
    /**
     * Optional
     * Unique identifier of the business connection on behalf of which the message to be edited was sent
     */
    @JsonProperty(BUSINESS_CONNECTION_ID_FIELD)
    private String businessConnectionId;

    @Tolerate
    public void setChatId(Long chatId) {
        this.chatId = chatId.toString();
    }

    /**
     * Get the ObjectMapper for serialization/deserialization
     * Returns the default instance if none is set
     */
    private ObjectMapper getObjectMapper() {
        return objectMapper != null ? objectMapper : DEFAULT_MAPPER;
    }

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public Serializable deserializeResponse(String answer) throws TelegramApiRequestException {
        ObjectMapper mapper = getObjectMapper();
        
        try {
            ApiResponse<Boolean> result = mapper.readValue(answer,
                    new TypeReference<ApiResponse<Boolean>>(){});
            if (result.getOk()) {
                return result.getResult();
            } else {
                throw new TelegramApiRequestException("Error stopping message live location", result);
            }
        } catch (IOException e) {
            try {
                ApiResponse<Message> result = mapper.readValue(answer,
                        new TypeReference<ApiResponse<Message>>(){});
                if (result.getOk()) {
                    return result.getResult();
                } else {
                    throw new TelegramApiRequestException("Error stopping message live location", result);
                }
            } catch (IOException e2) {
                throw new TelegramApiRequestException("Unable to deserialize response", e);
            }
        }
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (inlineMessageId == null) {
            if (chatId == null || chatId.isEmpty()) {
                throw new TelegramApiValidationException("ChatId parameter can't be empty if inlineMessageId is not present", this);
            }
            if (messageId == null) {
                throw new TelegramApiValidationException("MessageId parameter can't be empty if inlineMessageId is not present", this);
            }
        } else {
            if (chatId != null) {
                throw new TelegramApiValidationException("ChatId parameter must be empty if inlineMessageId is provided", this);
            }
            if (messageId != null) {
                throw new TelegramApiValidationException("MessageId parameter must be empty if inlineMessageId is provided", this);
            }
        }
        if (replyMarkup != null) {
            replyMarkup.validate();
        }
    }

    public static class StopMessageLiveLocationBuilder {
        private ObjectMapper objectMapper;
        
        public StopMessageLiveLocationBuilder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }
        
        @Tolerate
        public StopMessageLiveLocationBuilder chatId(Long chatId) {
            this.chatId = chatId.toString();
            return this;
        }
    }
}
