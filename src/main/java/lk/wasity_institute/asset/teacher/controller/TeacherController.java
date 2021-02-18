package lk.wasity_institute.asset.teacher.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.common_asset.model.enums.Gender;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.subject.service.SubjectService;
import lk.wasity_institute.asset.teacher.entity.Teacher;
import lk.wasity_institute.asset.teacher.service.TeacherService;
import lk.wasity_institute.util.interfaces.AbstractController;
import lk.wasity_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
public class TeacherController implements AbstractController<Teacher, Integer> {
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final BatchService batchService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;


    public TeacherController(TeacherService teacherService, SubjectService subjectService, BatchService batchService,MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.batchService = batchService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("teachers", teacherService.findAll()
                          .stream()
                          .filter(x->x.getLiveDead().equals(LiveDead.ACTIVE))
                          .collect(Collectors.toList()));
        return "teacher/teacher";
    }

    @GetMapping("/add")
    public String form(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("gender", Gender.values());
        model.addAttribute("addStatus",true);
        model.addAttribute("subjects",subjectService.findAll()
            .stream().filter(x->x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList()));
        return "teacher/addTeacher";
    }

    @GetMapping("/view/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("teacherDetail", teacherService.findById(id));
        return "teacher/teacher-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("teacher", teacherService.findById(id));
        model.addAttribute("gender", Gender.values());
        model.addAttribute("addStatus",false);
        model.addAttribute("batches",batchService.findAll());
        model.addAttribute("subjects",subjectService.findAll());
        return "teacher/addTeacher";
    }

    @PostMapping("/save")
    public String persist(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teacher", teacher);
            model.addAttribute("gender", Gender.values());
            model.addAttribute("addStatus",true);
            model.addAttribute("batches",batchService.findAll());
            model.addAttribute("subjects",subjectService.findAll());
            return "teacher/addTeacher";
        }
        if ( teacher.getId() == null ) {

            // need to create auto generated registration number
            Teacher lastTeacher = teacherService.lastTeacherOnDB();
            //registration number format => ST200001
            if ( lastTeacher != null ) {
                String lastNumber = lastTeacher.getRegistrationId().substring(3);
                teacher.setRegistrationId("SST" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
            } else {
                teacher.setRegistrationId("SST" + makeAutoGenerateNumberService.numberAutoGen(null
            ));
            }

        }
        teacherService.persist(teacher);
        return "redirect:/teacher";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        teacherService.delete(id);
        return "redirect:/teacher";
    }
    @GetMapping( "/{id}" )
    @ResponseBody
    public MappingJacksonValue findId(@PathVariable Integer id) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(teacherService.findById(id).getSubject());
        SimpleBeanPropertyFilter simpleBeanPropertyFilterOne = SimpleBeanPropertyFilter
            .filterOutAllExcept("id", "name","fee");

        FilterProvider filters = new SimpleFilterProvider()
            .addFilter("Subject", simpleBeanPropertyFilterOne);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

}
