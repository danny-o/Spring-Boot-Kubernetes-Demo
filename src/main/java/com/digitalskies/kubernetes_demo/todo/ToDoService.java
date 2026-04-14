package com.digitalskies.kubernetes_demo.todo;

//import org.springframework.security.core.parameters.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Service
public class ToDoService {

    private static List<ToDo> toDos = new ArrayList<>();

    private final ToDoRepository toDoRepository;

//    static {
//        toDos.add(new ToDo(1,"DigitalSkies","Learn AWS", LocalDate.now().plusYears(1),false));
//
//       toDos.add(new ToDo(2,"DigitalSkies","Learn Azure", LocalDate.now().plusYears(2),false));
//
//       toDos.add(new ToDo(3,"DigitalSkies","Learn Google Cloud", LocalDate.now().plusYears(3),false));
//
//
//    }

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }




    List<ToDo> findByUserName(String username){

        return toDoRepository.findByUsername(username);
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
