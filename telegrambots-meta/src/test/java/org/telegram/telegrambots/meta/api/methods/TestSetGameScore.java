package org.telegram.telegrambots.meta.api.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.games.SetGameScore;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.test.TelegramBotsHelper;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
class TestSetGameScore {

    private SetGameScore setGameScore;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        setGameScore = new SetGameScore(98765L, 12);
        setGameScore.setChatId(12345L);
        setGameScore.setDisableEditMessage(true);
        setGameScore.setMessageId(54321);
    }

    @Test
    void TestGetUpdatesMustBeSerializable() throws Exception {
        String json = mapper.writeValueAsString(setGameScore);
        assertNotNull(json);
        assertEquals("{\"chat_id\":\"12345\",\"message_id\":54321,\"disable_edit_message\":true,\"user_id\":98765,\"score\":12,\"method\":\"setGameScore\"}", json);
    }

    @Test
    void TestGetUpdatesMustDeserializeCorrectBooleanResponse() throws Exception {
        Serializable result =
                setGameScore.deserializeResponse(TelegramBotsHelper.GetSetGameScoreBooleanResponse());
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    void TestGetUpdatesMustDeserializeCorrectMessageResponse() throws Exception {
        Serializable result = setGameScore.deserializeResponse(TelegramBotsHelper.GetSetGameScoreMessageResponse());
        assertNotNull(result);
        assertTrue(result instanceof Message);
    }

    @Test
    void testSetGameScoreWithZeroScore() {
        SetGameScore zeroScore = new SetGameScore(98765L, 0);
        zeroScore.setChatId(12345L);
        assertNotNull(zeroScore);
        assertEquals(0, zeroScore.getScore());
    }

    @Test
    void testSetGameScoreWithNegativeScore() {
        SetGameScore negativeScore = new SetGameScore(98765L, -10);
        negativeScore.setChatId(12345L);
        assertNotNull(negativeScore);
        assertEquals(-10, negativeScore.getScore());
    }

    @Test
    void testSetGameScoreWithLargeScore() {
        SetGameScore largeScore = new SetGameScore(98765L, Integer.MAX_VALUE);
        largeScore.setChatId(12345L);
        assertNotNull(largeScore);
        assertEquals(Integer.MAX_VALUE, largeScore.getScore());
    }

    @Test
    void testSetGameScoreWithoutMessageId() {
        SetGameScore noMessageId = new SetGameScore(98765L, 100);
        noMessageId.setChatId(12345L);
        assertNotNull(noMessageId);
        assertNull(noMessageId.getMessageId());
    }
}
