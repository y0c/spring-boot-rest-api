package com.hosung.todoapi.todo;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter @Setter @AllArgsConstructor
@Builder @NoArgsConstructor @EqualsAndHashCode(of="id")
public class Todo {

    @Id @GeneratedValue
    private Integer id;
    private String title;
    private String content;
    private TodoStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

}
