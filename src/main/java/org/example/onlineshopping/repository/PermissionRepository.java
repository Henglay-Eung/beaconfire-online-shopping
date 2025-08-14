package org.example.onlineshopping.repository;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Permission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class PermissionRepository {
    private final SessionFactory sessionFactory;

    public long countPermission() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Permission> root = cq.from(Permission.class);
        cq.select(cb.count(root));
        Query<Long> query = session.createQuery(cq);
//        Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Permission p");
        return query.getSingleResult();
    }

    public Permission addPermission(Permission permission) {
        Session session = sessionFactory.getCurrentSession();
        session.save(permission);
        return permission;
    }

    public Permission getPermission(String value) {
        Session session = sessionFactory.getCurrentSession();
        Query<Permission> query = session.createQuery("from Permission p where p.value = :value");
        query.setParameter("value", value);
        return query.getSingleResult();
    }
}

