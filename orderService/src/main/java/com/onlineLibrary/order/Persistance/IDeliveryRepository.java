package com.onlineLibrary.order.Persistance;

import com.onlineLibrary.order.Entities.DAO.DeliveryDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDeliveryRepository extends JpaRepository<DeliveryDAO, Integer> {
    Optional<DeliveryDAO> findDeliveryByOrderId(int orderId);
}
