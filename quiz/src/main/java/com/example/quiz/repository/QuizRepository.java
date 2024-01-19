package com.example.quiz.repository;

import com.example.quiz.entity.Quiz;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {
    @Query("SELECT ID FROM QUIZ where id = 5 ORDER BY ID desc ")
    Integer getRandomId();
}
