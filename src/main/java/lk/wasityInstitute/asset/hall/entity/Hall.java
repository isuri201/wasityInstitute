package lk.wasityInstitute.asset.hall.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasityInstitute.asset.batch.entity.Batch;
import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.hall.entity.enums.HallCondition;
import lk.wasityInstitute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Hall")
public class Hall extends AuditEntity {
    private String hallName;
    private String number;
    private String seatCount;
    private String hallLocation;

    @Enumerated( EnumType.STRING)
    private HallCondition hallCondition;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

    @ManyToMany(mappedBy = "halls")
    private List< Batch > batches;


}
