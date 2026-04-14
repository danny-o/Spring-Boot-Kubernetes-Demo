package com.digitalskies.kubernetes_demo.todo;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;

@Controller
public class ToDoController {

    
    private ToDoService toDoService;

    Logger logger= LoggerFactory.getLogger(getClass());

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }



    @RequestMapping("/")
    String listToDo(ModelMap modelMap){



        var toDos = toDoService.findByUserName(getLoggedInUsername());
        modelMap.addAttribute("ToDos",toDos);

        return "todos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    String showNewToDoPage(ModelMap modelMap){
        String username = getLoggedInUsername();
        var toDo=new ToDo();
        toDo.setUsername(username);
        toDo.setDescription("");
        toDo.setTargetDate(LocalDate.now().plusYears(1));
        toDo.setDone(false);
        modelMap.put("todo",toDo);
        return "todo-form";
    }



    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    String addNewToDo(ModelMap modelMap, @Valid ToDo toDo, BindingResult bindingResult){

        logger.debug("adding to do {}", toDo.toString());
        if(bindingResult.hasErrors()){

            for(ObjectError error: bindingResult.getAllErrors()){
                logger.debug("error adding todo {}",error);
            }

            return "todo-form";
        }

        String username = getLoggedInUsername();

        toDo.setUsername(username);

        toDoService.addToDo(toDo);

        return "redirect:/";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    String showUpdateToDoPage(ModelMap modelMap,@RequestParam long id){

        ToDo toDo=toDoService.findByID(id);

        modelMap.put("todo",toDo);


        return "update-todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    String updateToDo(ModelMap modelMap,@Valid ToDo todo){

        toDoService.updateToDo(todo);


        return "redirect:/";
    }

    @RequestMapping(value = "delete-todo", method = RequestMethod.GET)
    String deleteToDo(@RequestParam long id){

    toDoService.deleteToDo(id);


        return "redirect:/";
    }

    private String getLoggedInUsername() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        return authentication.getName();
    }
}
