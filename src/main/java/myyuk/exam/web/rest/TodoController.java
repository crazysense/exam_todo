package myyuk.exam.web.rest;

import myyuk.exam.data.TodoEntry;
import myyuk.exam.web.ajax.TodoRequest;
import myyuk.exam.web.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for todo service.
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    /**
     * Gets all todo list.
     * @return All todo list.
     */
    @GetMapping(value = "/")
    public List<TodoEntry> getTodoList() {
        return todoService.getTodoList();
    }

    /**
     * Create new todo.
     * @param todo the new todo contents.
     * @return All todo list.
     */
    @PostMapping(value = "/")
    public List<TodoEntry> createTodo(@RequestBody TodoRequest todo) {
        todoService.createTodo(todo.getContent());
        return getTodoList();
    }

    /**
     * Update todo.
     * @param id the unique id of the todo that want to update.
     * @param todo the changed todo contents.
     * @return All todo list.
     */
    @PutMapping(value = "/{id}")
    public List<TodoEntry> updateTodo(@PathVariable Long id, @RequestBody TodoRequest todo) {
        todoService.updateTodo(id, todo.getContent(), todo.isComplete());
        return getTodoList();
    }
}
