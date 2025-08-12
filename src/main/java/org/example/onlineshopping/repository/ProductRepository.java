package org.example.onlineshopping.repository;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final SessionFactory sessionFactory;

    public List<Product> getAllProducts() {
        Session session = sessionFactory.getCurrentSession();
        List<Product> products = session.createQuery("from Product WHERE Product.quantity > 0").getResultList();
        return products;
    }

    public Optional<Product> getProductById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, id);
        return Optional.ofNullable(product);
    }

    public void updateProductForAdmin(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.update(product);
    }

    public void addProductForAdmin(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.save(product);
    }

}
