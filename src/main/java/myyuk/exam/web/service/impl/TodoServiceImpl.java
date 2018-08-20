package myyuk.exam.web.service.impl;

import com.google.common.collect.Lists;
import myyuk.exam.data.TodoEntry;
import myyuk.exam.repo.TodoStore;
import myyuk.exam.web.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Todo Service Implementation.
 */
@Service
public class TodoServiceImpl implements TodoService {
    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Autowired
    private TodoStore todoStore;

    @Override
    public void createTodo(String content) {
        try {
            TodoEntry todo = new TodoEntry();
            todo.setContent(content);
            todo.setComplete(false);

            long currentTime = Instant.now().toEpochMilli();
            todo.setCreateTime(currentTime);
            todo.setUpdateTime(currentTime);

            todoStore.insert(todo);
        } catch (Exception e) {
            logger.error("Failed to insert: {}", e.getMessage(), e);
        }
    }

    @Override
    public void updateTodo(long id, String content, boolean complete) {
        try {
            TodoEntry todo = todoStore.getTodoById(id);
            todo.setContent(content);
            todo.setComplete(complete);
            todo.setUpdateTime(Instant.now().toEpochMilli());

            todoStore.update(todo);
        } catch (Exception e) {
            logger.error("Failed to update: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<TodoEntry> getTodoList() {
        List<TodoEntry> todoList = Lists.newArrayList();
        try {
            todoList = todoStore.getAllTodos();
        } catch (Exception e) {
            logger.error("Failed to update: {}", e.getMessage(), e);
        }
        return todoList;
    }

}
