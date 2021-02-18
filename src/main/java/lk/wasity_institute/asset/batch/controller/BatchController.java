package lk.wasity_institute.asset.batch.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.entity.enums.ClassDay;
import lk.wasity_institute.asset.batch.entity.enums.Grade;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.batch_student.service.BatchStudentService;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.student.service.StudentService;
import lk.wasity_institute.asset.teacher.controller.TeacherController;
import lk.wasity_institute.asset.teacher.service.TeacherService;
import lk.wasity_institute.util.interfaces.AbstractController;
import lk.wasity_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/batch" )
public class BatchController implements AbstractController< Batch, Integer > {
  private final BatchService batchService;
  private final TeacherService teacherService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final StudentService studentService;
  private final BatchStudentService batchStudentService;

  public BatchController(BatchService batchService, TeacherService teacherService,
                         MakeAutoGenerateNumberService makeAutoGenerateNumberService, StudentService studentService,
                         BatchStudentService batchStudentService) {
    this.batchService = batchService;
    this.teacherService = teacherService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.studentService = studentService;
    this.batchStudentService = batchStudentService;
  }


  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("batchs",
                       batchService.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList()));
    return "batch/batch";
  }

  private String commonMethod(Model model, Batch batch, boolean addStatus) {
    model.addAttribute("grades", Grade.values());
    model.addAttribute("classDays", ClassDay.values());
    model.addAttribute("teachers", teacherService.findAll());
    model.addAttribute("batch", batch);
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("subjectUrl", MvcUriComponentsBuilder
        .fromMethodName(TeacherController.class, "findId", "")
        .build()
        .toString());
    return "batch/addBatch";
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonMethod(model, new Batch(), true);
  }

  @GetMapping( "/view/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("batchDetail", batchService.findById(id));
    return "batch/batch-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonMethod(model, batchService.findById(id), false);
  }

  @PostMapping( "/save" )
  public String persist(@Valid @ModelAttribute Batch batch, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonMethod(model, batch, true);
    }

    if ( batch.getId() == null ) {
      Batch batchDb = batchService.findByName(batch.getName());

      if ( batchDb != null ) {
        ObjectError error = new ObjectError("batch",
                                            "This batch is already in the system. ");
        bindingResult.addError(error);
        return commonMethod(model, batch, true);
      }
      // need to create auto generated registration number
      Batch lastBatch = batchService.lastBatchOnDB();
      if ( lastBatch != null ) {
        String lastNumber = lastBatch.getCode().substring(3);
        batch.setCode("SSB" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
      } else {
        batch.setCode("SSB" + makeAutoGenerateNumberService.numberAutoGen(null));
      }
    }

    batchService.persist(batch);
    return "redirect:/batch";

  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    batchService.delete(id);
    return "redirect:/batch";
  }

  @GetMapping( "/{grade}" )
  @ResponseBody
  public MappingJacksonValue findByGrade(@PathVariable( "grade" ) Grade grade) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(batchService.findByGrade(grade));

    SimpleBeanPropertyFilter forBatch = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Batch", forBatch);

    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }

  @GetMapping( "/{grade}/{id}" )
  @ResponseBody
  public MappingJacksonValue findByGradeAndStudent(@PathVariable( "grade" ) Grade grade,
                                                   @PathVariable( "id" ) Integer id) {
    List< BatchStudent > batchStudents = batchStudentService.findByStudent(studentService.findById(id));
    List< Batch > notAssignBatch = new ArrayList<>();
    List< Batch > batches = batchService.findByGrade(grade);
    for ( Batch batch : batches )
      for ( BatchStudent batchStudent : batchStudents )
        if ( !batchStudent.getBatch().equals(batch) ) notAssignBatch.add(batch);

    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(notAssignBatch);

    SimpleBeanPropertyFilter forBatch = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Batch", forBatch);

    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }

  @GetMapping( "/id/{id}" )
  @ResponseBody
  public MappingJacksonValue findById(@PathVariable( "id" ) Integer id) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(batchService.findById(id).getTeacher());

    SimpleBeanPropertyFilter forTeacher = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "firstName", "fee");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Teacher", forTeacher);

    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }
}
