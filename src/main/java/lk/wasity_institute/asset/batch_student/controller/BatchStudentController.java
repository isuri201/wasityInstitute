package lk.wasity_institute.asset.batch_student.controller;

import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.service.BatchService;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.batch_student.service.BatchStudentService;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.asset.student.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/batchStudent" )
public class BatchStudentController {
  private final BatchService batchService;
  private final BatchStudentService batchStudentService;
  private final StudentService studentService;

  public BatchStudentController(BatchService batchService, BatchStudentService batchStudentService,
                                StudentService studentService) {
    this.batchService = batchService;
    this.batchStudentService = batchStudentService;
    this.studentService = studentService;
  }

  @GetMapping
  public String allActiveBatch(Model model) {
    List< Batch > batchList = batchService.findAll()
        .stream()
        .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
        .collect(Collectors.toList());

    List< Batch > batches = new ArrayList<>();
    for ( Batch batch : batchList ) {
      batch.setCount(batchStudentService.countByBatch(batch));
      batches.add(batch);
    }
    model.addAttribute("batches", batches);
    return "batchStudent/batchStudent";
  }

  @PostMapping( "/batch/{id}" )
  public String studentAddBatch(@PathVariable( "id" ) Integer id, Model model) {
    Batch batch = batchService.findById(id);
    model.addAttribute("batchDetail", batch);
    model.addAttribute("teacherDetail", batch.getTeacher());
    //already registered student on this batch
    List< Student > registeredStudent = new ArrayList<>();
    batch.getBatchStudents().forEach(x -> registeredStudent.add(x.getStudent()));
    //not registered student on this batch
    List< Student> notRegisteredStudent = studentService.findByGrade(batch.getGrade())
        .stream()
        .filter(x->!registeredStudent.contains(x))
        .collect(Collectors.toList());
    model.addAttribute("students", registeredStudent);
    model.addAttribute("notRegisteredStudent",notRegisteredStudent );
    model.addAttribute("student", new BatchStudent());
    model.addAttribute("studentRemoveBatch", true);
    return "batchStudent/addBatchStudent";
  }

  @GetMapping("/removeBatch")
  public String removeStudentFromBatch(@ModelAttribute BatchStudent batchStudent){
    BatchStudent batchStudentDB = batchStudentService.findByStudentAndBatch(batchStudent.getStudent(), batchStudent.getBatch());
    batchStudentDB.setLiveDead(LiveDead.STOP);
    batchStudentService.persist(batchStudent);
    return "redirect:/batchStudent/batch/"+ batchStudentDB.getId();
  }

}
