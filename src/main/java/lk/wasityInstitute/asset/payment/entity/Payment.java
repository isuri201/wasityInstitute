package lk.wasityInstitute.asset.payment.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.discount.entity.Discount;
import lk.wasityInstitute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Payment" )
public class Payment extends AuditEntity {

    private String tranId;
    private String discountStatus;
    private String amount;
    private String createdTime;
    private String createdDate;
    private String createdBy;


    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;

    @ManyToOne
    private Discount discount;

}
