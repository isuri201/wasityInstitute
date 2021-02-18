package lk.wasity_institute.asset.user_management.dao;

import lk.wasity_institute.asset.user_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer > {
    Role findByRoleName(String roleName);
}
