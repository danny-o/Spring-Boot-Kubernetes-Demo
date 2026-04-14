package com.digitalskies.kubernetes_demo.todo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo,Long> {

    List<ToDo> findByUsername(String username);
}
