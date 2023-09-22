package com.example.school.controller;

import com.example.school.dto.TeacherDto;
import com.example.school.entity.School;
import com.example.school.entity.Teacher;
import com.example.school.service.SchoolService;
import com.example.school.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final SchoolService schoolService;

    @PostMapping("/teacherForm")
    public String addTeacher(@ModelAttribute TeacherDto teacherDto) {
        teacherService.addTeacher(teacherDto);
        return "redirect:/teachers";
    }

    @GetMapping("/teacherForm")
    public ModelAndView getTeacherForm() {
        ModelAndView modelAndView = new ModelAndView("/teacher/teacherForm");
        modelAndView.addObject("teacher", new Teacher());
        return modelAndView;
    }

    @GetMapping("/teachers")
    public String getTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getTeachers());
        return "teacher/teachers";
    }

    @GetMapping("/deleteTeacher")
    public String deleteTeacher(@RequestParam int teacherId) {
        teacherService.deleteTeacher(teacherId);
        return "redirect:/teachers";
    }

    @GetMapping("/modifyTeacher")
    public String modifyTeacher(@RequestParam int teacherId, Model model) {
        model.addAttribute("teacherDto", teacherService.getTeacherDto(teacherId));
        model.addAttribute("teacherFirstName", teacherService.getTeacherFirstName(teacherId));
        model.addAttribute("teacherLastName", teacherService.getTeacherLastName(teacherId));
        return "teacher/teacherEdit";
    }

    @PostMapping("/modifyTeacher")
    public String editTeacher(@ModelAttribute TeacherDto teacherDto) {
        teacherService.editTeacher(teacherDto);
        return "redirect:/teachers";
    }

    @GetMapping("/teacherSeesSchools")
    public String getTeacherSeesSchools(@RequestParam int teacherId,
                                        Model model) {
        model.addAttribute("teacherSchools", teacherService.getTeacherSchools(teacherId));
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("teacherFirstName", teacherService.getTeacherFirstName(teacherId));
        model.addAttribute("teacherLastName", teacherService.getTeacherLastName(teacherId));
        return "teacher/teacherSeesSchools";
    }

    @GetMapping("/addSchoolsList")
    public String getAddSchoolsList(@RequestParam int teacherId, Model model) {
        model.addAttribute("schoolsForTeacherToAdd", teacherService.getSchoolsForTeacherToAdd(teacherId));
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("teacherFirstName", teacherService.getTeacherFirstName(teacherId));
        model.addAttribute("teacherLastName", teacherService.getTeacherLastName(teacherId));
        return "teacher/teacherAddsSchools";
    }

    @GetMapping("/teacherAddsSchools")
    public String addSchoolToTeacher(@RequestParam int teacherId, @RequestParam int schoolId) {
        teacherService.addSchoolToTeacher(teacherId, schoolId);
        return "redirect:/addSchoolsList?teacherId=" + teacherId;
    }

    @GetMapping("/deleteSchoolFromTeacher")
    public String deleteSchoolFromTeacher(@RequestParam int teacherId, @RequestParam int schoolId) {
        teacherService.deleteSchoolFromTeacher(teacherId, schoolId);
        return "redirect:/teacherSeesSchools?teacherId=" + teacherId;
    }

    @GetMapping("/getClassesForTeacher")
    public String getClassesForTeacher(@RequestParam int teacherId,
                                       @RequestParam int schoolId,
                                       Model model) {
        model.addAttribute("classesList",
                teacherService.getListOfClassesForTeacher(teacherId));
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        model.addAttribute("teacherFirstName", teacherService.getTeacherFirstName(teacherId));
        model.addAttribute("teacherLastName", teacherService.getTeacherLastName(teacherId));
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("schoolId", schoolId);
        return "/teacher/teacherSeeClasses";
    }

    @GetMapping("/addClassesToTeacherList")
    public String getClassesForTeacherToAdd(@RequestParam int teacherId,
                                            @RequestParam int schoolId,
                                            Model model) {
        model.addAttribute("classListForTeacher",
                teacherService.getClassListForTeacherToAdd(schoolId, teacherId));
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        model.addAttribute("teacherFirstName", teacherService.getTeacherFirstName(teacherId));
        model.addAttribute("teacherLastName", teacherService.getTeacherLastName(teacherId));
        return "/teacher/teacherAddClasses";
    }

    @GetMapping("/teacherAddClasses")
    public String addClassToTeacher(@RequestParam int teacherId,
                                    @RequestParam int schoolId,
                                    @RequestParam int classId) {
        teacherService.addClassToTeacher(teacherId, classId);
        return "redirect:/addClassesToTeacherList?teacherId=" + teacherId + "&schoolId=" + schoolId;
    }

    @GetMapping("/deleteClassForTeacher")
    public String deleteClassForTeacher(@RequestParam int teacherId,
                                        @RequestParam int schoolId,
                                        @RequestParam int classId) {
        teacherService.deleteClassForTeacher(teacherId, classId);
        return "redirect:/getClassesForTeacher?teacherId=" + teacherId + "&schoolId=" + schoolId;
    }

}
