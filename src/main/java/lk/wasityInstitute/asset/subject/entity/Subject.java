package lk.wasityInstitute.asset.subject.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.wasityInstitute.asset.batch.entity.Batch;
import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.student.entity.Student;
import lk.wasityInstitute.asset.teacher.entity.Teacher;
import lk.wasityInstitute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Subject" )
public class Subject extends AuditEntity {

    private String code;

    private String name;

    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;

    @OneToMany(mappedBy = "subject")
    private List< Batch > batches;

    @ManyToMany(mappedBy = "subjects")
    private List< Student > students;

    @OneToMany(mappedBy = "subject")
    private List< Teacher > teachers;


}
