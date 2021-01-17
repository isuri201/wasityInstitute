package lk.wasityInstitute.asset.payment.service;



import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.payment.dao.PaymentDao;
import lk.wasityInstitute.asset.payment.entity.Payment;
import lk.wasityInstitute.asset.payment.entity.Payment;
import lk.wasityInstitute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements AbstractService< Payment, Integer> {
    private final PaymentDao paymentDao;

    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public List<Payment> findAll() {
        return paymentDao.findAll();
    }

    public Payment findById(Integer id) {
        return paymentDao.getOne(id);
    }

    public Payment persist(Payment payment) {
        if(payment.getId()==null){
            payment.setLiveDead(LiveDead.ACTIVE);}
        return paymentDao.save(payment);
    }

    public boolean delete(Integer id) {
        Payment payment =  paymentDao.getOne(id);
        payment.setLiveDead(LiveDead.STOP);
        paymentDao.save(payment);
        return false;
    }

    public List<Payment> search(Payment payment) {
        return null;
    }



}
