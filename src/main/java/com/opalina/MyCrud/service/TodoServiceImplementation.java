package com.opalina.MyCrud.service;

import java.util.*;

import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opalina.MyCrud.exception.TodoCollectionException;
import com.opalina.MyCrud.model.TodoDTO;
import com.opalina.MyCrud.repository.TodoRepository;


@Service
public class TodoServiceImplementation implements TodoService {
	
	@Autowired
	public TodoRepository todoRepo;

	private static final Logger logger = LoggerFactory.getLogger("com.opalina.MyCrud.service");

//	override the post mapping (creating a todo)
	@Override
	public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
		Optional<TodoDTO> fetchedTodo = todoRepo.findByTodo(todo.getTodo());

		if (fetchedTodo.isPresent()) {
			logger.error("Failed to add new record because of conflict");
			throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());

		} else if (todo.getTodo() == null) {

			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<TodoDTO>> violations = validator.validateProperty(todo, "todo");
			logger.error("Failed to add new record because todo is null");
			throw new ConstraintViolationException("Todo cannot be null", violations);

		} else {
			logger.info("New record of Todo: " + todo.getTodo() + " added");
			todo.setCreatedAt(new Date(System.currentTimeMillis()));
			todoRepo.save(todo);
		}
	}

//	override the getMapping (reading all todos)
	@Override
	public List<TodoDTO> getAllTodos() {
		List<TodoDTO> todos = todoRepo.findAll();
		
		if (!todos.isEmpty()) {
			logger.info("Database records fetched");
			return todos;
		} else {
			logger.info("Database records fetched");
			return new ArrayList<>();
		}
	}
	
//	override the getMapping (reading a single todo)
	@Override
	public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		
		if (requiredDocument.isPresent()) {
			logger.info("Document of ID: " + id + " found");
			return requiredDocument.get();
		} else {
			logger.error("Document of ID: " + id + " not found");
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
	}
	
// override the putMapping (updating a todo)  // bugs needs to be fixed
	@Override
	public TodoDTO updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		Optional<TodoDTO> sameNameTodo = todoRepo.findByTodo(todo.getTodo());

//		checking if the updated name already exists in todo list
		if (sameNameTodo.isPresent()) {
			logger.error("Failed to update todo, because given todo already exists");
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
			todoToSave.setSkills(todo.getSkills() != null ? todo.getSkills() : todoToSave.getSkills());

			todoRepo.save(todoToSave);
			logger.info("Record of Todo: " + todo.getTodo() + " updated");
			return todoToSave;		
			
		} else {
			logger.error("Failed to update record because the required document not found");
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
			
		}
	}
	
//	override the deleteMapping (to delete a todo)
	@Override
	public String deleteTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> requiredDocument = todoRepo.findById(id);
		
		if (requiredDocument.isPresent()) {
			todoRepo.deleteById(id);
			logger.info("Todo of ID: " + id + " successfully deleted");
			return "Todo of ID: " + id + ", successfully deleted";

		} else {
			logger.error("Failed to delete today because todo not found");
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
	}
	
}
