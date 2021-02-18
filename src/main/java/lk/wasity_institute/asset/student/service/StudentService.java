package lk.wasity_institute.asset.student.service;


import lk.wasity_institute.asset.batch.entity.enums.Grade;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.student.dao.StudentDao;
import lk.wasity_institute.asset.student.entity.Student;
import lk.wasity_institute.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService implements AbstractService< Student, Integer > {
    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List< Student > findAll() {
        return studentDao.findAll();
    }

    public Student findById(Integer id) {
        return studentDao.getOne(id);
    }

    public Student persist(Student student) {
        if(student.getId()==null){
            student.setLiveDead(LiveDead.ACTIVE);
        }
        return studentDao.save(student);
    }

    public boolean delete(Integer id) {
      Student student = studentDao.getOne(id);
      student.setLiveDead(LiveDead.STOP);
      studentDao.save(student);
        return false;
    }

    public List< Student > search(Student student) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Student > studentExample = Example.of(student, matcher);
        return studentDao.findAll(studentExample);
    }

    public Student lastStudentOnDB() {
        return studentDao.findFirstByOrderByIdDesc();
    }

  public List< Student > findByGrade(Grade grade) {
      return studentDao.findByGrade(grade);
  }
}
