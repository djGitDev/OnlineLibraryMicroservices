package com.onlineLibrary.order.Persistance;

import com.onlineLibrary.order.Entities.DAO.OrderLineDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IOrderLineRepository extends JpaRepository<OrderLineDAO, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE OrderLineDAO ol SET ol.deliveryStatut = 'DELIVERED' WHERE ol.orderId = :orderId")
    int markAsDeliveredByOrderId(int orderId);
    Optional<List<OrderLineDAO>> findByOrderId(int orderId);
}
