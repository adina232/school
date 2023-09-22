package com.example.school.controller;

import com.example.school.dto.SubjectDto;
import com.example.school.entity.Subject;
import com.example.school.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subjectHome")
    public String getSubjectPage() {
        return "subject/subjectHome";
    }
    @GetMapping("/subjectForm")
    public ModelAndView addSubjectForm() {
        ModelAndView modelAndView = new ModelAndView("/subject/subjectForm");
        modelAndView.addObject("subject", new Subject());
        return modelAndView;

    }
    @PostMapping("/subjectForm")
    public String addSubject(@ModelAttribute SubjectDto subjectDto) {
        subjectService.addSubject(subjectDto);
        return "redirect:/subjects";
    }

    @GetMapping("/subjects")
    public String getSubjects(Model model) {
        model.addAttribute("subjectList", subjectService.getSubjects());
        return "subject/subjects";
    }

    @GetMapping("/subjectSeeTeachers")
    public String subjectSeeTeachers(@RequestParam int subjectId, Model model) {
        model.addAttribute("teacherList", subjectService.getTeachersForSubject(subjectId));
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("subjectName", subjectService.getSubjectName(subjectId));
        return "subject/subjectSeeTeachers";
    }

    @GetMapping("/subjectSeeTeachersToAdd")
    public String subjectSeeTeachersToAdd(@RequestParam int subjectId, Model model) {
        model.addAttribute("teachersToAdd", subjectService.getTeachersToAdd(subjectId));
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("subjectName", subjectService.getSubjectName(subjectId));
        return "subject/subjectAddTeacher";
    }
    @GetMapping("/subjectAddTeacher")
    public String subjectAddTeacher(@RequestParam int subjectId, @RequestParam int teacherId) {
        subjectService.addTeacherToSubject(subjectId, teacherId);
        return "redirect:/subjectSeeTeachers?subjectId=" + subjectId;
    }

    @GetMapping("/deleteSubject")
    public String deleteSubject(@RequestParam int subjectId) {
        subjectService.deleteSubject(subjectId);
        return "redirect:/subjects";
    }

    @GetMapping("/deleteTeacherFromSubject")
    public String deleteTeacherFromSubject(@RequestParam int subjectId, @RequestParam int teacherId) {
        subjectService.deleteTeacherFromSubject(subjectId, teacherId);
        return "redirect:/subjectSeeTeachers?subjectId=" + subjectId;
    }
}
