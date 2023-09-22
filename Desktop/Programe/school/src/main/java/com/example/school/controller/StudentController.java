package com.example.school.controller;

import com.example.school.dto.StudentDto;
import com.example.school.entity.Student;
import com.example.school.service.StudentService;
import lombok.Getter;
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
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/studentHome")
    public String getStudentHome() {
        return "/student/studentHome";
    }

    @GetMapping("/studentForm")
    public String getStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "/student/studentForm";
    }

    @PostMapping("/studentForm")
    public String addStudent(@ModelAttribute StudentDto studentDto) {
        studentService.addStudent(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/students")
    public String getStudentList(Model model) {
        model.addAttribute("studentListDto", studentService.getStudentListDto());
        return "/student/students";
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam int studentId) {
        studentService.deleteStudent(studentId);
        return "redirect:/students";
    }

    @GetMapping("/studentModify")
    public String modifyStudentForm(@RequestParam int studentId, Model model) {
        model.addAttribute("student", studentService.getStudent(studentId));
        model.addAttribute("studentLastName",
                studentService.getStudent(studentId).getStudentLastName());
        model.addAttribute("studentFirstName",
                studentService.getStudent(studentId).getStudentFirstName());
        return "student/studentModify";
    }

    @PostMapping("/studentModify")
    public String modifyStudent(@ModelAttribute StudentDto studentDto) {
        studentService.modifyStudent(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/catalogForStudents")
    public String getCatalogForStudents(@RequestParam int studentId, Model model) {
        model.addAttribute("studentLastName",
                studentService.getStudent(studentId).getStudentLastName());
        model.addAttribute("studentFirstName",
                studentService.getStudent(studentId).getStudentFirstName());
        model.addAttribute("classMaster", studentService.getClassMaster(studentId));
        model.addAttribute("className",
                studentService.getStudent(studentId).getClassOfStudents().getClassName());
        model.addAttribute("subjectList", studentService.getSubjectList(studentId));
        return "student/studentCatalog";
    }
}
