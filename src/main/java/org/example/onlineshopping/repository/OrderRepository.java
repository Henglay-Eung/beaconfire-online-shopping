package org.example.onlineshopping.repository;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final SessionFactory sessionFactory;

    public void placeAnOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.save(order);
    }

    public void updateAnOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.update(order);
    }

    public Optional<Order> getAnOrderByIdAndUserId(int id, Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Order> query = session.createQuery("from Order o WHERE o.id = :id AND o.user.id = :userId");
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        return Optional.ofNullable(query.getSingleResult());
    }

    public List<Integer> getTopNMostRecentOrderListByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("select o.orderId from Order o WHERE o.user.id = :userId AND o.orderStatus != :status ORDER BY o.datePlaced DESC");
        query.setParameter("userId", userId);
        query.setParameter("status", "Canceled");
        return query.list();
    }

    public List<OrderItem> getOrderItemsByOrderId(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.order.orderId in :orderIdList");
        query.setParameter("orderIdList", orderIdList);
        query.setMaxResults(topN);
        return query.getResultList();
    }

    public List<Integer> getOrderListByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("select o.id from Order o WHERE o.user.id = :userId AND o.orderStatus != :status");
        query.setParameter("userId", userId);
        query.setParameter("status", "Canceled");
        return query.list();
    }

    public List<OrderItem> getTopNFrequentlyPurchasedOrderItemsByOrderId(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.id in :orderIdList ORDER BY oi.quantity DESC");
        query.setParameter("orderIdList", orderIdList);
        query.setMaxResults(topN);
        return query.getResultList();
    }

    public List<Order> getAllOrders() {
        Session session = sessionFactory.getCurrentSession();
        Query<Order> query = session.createQuery("from Order");
        return query.getResultList();
    }

    public Optional<Order> getAnOrderById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query<Order> query = session.createQuery("from Order o where o.id = :id");
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public List<Integer> getAllCompletedOrders() {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("select o.id from Order o where o.orderStatus = 'Completed'");
        return query.getResultList();
    }

    public List<OrderItem> getTopNMostProfitableItems(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.id in :orderIdList ORDER BY oi.purchasedPrice-oi.wholesalePrice DESC");
        query.setParameter("orderIdList", orderIdList);
        query.setMaxResults(topN);
        return query.getResultList();
    }

    public List<OrderItem> getTopNMostPopularItems(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.id in :orderIdList ORDER BY oi.quantity DESC");
        query.setParameter("orderIdList", orderIdList);
        if (topN > 0) {
            query.setMaxResults(topN);
        }

        return query.getResultList();
    }

    public List<Order> getAllOrdersByOrderIds(List<Integer> orderIds) {
        Session session = sessionFactory.getCurrentSession();
        Query<Order> query = session.createQuery("from Order o where o.orderId in :orderIds");
        query.setParameter("orderIds", orderIds);
        return query.getResultList();
    }
}
