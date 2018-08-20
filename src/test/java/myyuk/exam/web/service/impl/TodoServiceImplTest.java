package myyuk.exam.web.service.impl;

import com.google.common.collect.Lists;
import myyuk.exam.data.TodoEntry;
import myyuk.exam.repo.TodoStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceImplTest {

    @Mock
    private TodoStore todoStore;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    public void testGetTodoList() {
        when(todoStore.getAllTodos()).thenAnswer(mock -> {
            List<TodoEntry> todoList = Lists.newArrayList();

            TodoEntry todoStudy = new TodoEntry();
            todoStudy.setId(1L);
            todoStudy.setContent("Study SAT 4PM-6PM");
            todoList.add(todoStudy);

            return todoList;
        });

        List<TodoEntry> todoList = todoService.getTodoList();

        assertEquals(1, todoList.size());
        assertEquals(1L, todoList.get(0).getId());
        assertEquals("Study SAT 4PM-6PM", todoList.get(0).getContent());
        assertNotNull(todoList.get(0).getCreateTime());
        assertNotNull(todoList.get(0).getUpdateTime());

        verify(todoStore).getAllTodos();
        verifyNoMoreInteractions(todoStore);
    }

    @Test
    public void testCreateTodo() {
        todoService.createTodo("Washing MON 8AM-8:30AM");
        verify(todoStore).insert(Mockito.any(TodoEntry.class));
        verifyNoMoreInteractions(todoStore);
    }

    @Test
    public void testUpdateTodo() {
        when(todoStore.getTodoById(3L)).thenAnswer(mock -> {
            TodoEntry todoStudy = new TodoEntry();
            todoStudy.setId(3L);
            todoStudy.setContent("Cleaning WED 6PM-6:30PM");
            todoStudy.setCreateTime(System.currentTimeMillis());
            todoStudy.setUpdateTime(System.currentTimeMillis());
            return todoStudy;
        });

        todoService.updateTodo(3L, "Cleaning WED 6PM-6:30PM", true);
        verify(todoStore).getTodoById(3L);
        verify(todoStore).update(Mockito.any(TodoEntry.class));
        verifyNoMoreInteractions(todoStore);
    }
}