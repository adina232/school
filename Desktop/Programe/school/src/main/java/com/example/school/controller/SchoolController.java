package com.example.school.controller;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.SchoolDto;
import com.example.school.entity.ClassOfStudents;
import com.example.school.entity.School;
import com.example.school.service.SchoolService;
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
public class SchoolController {
    private final SchoolService schoolService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/schoolForm")
    public ModelAndView getSchoolForm() {
        ModelAndView modelAndView = new ModelAndView("/school/schoolForm");
        modelAndView.addObject("school", new School());
        return modelAndView;
    }

    @PostMapping("/schoolForm")
    public String addSchool(@ModelAttribute SchoolDto schoolDto) {
        schoolService.addSchool(schoolDto);
        return "redirect:/schools";
    }

    @GetMapping("/schools")
    public String getSchools(Model model) {
        model.addAttribute("schools", schoolService.getSchools());
        return "school/schools";
    }

    @GetMapping("/deleteSchool")
    public String deleteSchool(@RequestParam int schoolId) {
        schoolService.deleteSchool(schoolId);
        return "redirect:/schools";
    }

    @GetMapping("/editSchool")
    public String modifySchool(@RequestParam int schoolId, Model model) {
        model.addAttribute("schoolDto", schoolService.getSchool(schoolId));
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        return "school/schoolEdit";
    }

    @PostMapping("/editSchool")
    public String editSchool(@ModelAttribute SchoolDto schoolDto) {
        schoolService.editSchool(schoolDto);
        return "redirect:/schools";
    }

    @GetMapping("/seeTeachersForSchool")
    public String seeTeachersForSchool(@RequestParam int schoolId, Model model) {
        model.addAttribute("teacherDtoList", schoolService.getTeachersInSchool(schoolId));
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        return "school/schoolSeeTeachers";
    }

    @GetMapping("/deleteTeacherFromSeeTeachers")
    public String deleteTeacherFromSeeTeachers(@RequestParam int teacherId, @RequestParam int schoolId) {
        schoolService.deleteTeacherFromSeeTeachers(teacherId, schoolId);
        return "redirect:/seeTeachersForSchool?schoolId=" + schoolId;
    }

    @GetMapping("/schoolAddTeacher")
    public String getTeachersForSchool(@RequestParam int schoolId, Model model) {
        model.addAttribute("teacherListForSchool", schoolService.getTeachersForSchool(schoolId));
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        return "school/schoolAddTeacher";
    }

    @GetMapping("/addTeacherToSchool")
    public String addTeacherToSchool(@RequestParam int schoolId, @RequestParam int teacherId) {
        schoolService.addTeacherToSchool(schoolId, teacherId);
        return "redirect:/schoolAddTeacher?schoolId=" + schoolId;
    }

    @GetMapping("/classesForSchool")
    public String getClassList(@RequestParam int schoolId, Model model) {
        model.addAttribute("classListForSchool", schoolService.getClassListForSchool(schoolId));
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("schoolName", schoolService.getSchool(schoolId).getSchoolName());
        return "school/classes";
    }

    @GetMapping("/addClassToSchool")
    public ModelAndView addClass(@RequestParam int schoolId) {
        ModelAndView modelAndView = new ModelAndView("/school/addClassToSchool");
        modelAndView.addObject("classOfStudents", new ClassOfStudents());
        modelAndView.addObject("options", schoolService.getTeachersOptions());
        modelAndView.addObject("schoolId", schoolId);
        return modelAndView;
    }

    @PostMapping("/addClassToSchool")
    public String addClassToSchool(@ModelAttribute ClassOfStudentsDto classOfStudentsDto) {
        schoolService.addClassOfStudents(classOfStudentsDto);
        return "redirect:/classesForSchool?schoolId=" + classOfStudentsDto.getSchoolId();
    }

    @GetMapping("/deleteClassForSchool")
    public String deleteClassForSchool(@RequestParam int classId) {
        int schoolId = schoolService.getSchoolId(classId);
        schoolService.deleteClassForSchool(classId);
        return "redirect:/classesForSchool?schoolId=" + schoolId;
    }

    @GetMapping("/studentsInClass")
    public String getStudentsInClass(@RequestParam int classId, Model model) {
        model.addAttribute("studentListInClass", schoolService.getStudentsInClass(classId));
        model.addAttribute("className", schoolService.getClassName(classId));
        model.addAttribute("classId", classId);
        model.addAttribute("schoolId", schoolService.getSchoolId(classId));
        return "school/studentsInClass";
    }
    @GetMapping("/deleteStudentForClass")
    public String deleteStudentForClass(@RequestParam int classId, @RequestParam int studentId) {
        schoolService.deleteStudentForClass(studentId);
        return "redirect:/studentsInClass?classId=" + classId;
    }

    @GetMapping("/addStudentsToClassList")
    public String addStudentsToClassList(@RequestParam int classId, Model model) {
        model.addAttribute("studentsToAddToClassList",
                schoolService.getListOfStudentsToAddToClass(classId));
        model.addAttribute("className", schoolService.getClassName(classId));
        model.addAttribute("classId", classId);
        return "school/classAddStudent";
    }

    @GetMapping("/addStudentToClass")
    public String addStudentToClass(@RequestParam int classId, @RequestParam int studentId) {
        schoolService.addStudentToClass(classId, studentId);
        return "redirect:/addStudentsToClassList?classId=" + classId;
    }

}
