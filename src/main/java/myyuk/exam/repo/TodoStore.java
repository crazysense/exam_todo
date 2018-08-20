package myyuk.exam.repo;

import myyuk.exam.data.TodoEntry;

import java.util.List;

/**
 * Todo Store
 * - CRUD data from the data store for Todo.
 * @see TodoEntry
 */
public interface TodoStore {
    /**
     * Gets todo item by id.
     * @param id the unique id.
     * @return the todo item.
     */
    TodoEntry getTodoById(long id);

    /**
     * Gets all todo list.
     * @return the todo item list.
     */
    List<TodoEntry> getAllTodos();

    /**
     * Insert todo item to the data store.
     * @param todo the todo item.
     */
    void insert(TodoEntry todo);

    /**
     * Update todo item to the data store.
     * @param todo the todo item.
     */
    void update(TodoEntry todo);

    /**
     * Delete todo item from the data store.
     * @param id the unique id.
     */
    void delete(long id);
}
