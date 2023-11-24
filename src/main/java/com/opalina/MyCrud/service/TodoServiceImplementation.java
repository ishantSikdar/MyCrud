package com.opalina.MyCrud.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opalina.MyCrud.exception.TodoCollectionException;
import com.opalina.MyCrud.model.TodoDTO;
import com.opalina.MyCrud.repository.TodoRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class TodoServiceImplementation implements TodoService {
	
	@Autowired
	public TodoRepository todoRepo;

//	override the postmapping (creating a todo)
	@Override
	public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
		Optional<TodoDTO> fetchedTodo = todoRepo.findByTodo(todo.getTodo());
		
		if (fetchedTodo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
		} else {
			todo.setCreatedAt(new Date(System.currentTimeMillis()));
			todoRepo.save(todo);
		}	
	}
	
//	override the getMapping (reading all todos)
	@Override
	public List<TodoDTO> getAllTodos() {
		List<TodoDTO> todos = todoRepo.findAll();
		
		if (!todos.isEmpty()) {
			return todos;
		} else {
			return new ArrayList<>();
		}
	}
	
//	override the getMapping (reading a single todo)
	@Override
	public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		
		if (requiredDocument.isPresent()) {
			return requiredDocument.get();
		} else {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
	}
	
// override the putMapping (updating a todo)
	@Override
	public TodoDTO updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		Optional<TodoDTO> sameNameTodo = todoRepo.findByTodo(id);

//		checking if the updated name already exists in todo list
		if (sameNameTodo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
		}

		if (requiredDocument.isPresent()) {
//			getting required record
			TodoDTO todoToSave = requiredDocument.get();

//			updating accordingly with the inputted values,
			todoToSave.setTodo(todo.getTodo() != null ? todo.getTodo() : todoToSave.getTodo());
			todoToSave.setDescription(todo.getDescription() != null ? todo.getDescription() : todoToSave.getDescription());
			todoToSave.setCompleted(todo.getCompleted() != null ? todo.getCompleted() : todoToSave.getCompleted());
			todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));

			//	new (23-11)
			todoToSave.setCost(todo.getCost() != 0.0 ? todo.getCost() : todoToSave.getCost());
			todoToSave.setTaskNumber(todo.getTaskNumber() != 0 ? todo.getTaskNumber() : todoToSave.getTaskNumber());
			todoToSave.setUniCode(todo.getUniCode() != null ? todo.getUniCode() : todoToSave.getUniCode());
			todoToSave.setSymbol(todo.getSymbol() != null ? todo.getSymbol() : todoToSave.getSymbol());

			todoToSave.setDetails(todo.getDetails() != null ? todo.getDetails() : todoToSave.getDetails());
			todoRepo.save(todoToSave);
			return todoToSave;		
			
		} else {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
			
		}
	}
	
//	override the deleteMapping (to delete a todo)
	@Override
	public String deleteTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		
		if (requiredDocument.isPresent()) {
			todoRepo.deleteById(id);
			return "Todo of ID: " + id + ", successfully deleted";
		} else {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
	}
	
}
