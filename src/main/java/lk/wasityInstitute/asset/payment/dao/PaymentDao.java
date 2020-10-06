package lk.wasityInstitute.asset.payment.dao;


import lk.wasityInstitute.asset.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDao extends JpaRepository< Payment, Integer> {

}
