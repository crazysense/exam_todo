package myyuk.exam.repo.impl;

import com.google.common.collect.Lists;
import myyuk.exam.data.TodoEntry;
import myyuk.exam.exception.ResourceNotFoundException;
import myyuk.exam.repo.TodoStore;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Todo Store Implementation for Ignite Cache
 */
@Component
@Configuration
public class TodoStoreImpl implements TodoStore {
    private static final Logger logger = LoggerFactory.getLogger(TodoStoreImpl.class);

    @Autowired
    private Ignite ignite;

    @Bean
    public IgniteAtomicSequence todoSequence() {
        return ignite.atomicSequence("TODO_SEQ", 0, true);
    }

    /**
     * Gets an ignite cache for store todo.
     * @return the cache.
     */
    public IgniteCache<Long, TodoEntry> todoCache() {
        return ignite.getOrCreateCache("TODO");
    }

    @Override
    public TodoEntry getTodoById(long id) {
        TodoEntry todo = Optional.ofNullable(todoCache().get(id)).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Cannot found todo entry: id=%d", id)));
        logger.debug("get todo: {}", todo);
        return todo;
    }

    @Override
    public List<TodoEntry> getAllTodos() {
        List<TodoEntry> todoList = Optional.ofNullable(todoCache().query(new ScanQuery<Long, TodoEntry>()).getAll())
                .orElse(Lists.newArrayList())
                .stream()
                .map(e -> e.getValue())
                .collect(Collectors.toList());
        logger.debug("get todo-list: {}", todoList);
        return todoList;
    }

    @Override
    public void insert(TodoEntry todo) {
        long id = todoSequence().incrementAndGet();
        todo.setId(id);
        logger.debug("insert todo: {}", todo);
        todoCache().put(id, todo);
    }

    @Override
    public void update(TodoEntry todo) {
        logger.debug("update todo: {}", todo);
        todoCache().put(todo.getId(), todo);
    }

    @Override
    public void delete(long id) {
        logger.debug("delete todo: id={}", id);
        todoCache().remove(id);
    }
}
