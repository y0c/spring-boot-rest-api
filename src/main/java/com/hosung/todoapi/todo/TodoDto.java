package com.hosung.todoapi.todo;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TodoDto {

    @NotEmpty
    @Length(max=200)
    private String title;

    @NotEmpty
    @Length(max=3000)
    private String content;

    @Enumerated(value=EnumType.STRING)
    private TodoStatus status;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @NotNull
    private LocalDateTime dueDate;

}
