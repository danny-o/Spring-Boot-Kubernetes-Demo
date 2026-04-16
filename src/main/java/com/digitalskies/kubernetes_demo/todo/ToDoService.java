package com.digitalskies.kubernetes_demo.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class ToDoService {

    private static List<ToDo> toDos = new ArrayList<>();

    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    List<ToDo> findByUserName(String username){

        return toDoRepository.findByUsername(username);
    }

    public void addInitialToDo(String username){
        ToDo todo= new ToDo();
        todo.setUsername(username);
        todo.setDescription("Learn Kubernetes");
        todo.setTargetDate(LocalDate.now().plusMonths(1));
        todo.setDone(false);

        addToDo(todo);


    }

    ToDo findByID(long id){

        return toDoRepository.findById(id).get();
    }

    void addToDo(ToDo todo){
        toDoRepository.save(todo);
    }

    void updateToDo(ToDo updatedToDo){

        toDoRepository.save(updatedToDo);

    }

    void deleteToDo(long id){

        toDoRepository.deleteById(id);
    }
}
