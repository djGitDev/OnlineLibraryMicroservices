package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Entities.DTO.PublishersResponseDTO;

import java.util.Optional;

public interface IPublisherService {
    PublishersResponseDTO getPublishers();
    PublisherDAO addPublisher(String name);
    Optional<PublisherDAO> getPublisherById(int publisherId);
}
