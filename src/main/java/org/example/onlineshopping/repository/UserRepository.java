package org.example.onlineshopping.repository;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final SessionFactory sessionFactory;
    public Optional<User> loadUserByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        User user =  session.get(User.class, username);
        return Optional.ofNullable(user);
    }

    public void registerUser(String username, String email, String password) {
        Session session = sessionFactory.getCurrentSession();

        User user = User.builder().username(username).email(email).password(password).build();
        session.persist(user);
    }

    public void updateUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    public boolean isUsernameOrEmailAvailable(String username, String email) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("SELECT count(u) FROM User u WHERE User.username = :username OR User.email = :email");
        query.setParameter("username", username);
        query.setParameter("email", email);
        int count = query.getSingleResult();
        return count == 0;
    }
}
