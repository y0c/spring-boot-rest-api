package com.hosung.todoapi.todo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class TodoResource extends Resource<Todo> {

    public TodoResource(Todo content, Link... links) {
        super(content, links);
        add(linkTo(TodoController.class).slash(content.getId()).withSelfRel());
    }
}
