package com.hosung.todoapi.todo;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;


public class TodoTest {

    @Test
    public void builderTest() {
        Todo todo = Todo.builder()
                        .id(1)
                        .title("Test")
                        .content("Test Todo Api")
                        .status(TodoStatus.COMPLETED)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                    .build();

        assertThat(todo.getId())
                .isEqualTo(1);
    }
}
