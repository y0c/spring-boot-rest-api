package com.hosung.todoapi.todo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void index_200() throws Exception {
        this.mockMvc.perform(get("/api/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").hasJsonPath());
    }

    @Test
    public void todoCreate() throws Exception {
        Todo todo = Todo.builder()
                        .id(232)
                        .title("오늘 할일")
                        .content("Test Todo!!!")
                        .status(TodoStatus.COMPLETED)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        this.mockMvc.perform(
            post("/api/todos")
                .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(todo))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").hasJsonPath());
    }

    @Test
    public void todoCreate_badRequest() throws Exception {

        this.mockMvc.perform(
                post("/api/todos")
                .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

}
