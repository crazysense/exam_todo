package myyuk.exam.web.service;

import myyuk.exam.data.TodoEntry;

import java.util.List;

/**
 * Todo Service
 */
public interface TodoService {
    /**
     * Create new todo.
     * @param content the content for todo.
     */
    void createTodo(String content);

    /**
     * Update todo.
     * @param id the unique id of the todo that want to modify.
     * @param content the modified content.
     * @param complete the modified complete status.
     */
    void updateTodo(long id, String content, boolean complete);

    /**
     * Get all todo list.
     * @return All todo list.
     */
    List<TodoEntry> getTodoList();
}
