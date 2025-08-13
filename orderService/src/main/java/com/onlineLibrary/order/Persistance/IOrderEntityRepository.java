package com.onlineLibrary.order.Persistance;

import com.onlineLibrary.order.Entities.DAO.OrderDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderEntityRepository extends JpaRepository<OrderDAO, Integer> {

}
