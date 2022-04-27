package com.project.PP312.dao;

import com.project.PP312.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getListRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public void addRole(Role role) {
        if (getRoleByName(role) == null) {
            entityManager.persist(role);
        }
    }

    @Override
    public Role getRoleByName(Role role) {
        return entityManager.createQuery("select r from Role r", Role.class)
                .getResultStream()
                .filter(name -> name.getRole().equals(role.getRole()))
                .findAny()
                .orElse(null);
    }
}
