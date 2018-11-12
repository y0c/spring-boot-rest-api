package com.hosung.todoapi.todo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value="/api/todos", produces=MediaTypes.HAL_JSON_UTF8_VALUE)
public class TodoController {


    @Autowired
    public TodoRepository todoRepository;

    @GetMapping
    public ResponseEntity index() {

//        return ResponseEntity.ok();

        return ResponseEntity.ok(new TodoResource(Todo.builder().id(1).build()));
    }

    @PostMapping
    public ResponseEntity createTodo(@RequestBody @Valid Todo todo, Errors errors) {

        if( errors.hasErrors() ) {
            return ResponseEntity.badRequest().body(errors);
        }

        Todo savedTodo = todoRepository.save(todo);
        TodoResource savedResource = new TodoResource(savedTodo);
        URI uri = linkTo(TodoController.class).slash(savedTodo.getId()).toUri();

        return ResponseEntity.created(uri).body(savedResource);
    }
}

