package com.example.notepad;

import com.example.notepad.dto.NoteDto;
import com.example.notepad.entity.NoteEntity;
import com.example.notepad.repository.NoteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class NoteControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    private static ObjectMapper objectMapper;

    private final static String URL = "/api/note";
    private final static String FIRST_NOTE_CONTENT = "First note";
    private final static String SECOND_NOTE_CONTENT = "Second note";
    private final static String THIRD_NOTE_CONTENT = "Third note";
    private final static String CREATED_NOTE_CONTENT = "Created note";
    private final static String UPDATED_NOTE_CONTENT = "Updated note";

    private static NoteEntity firstNote;
    private static NoteEntity secondNote;
    private static NoteEntity thirdNote;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();

        firstNote = new NoteEntity();
        firstNote.setContent(FIRST_NOTE_CONTENT);

        secondNote = new NoteEntity();
        secondNote.setContent(SECOND_NOTE_CONTENT);

        thirdNote = new NoteEntity();
        thirdNote.setContent(THIRD_NOTE_CONTENT);
    }

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
    }

    @Test
    void givenRepository_whenList_thenListSizeAndContentEqualExpected() throws Exception {
        //given
        List<NoteEntity> entities = List.of(firstNote, secondNote, thirdNote);
        noteRepository.saveAll(entities);

        //when
        String responseJson = mockMvc
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<NoteEntity> notes = objectMapper.readValue(responseJson, new TypeReference<>() {
        });

        //then
        assertAll(
                () -> assertNotNull(notes, "List is null"),
                () -> assertEquals(3, notes.size(), "List size not equal"),
                () -> assertEquals(firstNote, entities.get(0), "First element not equal"),
                () -> assertEquals(secondNote, entities.get(1), "Second element not equal"),
                () -> assertEquals(thirdNote, entities.get(2), "Third element not equal")
        );
    }

    @Nested
    public class CreateNoteTest {
        @Test
        void givenNote_whenCreate_thenNoteExists() throws Exception {
            //given
            NoteDto noteDto = new NoteDto();
            noteDto.setContent(CREATED_NOTE_CONTENT);

            String noteAsJson = objectMapper.writeValueAsString(noteDto);

            //when
            String responseJson = mockMvc
                    .perform(post(URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(noteAsJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();


            NoteEntity createdNote = objectMapper.readValue(responseJson, NoteEntity.class);

            //then
            assertAll(
                    () -> assertEquals(createdNote.getContent(), noteDto.getContent(), "Note content is not the same"),
                    () -> assertNotNull(createdNote.getId(), "Note id equals null")
            );
        }

        @Test
        void givenInvalidNoteContent_whenCreate_thenThrowException() throws Exception {
            //given
            String invalidContent = "";

            for(int i = 0; i <= 200; i++) {
                invalidContent += " ";
            }

            NoteDto noteToCreate = new NoteDto();
            noteToCreate.setContent(invalidContent);

            String noteAsJson = objectMapper.writeValueAsString(noteToCreate);

            //when, then
            mockMvc.perform(post(URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(noteAsJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class UpdateNoteTest {
        @Test
        void givenNoteDto_whenUpdate_thenContentEquals() throws Exception {
            //given
            NoteDto noteToUpdate = new NoteDto();
            noteToUpdate.setContent(UPDATED_NOTE_CONTENT);
            firstNote = noteRepository.save(firstNote);

            String noteAsJson = objectMapper.writeValueAsString(noteToUpdate);

            //when
            String responseJson = mockMvc
                    .perform(put(URL + "/" + firstNote.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(noteAsJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            NoteEntity updatedNote = objectMapper.readValue(responseJson, NoteEntity.class);

            //then
            assertAll(
                    () -> assertEquals(noteToUpdate.getContent(), updatedNote.getContent(), "Note content are not equal"),
                    () -> assertEquals(firstNote.getId(), updatedNote.getId(), "Notes id are not equal")
            );
        }

        @Test
        void givenInvalidId_whenUpdate_thenThrowException() throws Exception {
            //given
            NoteDto noteToUpdate = new NoteDto();
            noteToUpdate.setContent(UPDATED_NOTE_CONTENT);
            firstNote = noteRepository.save(firstNote);
            Long invalidId = 999l;

            String noteAsJson = objectMapper.writeValueAsString(noteToUpdate);

            //when, then
            mockMvc.perform(put(URL + "/" + invalidId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(noteAsJson))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void givenInvalidNoteContent_whenUpdate_thenThrowException() throws Exception {
            //given
            String invalidContent = "";

            for(int i = 0; i <= 200; i++) {
                invalidContent += " ";
            }

            NoteDto noteToUpdate = new NoteDto();
            noteToUpdate.setContent(invalidContent);

            String noteAsJson = objectMapper.writeValueAsString(noteToUpdate);

            //when, then
            mockMvc.perform(put(URL + "/" + firstNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(noteAsJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class DeleteNoteTest {

        @Test
        void givenNoteIdAndRepository_whenDelete_thenNoteDeleted() throws Exception {
            //given
            firstNote = noteRepository.save(firstNote);

            //when
            mockMvc.perform(delete(URL + "/" + firstNote.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            //then
            assertFalse(noteRepository.existsById(firstNote.getId()));
        }

        @Test
        void givenInvalidId_whenDelete_thenThrowException() throws Exception {
            //given
            Long invalidId = 999l;

            //when
            mockMvc.perform(delete(URL + "/" + firstNote.getId()))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }
}
