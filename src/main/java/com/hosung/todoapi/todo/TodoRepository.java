package com.hosung.todoapi.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

}
