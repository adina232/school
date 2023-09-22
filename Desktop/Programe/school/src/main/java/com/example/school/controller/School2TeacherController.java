package com.example.school.controller;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.StudentDto;
import com.example.school.service.School2TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class School2TeacherController {
    private final School2TeacherService school2TeacherService;

    @GetMapping("/schoolsAndTeachers")
    public String getSchoolsAndTeachers(Model model) {
        model.addAttribute("school2TeacherListDto", school2TeacherService.getList());
        return "schoolsAndTeachers";
    }

    @GetMapping("/subjectsForSchool2Teacher")
    public String getSubjectsForSchool2Teacher(@RequestParam int school2TeacherId, Model model) {
        model.addAttribute("subjectListForS2T",
                school2TeacherService.getSubjectsForSchool2Teacher(school2TeacherId));
        model.addAttribute("school2TeacherId", school2TeacherId);
        model.addAttribute("schoolName", school2TeacherService.
                getSchoolName(school2TeacherId));
        model.addAttribute("teacherName", school2TeacherService.
                getTeacherName(school2TeacherId));
        return "subjectsForSchool2Teacher";
    }

    @GetMapping("/classesForSubjects")
    public String getClassesForSubjects(@RequestParam int school2TeacherId, int subjectId, Model model) {
        List<ClassOfStudentsDto> classesForSubjects = school2TeacherService.getClassesForSubjects(school2TeacherId, subjectId);
        model.addAttribute("classListForSubject", classesForSubjects);
        model.addAttribute("schoolName",
                school2TeacherService.getSchoolName(school2TeacherId));
        model.addAttribute("teacherName",
                school2TeacherService.getTeacherName(school2TeacherId));
        model.addAttribute("subjectName",
                school2TeacherService.getSubjectName(subjectId));
        model.addAttribute("school2TeacherId", school2TeacherId);
        model.addAttribute("subjectId", subjectId);
        return "classesForSchool2Teacher";
    }

    @GetMapping("/deleteClassForSubject")
    public String deleteClassForSubject(@RequestParam int classId,
                                        @RequestParam int subjectId,
                                        @RequestParam int school2TeacherId,
                                        Model model) {
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("school2TeacherId", school2TeacherId);
        school2TeacherService.deleteClassForSubject(classId, subjectId);
        return "redirect:/classesForSubjects?school2TeacherId=" + school2TeacherId + "&subjectId="
                + subjectId;
    }

    @GetMapping("/addClassToSubjectList")
    public String addClassToSubjectList(@RequestParam int school2TeacherId,
                                        @RequestParam int subjectId,
                                        Model model) {
        model.addAttribute("classListForSubjectToAdd",
                school2TeacherService.addClassToSubjectList(school2TeacherId, subjectId));
        model.addAttribute("schoolName",
                school2TeacherService.getSchoolName(school2TeacherId));
        model.addAttribute("teacherName",
                school2TeacherService.getTeacherName(school2TeacherId));
        model.addAttribute("subjectName",
                school2TeacherService.getSubjectName(subjectId));
        model.addAttribute("school2TeacherId", school2TeacherId);
        model.addAttribute("subjectId", subjectId);
        return "school2TeacherAddClassToSubject";
    }

    @GetMapping("/addClassToSubject")
    public String addClassToSubject(@RequestParam int school2TeacherId,
                                    @RequestParam int subjectId,
                                    @RequestParam int classId) {
        school2TeacherService.addClassToSubject(subjectId, classId);
        return "redirect:/addClassToSubjectList?school2TeacherId=" + school2TeacherId + "&subjectId="
                + subjectId;
    }

    @GetMapping("/catalog")
    public String getCatalog(@RequestParam int school2TeacherId,
                             @RequestParam int subjectId,
                             @RequestParam int classId,
                             Model model) {
        model.addAttribute("studentListForCatalog",
                school2TeacherService.getStudentListForCatalog(subjectId, classId));
        model.addAttribute("schoolName",
                school2TeacherService.getSchoolName(school2TeacherId));
        model.addAttribute("teacherName",
                school2TeacherService.getTeacherName(school2TeacherId));
        model.addAttribute("subjectName",
                school2TeacherService.getSubjectName(subjectId));
        model.addAttribute("className",
                school2TeacherService.getClassName(classId));
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("school2TeacherId", school2TeacherId);
        model.addAttribute("classId", classId);
        return "school2TeacherCatalog";
    }

    @PostMapping("/addGrade")
    public String addGrade(@RequestParam int school2TeacherId,
                           @RequestParam int subjectId,
                           @RequestParam int classId,
                           @ModelAttribute StudentDto studentDto,
                           Model model) {
        school2TeacherService.addGrade(subjectId, studentDto.getStudentId(),
                studentDto.getGrade());
        model.addAttribute("school2TeacherId", school2TeacherId);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("classId", classId);
        return "redirect:/catalog?school2TeacherId=" + school2TeacherId + "&subjectId=" + subjectId +
                "&classId=" + classId;
    }
}
