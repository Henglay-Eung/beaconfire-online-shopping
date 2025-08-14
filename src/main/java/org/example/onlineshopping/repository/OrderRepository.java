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
        Query<Integer> query = session.createQuery("select o.orderId from Order o WHERE o.user.id = :userId AND o.orderStatus = :status ORDER BY o.datePlaced DESC");
        query.setParameter("userId", userId);
        query.setParameter("status", "Completed");
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
        Query<Integer> query = session.createQuery("select o.id from Order o WHERE o.user.id = :userId AND o.orderStatus = :status");
        query.setParameter("userId", userId);
        query.setParameter("status", "Completed");
        return query.list();
    }

    public List<Integer> getTopNFrequentOrderItemIdList(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("" +
                "select oi.itemId from OrderItem oi WHERE oi.order.id IN :orderIdList " +
                "group by oi.itemId order by COUNT(oi.order.id) DESC"
        );
        query.setParameter("orderIdList", orderIdList);
        return query.list();
    }

    public List<OrderItem> getTopNFrequentlyPurchasedOrderItemsByOrderIds(List<Integer> orderItemIdList) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery(
                "from OrderItem oi WHERE oi.itemId in :orderItemIdList"
        );
        query.setParameter("orderItemIdList", orderItemIdList);
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
        Query<Integer> query = session.createQuery("select o.orderId from Order o where o.orderStatus = 'Completed'");
        return query.getResultList();
    }

    public List<OrderItem> topProfitableProductIdList(List<Integer> productIdList, List<Integer> completedOrderIds) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.product.id in :productIdList AND oi.order.id in :completedOrderIds");
        query.setParameter("productIdList", productIdList);
        query.setParameter("completedOrderIds", completedOrderIds);
        return query.getResultList();
    }

    public List<OrderItem> getTopNMostPopularItems(List<Integer> orderIdList) {
        Session session = sessionFactory.getCurrentSession();
        Query<OrderItem> query = session.createQuery("from OrderItem oi WHERE oi.id in :orderIdList ORDER BY oi.quantity DESC");
        query.setParameter("orderIdList", orderIdList);
        return query.getResultList();
    }

    public List<Order> getAllOrdersByOrderIds(List<Integer> orderIds) {
        Session session = sessionFactory.getCurrentSession();
        Query<Order> query = session.createQuery("from Order o where o.orderId in :orderIds");
        query.setParameter("orderIds", orderIds);
        return query.getResultList();
    }

    public List<Integer> getTopNProfitableOrderItemIdList(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery("" +
                "select oi.product.id from OrderItem oi WHERE oi.order.id IN :orderIdList " +
                "group by oi.product.id order by sum(oi.purchasedPrice - oi.wholesalePrice) DESC"
        );
        query.setParameter("orderIdList", orderIdList);
        query.setMaxResults(topN);
        return query.list();
    }

    public List<Integer> getTopNPopularOrderItemIdList(List<Integer> orderIdList, int topN) {
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(
                "select oi.itemId from OrderItem oi WHERE oi.order.id IN :orderIdList " +
                "group by oi.itemId order by SUM(oi.quantity) DESC"
        );
        query.setParameter("orderIdList", orderIdList);
        query.setMaxResults(topN);
        return query.list();
    }

    public Long getTotalOfItemsSold(List<Integer> orderIdList) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(
                "select sum(oi.quantity) from OrderItem oi WHERE oi.order.id IN :orderIdList "
        );
        query.setParameter("orderIdList", orderIdList);
        return query.getSingleResult();
    }
}
