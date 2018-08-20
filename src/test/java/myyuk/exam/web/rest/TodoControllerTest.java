package myyuk.exam.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import myyuk.exam.web.ajax.TodoRequest;
import myyuk.exam.web.service.TodoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TodoControllerTest {
    @Mock
    private TodoService todoService;
    @InjectMocks
    private TodoController todoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void testGetTodos() throws Exception {
        when(todoService.getTodoList()).thenReturn(Lists.newArrayList());
        mockMvc.perform(get("/api/todos/")).andExpect(status().isOk());

        verify(todoService).getTodoList();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void testPostTodos() throws Exception {
        when(todoService.getTodoList()).thenReturn(Lists.newArrayList());

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setContent("Cleaning WED 6PM-6:30PM");
        String json = objectMapper.writeValueAsString(todoRequest);

        mockMvc.perform(post("/api/todos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(todoService).createTodo("Cleaning WED 6PM-6:30PM");
        verify(todoService).getTodoList();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void testPutTodos() throws Exception {
        when(todoService.getTodoList()).thenReturn(Lists.newArrayList());

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setContent("Washing MON 8AM-8:30AM");
        todoRequest.setComplete(true);
        String json = objectMapper.writeValueAsString(todoRequest);

        mockMvc.perform(put("/api/todos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(todoService).updateTodo(1L, "Washing MON 8AM-8:30AM", true);
        verify(todoService).getTodoList();
        verifyNoMoreInteractions(todoService);
    }

}