package com.hosung.todoapi.todo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.IntStream;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class TodoControllerTest {


    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Test
    public void todolist_200() throws Exception {
        IntStream.range(0,30).forEach(this::saveTodo);
        this.mockMvc.perform(
                get("/api/todos")
                .param("page","0")
                .param("size","10")
        )
                .andDo(print())
                .andDo(document("get-todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").hasJsonPath());
    }


    private Todo saveTodo(int index) {
        Todo todo = Todo.builder()
                .id(index)
                .title(index + "")
                .content("Test Todo!!!" + index)
                .status(TodoStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .build();
        return  todoRepository.save(todo);
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
                        .dueDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build();

        this.mockMvc.perform(
            post("/api/todos")
                .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(todo))
        )
                .andDo(print())
                .andDo(document("create-todo", links(
                    linkWithRel("self").description("link to self"),
                    linkWithRel("update").description("link to update"),
                    linkWithRel("profile").description("link to profile"),
                    linkWithRel("todos").description("link to todos")
                ),
                requestFields(
                    fieldWithPath("id").description("Todo id"),
                    fieldWithPath("status").description("Todo status"),
                    fieldWithPath("createdAt").description("Todo createdAt"),
                    fieldWithPath("updatedAt").description("Todo updatedAt"),
                    fieldWithPath("dueDate").description("Todo dueDate"),
                    fieldWithPath("title").description("Todo Title"),
                    fieldWithPath("content").description("Todo Content")
                ),
                relaxedResponseFields(
                    fieldWithPath("id").description("Todo id"),
                    fieldWithPath("status").description("Todo status"),
                    fieldWithPath("createdAt").description("Todo createdAt"),
                    fieldWithPath("updatedAt").description("Todo updatedAt"),
                    fieldWithPath("dueDate").description("Todo dueDate"),
                    fieldWithPath("title").description("Todo Title"),
                    fieldWithPath("content").description("Todo Content")
                )
                ))
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

    @Test
    public void todoCreate_domain_error() throws Exception {
        Todo todo = Todo.builder()
                .id(232)
                .title("오늘 할일")
                .content("Test Todo!!!")
                .status(TodoStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .dueDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .build();

        this.mockMvc.perform(
                post("/api/todos")
                        .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(todo))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("dueDate"));

    }

    @Test
    public void getTodo() throws Exception{
        Todo todo = saveTodo(10);


        this.mockMvc.perform(
                get("/api/todos/" + todo.getId())
                .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
        )
                .andDo(print())
                .andDo(document("get-todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").hasJsonPath());
    }

    @Test
    public void todoUpdate_200() throws Exception {
        Todo todo = saveTodo(1);
        Todo updateTodo = saveTodo(2);

        this.mockMvc.perform(
                put("/api/todos/" + todo.getId())
                .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(updateTodo))
        )
            .andDo(print())
            .andDo(document("update-todo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("_links.self").hasJsonPath())
            .andExpect(jsonPath("title").value(updateTodo.getTitle()));
    }

    @Test
    public void todoUpdate_notFound() throws Exception{
        Todo updateTodo = saveTodo(2);

        this.mockMvc.perform(
                put("/api/todos/3")
                        .contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(updateTodo))
        )
            .andDo(print())
            .andExpect(status().isNotFound());
    }


    @Test
    public void todoDelete_ok() throws Exception {
        saveTodo(1);
        this.mockMvc.perform(
                delete("/api/todos/1")
        )
            .andDo(print())
            .andExpect(status().isOk());
    }
}
