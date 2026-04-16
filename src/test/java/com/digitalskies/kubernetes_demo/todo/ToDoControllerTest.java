package com.digitalskies.kubernetes_demo.todo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ToDoControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void testAddNewToDo_Successful(){
        ToDo todo= new ToDo();
        todo.setUsername("test");
        todo.setDescription("Learn Kubernetes");
        todo.setTargetDate(LocalDate.now().plusMonths(1));
        todo.setDone(false);


        var result=mockMvcTester.post()
                .with(user("test").password("password"))
                .uri("/add-todo")
                .param("toDo",todo.toString())
                .exchange();

        assertThat(result).hasStatusOk();
    }
}
