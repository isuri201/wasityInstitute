package lk.wasityInstitute.asset.teacher.service;


import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.teacher.dao.TeacherDao;
import lk.wasityInstitute.asset.teacher.entity.Teacher;
import lk.wasityInstitute.asset.teacher.entity.Teacher;
import lk.wasityInstitute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService implements AbstractService< Teacher, Integer> {
    private final TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    public Teacher findById(Integer id) {
        return teacherDao.getOne(id);
    }

    public Teacher persist(Teacher teacher) {
        if(teacher.getId()==null){
            teacher.setLiveDead(LiveDead.ACTIVE);}
        return teacherDao.save(teacher);
    }

    public boolean delete(Integer id) {
        Teacher teacher =  teacherDao.getOne(id);
        teacher.setLiveDead(LiveDead.STOP);
        teacherDao.save(teacher);
        return false;
    }

    public List<Teacher> search(Teacher teacher) {
        return null;
    }


    public Teacher lastTeacher() {
        return teacherDao.findFirstByOrderByIdDesc();
    }

    public Teacher lastTeacherOnDB() {return teacherDao.findFirstByOrderByIdDesc();
    }
}
