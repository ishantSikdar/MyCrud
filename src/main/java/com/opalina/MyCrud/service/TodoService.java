package com.opalina.MyCrud.service;

import java.util.List;

import com.opalina.MyCrud.exception.TodoCollectionException;
import com.opalina.MyCrud.model.TodoDTO;

import jakarta.validation.ConstraintViolationException;

public interface TodoService {
//	post method
	public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException;
	
//	Read methods
	public List<TodoDTO> getAllTodos();
	public TodoDTO getSingleTodo(String id) throws TodoCollectionException;
	
// put method
	public TodoDTO updateTodo(String id, TodoDTO todo) throws TodoCollectionException;
	
//	delete method
	public String deleteTodo(String id) throws TodoCollectionException;
}
