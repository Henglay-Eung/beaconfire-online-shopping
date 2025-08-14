package org.example.onlineshopping.repository;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Permission;
import org.example.onlineshopping.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final SessionFactory sessionFactory;
    public Optional<User> loadUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User u where u.username = :username");
        query.setParameter("username", username);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

    public User registerUser(String username, String email, String password, List<Permission> permissionList) {
        Session session = sessionFactory.getCurrentSession();
        User user = User.builder().username(username).email(email).password(password).permissions(permissionList).build();
        session.save(user);
        return user;
    }

    public void updateUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    public boolean isUsernameOrEmailAvailable(String username, String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("SELECT count(u) FROM User u WHERE u.username = :username OR u.email = :email");
        query.setParameter("username", username);
        query.setParameter("email", email);
        long count = query.getSingleResult();
        return count == 0;
    }

    public Optional<User> loadUserByUsernameWithWatchList(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User u LEFT JOIN fetch u.watchlist where u.username = :username");
        query.setParameter("username", username);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

    public Optional<User> loadUserByUsernameWithOrderList(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User u LEFT JOIN fetch u.orderList where u.username = :username");
        query.setParameter("username", username);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

}
