package com.hosung.todoapi.todo;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value="/api/todos", produces=MediaTypes.HAL_JSON_UTF8_VALUE)
public class TodoController {


    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoValidator validator;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity todoList(Pageable pageable, PagedResourcesAssembler<Todo> assembler) throws Exception{
        Page<Todo> todos = todoRepository.findAll(pageable);

        PagedResources<TodoResource> pagedResource = assembler.toResource(todos, t -> new TodoResource(t));

        pagedResource.add(new Link("/docs/index.html#resources-index", "profile"));

        return ResponseEntity.ok(pagedResource);
    }


    @GetMapping("/{id}")
    public ResponseEntity getTodo(@PathVariable Integer id) {

        Optional<Todo> byId = todoRepository.findById(id);

        if( !byId.isPresent() ) {
            return ResponseEntity.notFound().build();
        }

        TodoResource resource = new TodoResource(byId.get());

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTodo(@PathVariable Integer id, @RequestBody @Valid TodoDto todoDto, Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        validator.validate(todoDto, errors);

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Optional<Todo> byId = todoRepository.findById(id);

        if(!byId.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Todo existsTodo = byId.get();

        modelMapper.map(todoDto, existsTodo);

        Todo todo = todoRepository.save(existsTodo);
        TodoResource resource = new TodoResource(todo);

        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity createTodo(@RequestBody @Valid TodoDto todoDto, Errors errors) {

        //Annotation Level에서 탐지할 수 있는 에러
        if( errors.hasErrors() ) {
            return ResponseEntity.badRequest().body(errors);
        }

        validator.validate(todoDto, errors);

        if( errors.hasErrors() ) {
            return ResponseEntity.badRequest().body(errors);
        }

        Todo todo =  modelMapper.map(todoDto, Todo.class);

        Todo savedTodo = todoRepository.save(modelMapper.map(todo, Todo.class));
        TodoResource savedResource = new TodoResource(savedTodo);
        URI uri = linkTo(TodoController.class).slash(savedTodo.getId()).toUri();

        savedResource.add(linkTo(TodoController.class).withRel("todos"));
        savedResource.add(linkTo(TodoController.class).slash(savedTodo.getId()).withRel("update"));
        savedResource.add(new Link("/docs/index.html#resources-create-todo", "profile"));

        return ResponseEntity.created(uri).body(savedResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodo(@PathVariable Integer id) {

        todoRepository.deleteById(id);

        Link link = linkTo(TodoController.class).withRel("list");

        return ResponseEntity.ok(link);
    }
}

