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

}
