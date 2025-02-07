package org.telegram.telegrambots.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MultipartBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.stickers.InputSticker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestTelegramMultipartBuilder {
    private TelegramMultipartBuilder multipartBuilder;

    @BeforeEach
    public void setUp() {
        multipartBuilder = new TelegramMultipartBuilder(new ObjectMapper());
    }

    @Test
    public void testAddStringPart() {
        MultipartBody result = multipartBuilder.addPart("TestPart", "TestValue").build();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddObjectPart() {
        MultipartBody result = multipartBuilder.addPart("TestPart", 10000).build();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAddJsonPart() {
        try {
            Location location = new Location(1000d, 1000d);
            MultipartBody result = multipartBuilder.addJsonPart("TestPart", location).build();
            assertNotNull(result);
            assertEquals(1, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputFileWithoutField() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputFile inputFile = new InputFile(file, "test_file.txt");
            MultipartBody result = multipartBuilder.addInputFile("testField", inputFile, false).build();
            assertNotNull(result);
            assertEquals(1, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputFileAsStreamWithoutField() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputStream fileStream = Files.newInputStream(file.toPath());
            InputFile inputFile = new InputFile(fileStream, "test_file.txt");
            MultipartBody result = multipartBuilder.addInputFile("testField", inputFile, false).build();
            assertNotNull(result);
            assertEquals(1, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputFileWithField() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputFile inputFile = new InputFile(file, "test_file.txt");
            MultipartBody result = multipartBuilder.addInputFile("testField", inputFile, true).build();
            assertNotNull(result);
            assertEquals(2, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputMedia() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputMediaDocument inputMedia = new InputMediaDocument(file, "test_file.txt");
            MultipartBody result = multipartBuilder.addMedia(inputMedia).build();
            assertNotNull(result);
            assertEquals(1, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputMediaAsStream() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputStream fileStream = Files.newInputStream(file.toPath());
            InputMediaDocument inputMedia = new InputMediaDocument(fileStream, "test_file.txt");
            MultipartBody result = multipartBuilder.addMedia(inputMedia).build();
            assertNotNull(result);
            assertEquals(1, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddStickerSet() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("test_file.txt").getFile());
            InputFile inputFile = new InputFile(file, "test_file.txt");
            InputSticker inputSticker = InputSticker.builder()
                    .sticker(inputFile)
                    .emoji("A")
                    .format("static")
                    .build();
            MultipartBody result = multipartBuilder.addInputStickers("testField", List.of(inputSticker)).build();
            assertNotNull(result);
            assertEquals(2, result.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void testAddInputFileWithFiniteStateMachine() throws IOException {
        // 準備測試數據
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test_file.txt").getFile());
        InputFile inputFile = new InputFile(file, "test_file.txt");

        // 測試狀態機
        // 狀態定義
        enum State {
            INITIAL,
            FILE_NULL,
            FILE_NEW,
            FILE_EXISTING,
            ADD_FIELD
        }

        State state = State.INITIAL;

        // 狀態轉換
        while (true) {
            switch (state) {
                case INITIAL:
                    if (inputFile == null) {
                        state = State.FILE_NULL;
                    } else {
                        // 假設 inputFile 有一個方法來檢查是否為新文件
                        // 如果沒有，您可能需要根據實際情況進行調整
                        state = inputFile.getNewMediaFile() != null ? State.FILE_NEW : State.FILE_EXISTING;
                    }
                    break;

                case FILE_NULL:
                    // 檢查當文件為 null 時的行為
                    TelegramMultipartBuilder resultNull = multipartBuilder.addInputFile("testField", null, true);
                    assertNotNull(resultNull);
                    return; // 結束測試

                case FILE_NEW:
                    // 檢查當文件為新文件時的行為
                    TelegramMultipartBuilder resultNew = multipartBuilder.addInputFile("testField", inputFile, true);
                    assertNotNull(resultNew);
                    // 這裡可以添加更多的斷言來檢查結果
                    state = State.ADD_FIELD; // 轉換到添加字段狀態
                    break;

                case FILE_EXISTING:
                    // 檢查當文件已存在時的行為
                    TelegramMultipartBuilder resultExisting = multipartBuilder.addInputFile("testField", inputFile, false);
                    assertNotNull(resultExisting);
                    state = State.ADD_FIELD; // 轉換到添加字段狀態
                    break;

                case ADD_FIELD:
                    // 檢查添加字段的行為
                    TelegramMultipartBuilder finalResult = multipartBuilder.addInputFile("testField", inputFile, true);
                    assertNotNull(finalResult);
                    // 這裡可以添加更多的斷言來檢查結果
                    return; // 完成，返回
            }
        }
    }
}
