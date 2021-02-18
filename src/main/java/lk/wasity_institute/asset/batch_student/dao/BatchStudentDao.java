package lk.wasity_institute.asset.batch_student.dao;

import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchStudentDao extends JpaRepository< BatchStudent, Integer> {
  int countByBatch(Batch batch);

  List< BatchStudent> findByStudent(Student student);

  BatchStudent findByStudentAndBatch(Student student, Batch batch);

  List< BatchStudent> findByBatch(Batch batch);
}
