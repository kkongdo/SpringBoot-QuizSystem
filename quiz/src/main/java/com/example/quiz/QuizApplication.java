package com.example.quiz;

import com.example.quiz.entity.Quiz;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@SpringBootApplication
public class QuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args).getBean(QuizApplication.class).execute();
    }
    @Autowired
    QuizService service;

    private void execute() {
//        setUp();
//        showList();
//        showOne();
//        updateQuiz();
//        deleteQuiz();
        doQuiz();
    }

    private void doQuiz() {
        System.out.println("--- 퀴즈 1건 취득 개시---");
        Optional<Quiz> quizOpt = service.selectOneRandomQuiz();
        if(quizOpt.isPresent()){
            System.out.println(quizOpt.get());
        }
        else{
            System.out.println("해당 데이터는 존재하지 않습니다.");
        }
        System.out.println("----1건 취득 완료 ----");
        int num = 0;
        Integer id = quizOpt.get().getId();
        if(service.checkQuiz(id, num)){
            System.out.println("정답입니다!!");
        }
        else{
            System.out.println("오답입니다!!");
        }
    }

    private void deleteQuiz() {
        System.out.println("---삭제 처리 개시 ---");
        service.deleteQuizById(3);
        System.out.println("---삭제 처리 완료");
    }

    private void updateQuiz() {
        System.out.println("----변경 처리 개시----");
        Quiz quiz1 = new Quiz(3, "스프링은 프레임 워크 입니다요", 1, "변경 담당");
        service.updateQuiz(quiz1);
        System.out.println("--변경된 데이터는" + quiz1 + "입니다");
        System.out.println("----변경 처리 완료----");
    }

    private void showOne() {
        Optional<Quiz> quizOpt = service.selectOneById(3);
        if(quizOpt.isPresent()){
            System.out.println(quizOpt.get());
        }
        else{
            System.out.println("해당 데이터는 존재하지 않습니다.");
        }
        System.out.println("----1건 취득 완료----");
    }

    private void showList() {
        System.out.println("--모든 데이터 취득 개시----");
        Iterable<Quiz> quizzes = service.selectAll();
        for(Quiz quiz : quizzes){
            System.out.println(quiz);
        }
        System.out.println("--모든 데이터 취득 완료--");
    }

    private void setUp() {
        System.out.println("---등록 처리 개시---");
        Quiz quiz1 = new Quiz(null, "자바는 객체지향 언어?" , 1 , "홍길동");
        Quiz quiz2 = new Quiz(null, "스프링 데이터는 데이터 액세스에 관련된 기능을 제공? ", 0, "홍길동");
        Quiz quiz3 = new Quiz(null, "프로그램이 많은 등록되어 잇는 서버 ", 0, "홍길동");
        Quiz quiz4 = new Quiz(null, "인스턴스 생성 어노테이션 ", 0, "홍길동");
        Quiz quiz5 = new Quiz(null, "스프링 MVC구현뭐시꺵이 ", 0, "홍길동");

        ArrayList<Quiz> list = new ArrayList<>();
        Collections.addAll(list,quiz1,quiz2,quiz3,quiz4,quiz5);

        for(Quiz quiz : list){
            service.insertQuiz(quiz);
        }
        System.out.println("--등록처리완료--");
    }

}
