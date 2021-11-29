package com.todolist.task.persistence.repository;

import com.todolist.task.persistence.entity.Task;
import com.todolist.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Transactional
    void deleteByIdAndUser(Long id, User user);

    @Transactional
    void deleteAllByUser(User user);

    List<Task> findAllByUser(User user);

}
