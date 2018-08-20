package myyuk.exam.repo.impl;

import com.google.common.collect.Lists;
import myyuk.exam.data.TodoEntry;
import myyuk.exam.exception.ResourceNotFoundException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoStoreImplTest {

    @Mock
    private Ignite ignite;
    @Mock
    private IgniteCache igniteCache;
    @Mock
    private IgniteAtomicSequence sequence;

    @InjectMocks
    private TodoStoreImpl todoStore;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        when(ignite.getOrCreateCache("TODO")).thenReturn(igniteCache);
        when(ignite.atomicSequence("TODO_SEQ", 0, true)).thenReturn(sequence);
    }

    @Test
    public void testGetTodoById() {
        when(igniteCache.get(1L)).thenAnswer(mock -> {
            TodoEntry todoStudy = new TodoEntry();
            todoStudy.setId(1L);
            todoStudy.setContent("Study SAT 4PM-6PM");
            todoStudy.setCreateTime(System.currentTimeMillis());
            todoStudy.setUpdateTime(System.currentTimeMillis());
            return todoStudy;
        });

        todoStore.getTodoById(1L);

        verify(igniteCache).get(1L);
        verifyNoMoreInteractions(igniteCache);
    }

    @Test
    public void testGetTodoByNonExistId() {
        when(igniteCache.get(1L)).thenReturn(null);
        thrown.expect(ResourceNotFoundException.class);
        todoStore.getTodoById(1L);
        fail("Expected exception is not thrown.");
    }

    @Test
    public void testGetAllTodos() {
        QueryCursor queryCursor = mock(QueryCursor.class);
        when(queryCursor.getAll()).thenReturn(Lists.newArrayList());
        when(igniteCache.query(Mockito.any(ScanQuery.class))).thenReturn(queryCursor);

        todoStore.getAllTodos();

        verify(igniteCache).query(Mockito.any(ScanQuery.class));
        verifyNoMoreInteractions(igniteCache);
    }

    @Test
    public void testGetAllTodosEmpty() {
        QueryCursor queryCursor = mock(QueryCursor.class);
        when(queryCursor.getAll()).thenReturn(null);
        when(igniteCache.query(Mockito.any(ScanQuery.class))).thenReturn(queryCursor);

        List<TodoEntry> todoList = todoStore.getAllTodos();
        assertEquals(0, todoList.size());

        verify(igniteCache).query(Mockito.any(ScanQuery.class));
        verifyNoMoreInteractions(igniteCache);
    }

    @Test
    public void testInsert() {
        TodoEntry todoClean = new TodoEntry();
        todoClean.setContent("Cleaning WED 6PM-6:30PM");
        todoClean.setCreateTime(System.currentTimeMillis());
        todoClean.setUpdateTime(System.currentTimeMillis());

        doNothing().when(igniteCache).put(1L, todoClean);
        when(sequence.incrementAndGet()).thenReturn(1L);

        todoStore.insert(todoClean);
        assertEquals(1L, todoClean.getId());

        verify(sequence).incrementAndGet();
        verify(igniteCache).put(1L, todoClean);

        verifyNoMoreInteractions(sequence, igniteCache);
    }

    @Test
    public void testUpdate() {
        TodoEntry todoWashing = new TodoEntry();
        todoWashing.setId(2L);
        todoWashing.setContent("Washing MON 8AM-8:30AM");
        todoWashing.setCreateTime(System.currentTimeMillis());
        todoWashing.setUpdateTime(System.currentTimeMillis());
        todoWashing.setComplete(true);

        doNothing().when(igniteCache).put(2L, todoWashing);

        todoStore.update(todoWashing);

        verify(igniteCache).put(2L, todoWashing);
        verifyNoMoreInteractions(igniteCache);
    }

    @Test
    public void testDelete() {
        when(igniteCache.remove(1L)).thenReturn(true);

        todoStore.delete(1L);

        verify(igniteCache).remove(1L);
        verifyNoMoreInteractions(igniteCache);
    }
}