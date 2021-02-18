package lk.wasity_institute.asset.subject.service;


import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.subject.dao.SubjectDao;
import lk.wasity_institute.asset.subject.entity.Subject;
import lk.wasity_institute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements AbstractService<Subject, Integer> {
    private final SubjectDao subjectDao;

    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public List<Subject> findAll() {
        return subjectDao.findAll();
    }

    public Subject findById(Integer id) {
        return subjectDao.getOne(id);
    }

    public Subject persist(Subject subject) {
        if(subject.getId() ==null){
            subject.setLiveDead(LiveDead.ACTIVE);
        }
        return subjectDao.save(subject);
    }

    public boolean delete(Integer id) {
        Subject subject = subjectDao.getOne(id);
        subject.setLiveDead(LiveDead.STOP);
        subjectDao.save(subject);
        return false;
    }

    public List<Subject> search(Subject subject) {
        return null;
    }



}
