package se.lexicon.todo_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_api.dto.TodoDto;
import se.lexicon.todo_api.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@Validated
@Tag(name = "Todo API", description = "API endpoints for managing todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "Get all todos", description = "Retrieves a list of all todo items")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved todo list")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TodoDto> getAllTodos() {
        return todoService.findAll();
    }

    @Operation(summary = "Get todo by ID", description = "Retrieves a specific todo item by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved todo"),
            @ApiResponse(responseCode = "404", description = "Todo not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TodoDto getTodoById(
            @PathVariable("id")
            @NotNull(message = "Id cannot be null")
            @Positive(message = "Id must be positive")
            Long id) {
        return todoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto createTodo(
            @RequestBody @Valid TodoDto todoDto) {
        return todoService.create(todoDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTodo(
            @PathVariable @NotNull(message = "Id cannot be null") Long id,
            @RequestBody @Valid TodoDto todoDto) {
        todoService.update(id, todoDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(
            @PathVariable @NotNull(message = "Id cannot be null") Long id) {
        todoService.delete(id);
    }

    @GetMapping("/person/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoDto> getTodosByPerson(
            @PathVariable @NotNull(message = "Person id cannot be null") Long personId) {
        return todoService.findByPersonId(personId);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoDto> getTodosByStatus(
            @RequestParam(required = true) boolean completed) {
        return todoService.findByCompleted(completed);
    }

    @GetMapping("/overdue")
    @ResponseStatus(HttpStatus.OK)
    public List<TodoDto> getOverdueTodos() {
        return todoService.findOverdueTodos();
    }
}