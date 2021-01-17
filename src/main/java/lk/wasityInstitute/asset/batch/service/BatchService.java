package lk.wasityInstitute.asset.batch.service;


import lk.wasityInstitute.asset.batch.dao.BatchDao;
import lk.wasityInstitute.asset.batch.entity.Batch;
import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.batch.entity.Batch;
import lk.wasityInstitute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchService implements AbstractService< Batch, Integer> {
    private final BatchDao batchDao;

    public BatchService(BatchDao batchDao) {
        this.batchDao = batchDao;
    }

    public List<Batch> findAll() {
        return batchDao.findAll();
    }

    public Batch findById(Integer id) {
        return batchDao.getOne(id);
    }

    public Batch persist(Batch batch) {
        if(batch.getId()==null){
            batch.setLiveDead(LiveDead.ACTIVE);}
        return batchDao.save(batch);
    }

    public boolean delete(Integer id) {
        Batch batch =  batchDao.getOne(id);
        batch.setLiveDead(LiveDead.STOP);
        batchDao.save(batch);
        return false;
    }

    public List<Batch> search(Batch batch) {
        return null;
    }



}
