package lk.wasity_institute.asset.school.controller;


import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.school.entity.School;
import lk.wasity_institute.asset.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/school" )
public class SchoolController {
  private final SchoolService schoolService;

  @Autowired
  public SchoolController(SchoolService schoolService) {
    this.schoolService = schoolService;
  }

  private String commonMethod(Model model, boolean addStatus, School school) {
    model.addAttribute("school", school);
    model.addAttribute("addStatus", addStatus);
    return "school/addSchool";
  }

  @GetMapping
  public String schoolPage(Model model) {
    model.addAttribute("schools", schoolService.findAll()
        .stream()
        .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
        .collect(Collectors.toList()));
    return "school/school";
  }


  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable( "id" ) Integer id, Model model) {
    return commonMethod(model, false, schoolService.findById(id));
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonMethod(model, true, new School());
  }

  // Above method support to send data to front end - All List, update, edit
  //Bellow method support to do back end function save, delete, update, search

  @PostMapping( value = {"/save", "/update"} )
  public String addSchool(@Valid @ModelAttribute School school,
                          BindingResult result, Model model) {
    if ( result.hasErrors() ) {
      return commonMethod(model, true, school);
    }
    schoolService.persist(school);
    return "redirect:/school";
  }


  @RequestMapping( value = "/delete/{id}", method = RequestMethod.GET )
  public String delete(@PathVariable Integer id) {
    schoolService.delete(id);
    return "redirect:/school";
  }


}
