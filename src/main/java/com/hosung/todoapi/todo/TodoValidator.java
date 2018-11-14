package com.hosung.todoapi.todo;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class TodoValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TodoDto todoDto = (TodoDto)target;

        if(todoDto.getDueDate().isBefore(LocalDateTime.now())) {
            errors.rejectValue("dueDate", "dueDate cannot before current date!");
        }
    }
}
