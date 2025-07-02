package se.lexicon.todo_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.lexicon.todo_api.dto.TodoDto;
import se.lexicon.todo_api.service.TodoService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TodoDto todoDto;
    private final Long TEST_TODO_ID = 1L;
    private final Long TEST_PERSON_ID = 1L;

    @BeforeEach
    void setUp() {
        todoDto = TodoDto.builder()
                .id(TEST_TODO_ID)
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .dueDate(LocalDateTime.now().plusDays(1))
                .personId(TEST_PERSON_ID)
                .build();
    }

    @Test
    void getAllTodos_ShouldReturnListOfTodos() throws Exception {
        List<TodoDto> todos = Arrays.asList(todoDto);
        when(todoService.findAll()).thenReturn(todos);

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_TODO_ID));
    }

    @Test
    void getTodoById_ShouldReturnTodo() throws Exception {
        when(todoService.findById(TEST_TODO_ID)).thenReturn(todoDto);

        mockMvc.perform(get("/api/todo/{id}", TEST_TODO_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TEST_TODO_ID));
    }

    @Test
    void createTodo_ShouldReturnCreatedTodo() throws Exception {
        when(todoService.create(any(TodoDto.class))).thenReturn(todoDto);

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TEST_TODO_ID));
    }

    @Test
    void updateTodo_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/api/todo/{id}", TEST_TODO_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTodo_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", TEST_TODO_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTodosByPerson_ShouldReturnListOfTodos() throws Exception {
        List<TodoDto> todos = Arrays.asList(todoDto);
        when(todoService.findByPersonId(TEST_PERSON_ID)).thenReturn(todos);

        mockMvc.perform(get("/api/todo/person/{personId}", TEST_PERSON_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_TODO_ID));
    }

    @Test
    void getTodosByStatus_ShouldReturnListOfTodos() throws Exception {
        List<TodoDto> todos = Arrays.asList(todoDto);
        when(todoService.findByCompleted(false)).thenReturn(todos);

        mockMvc.perform(get("/api/todo/status")
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_TODO_ID));
    }

    @Test
    void getOverdueTodos_ShouldReturnListOfTodos() throws Exception {
        List<TodoDto> todos = Arrays.asList(todoDto);
        when(todoService.findOverdueTodos()).thenReturn(todos);

        mockMvc.perform(get("/api/todo/overdue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(TEST_TODO_ID));
    }
}