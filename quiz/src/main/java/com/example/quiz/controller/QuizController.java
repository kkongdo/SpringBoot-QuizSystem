package com.example.quiz.controller;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizForm;
import com.example.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    QuizService service;

    @ModelAttribute
    public QuizForm setUpForm() {
        QuizForm form = new QuizForm();
        form.setAnswer(1);
        return form;
    }

    @GetMapping("/play")
    public String showQuiz(QuizForm quizForm, Model model){
        Optional<Quiz> quizOpt = service.selectOneRandomQuiz();
        if(quizOpt.isPresent()){
            Optional<QuizForm> quizFormOpt = quizOpt.map(t -> (QuizForm) makeQuizForm(t));
            quizForm = quizFormOpt.get();
        }
        else{
            model.addAttribute("msg","등록된 문제가 없습니다.");
            return "play";
        }
        model.addAttribute("quizForm",quizForm);
        return "play";
    }
    @PostMapping("/check")
    public String checkQuiz(QuizForm quizForm, @RequestParam Integer answer, Model model){
        if(service.checkQuiz(quizForm.getId(), answer) == 1){
            model.addAttribute("msg","정답입니다.");
        }
        else{
            model.addAttribute("msg","오답입니다.");
        }
        return "answer";
    }

    @GetMapping
    public String showList(QuizForm quizform, Model model) {
        quizform.setNewQuiz(1);
        Iterable<Quiz> list = service.selectAll();
        model.addAttribute("list", list);
        model.addAttribute("title", "등록폼");
        return "crud";
    }

    @PostMapping("/insert")
    public String insert(@Validated QuizForm quizForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());

        if (!bindingResult.hasErrors()) {
            service.insertQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "등록이 완료되었습니다.");
            return "redirect:/quiz";
        } else {
            return showList(quizForm, model);
        }
    }

    @PostMapping("/update")
    public String update(@Validated QuizForm quizForm, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Quiz quiz = makeQuiz(quizForm);
        if (!result.hasErrors()) {
            service.updateQuiz(quiz);
            redirectAttributes.addFlashAttribute("complete", "변경이 완료되었습니다.");
            return "redirect:/quiz/" + quiz.getId();
        } else {
            makeUpdateModel(quizForm, model);
            return "crud";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id, Model model, RedirectAttributes redirectAttributes) {
        service.deleteQuizById(Integer.parseInt(id));
        redirectAttributes.addFlashAttribute("delcomplete" , "삭제 완료했습니다.");
        return "redirect:/quiz";
    }

    private Quiz makeQuiz(QuizForm quizForm) {
        Quiz quiz = new Quiz();
        quiz.setId(quizForm.getId());
        quiz.setQuestion(quizForm.getQuestion());
        quiz.setAnswer(quizForm.getAnswer());
        quiz.setAuthor(quizForm.getAuthor());
        return quiz;
    }

    @GetMapping("/{id}")
    public String showUpdate(QuizForm quizForm, @PathVariable Integer id, Model model) {
        Optional<Quiz> quizOpt = service.selectOneById(id);
        Optional<QuizForm> quizFormOpt = quizOpt.map(t -> (QuizForm) makeQuizForm(t));
        if (quizFormOpt.isPresent()) {
            quizForm = quizFormOpt.get();
        }
        makeUpdateModel(quizForm, model);
        return "crud";
    }

    private void makeUpdateModel(QuizForm quizForm, Model model) {
        model.addAttribute("id", quizForm.getId());
        quizForm.setNewQuiz(0);
        model.addAttribute("quizForm", quizForm);
        model.addAttribute("title", "변경폼");
    }

    private Object makeQuizForm(Quiz quiz) {
        QuizForm form = new QuizForm();
        form.setId(quiz.getId());
        form.setQuestion(quiz.getQuestion());
        form.setAnswer(quiz.getAnswer());
        form.setAuthor(quiz.getAuthor());
        form.setNewQuiz(0);
        return form;
    }

}
