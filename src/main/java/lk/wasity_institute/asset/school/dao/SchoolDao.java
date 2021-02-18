package lk.wasity_institute.asset.school.dao;


import lk.wasity_institute.asset.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolDao extends JpaRepository< School, Integer> {
}
