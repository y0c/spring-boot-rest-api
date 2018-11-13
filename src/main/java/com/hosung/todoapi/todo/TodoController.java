package com.hosung.todoapi.todo;


import org.modelmapper.ModelMapper;
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
    private TodoRepository todoRepository;

    @Autowired
    private TodoVaildator vaildator;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity index() {

//        return ResponseEntity.ok();

        return ResponseEntity.ok(new TodoResource(Todo.builder().id(1).build()));
    }

    @PostMapping
    public ResponseEntity createTodo(@RequestBody @Valid TodoDto todoDto, Errors errors) {

        //Annotation Level에서 탐지할 수 있는 에러
        if( errors.hasErrors() ) {
            return ResponseEntity.badRequest().body(errors);
        }

        vaildator.validate(todoDto, errors);

        if( errors.hasErrors() ) {
            return ResponseEntity.badRequest().body(errors);
        }

        Todo todo =  modelMapper.map(todoDto, Todo.class);

        Todo savedTodo = todoRepository.save(modelMapper.map(todo, Todo.class));
        TodoResource savedResource = new TodoResource(savedTodo);
        URI uri = linkTo(TodoController.class).slash(savedTodo.getId()).toUri();

        return ResponseEntity.created(uri).body(savedResource);
    }
}

