package com.onlineLibrary.inventary.Persistance;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPublisherRepository extends JpaRepository<PublisherDAO, Integer> {
    Optional<PublisherDAO> findByName(String name);
}