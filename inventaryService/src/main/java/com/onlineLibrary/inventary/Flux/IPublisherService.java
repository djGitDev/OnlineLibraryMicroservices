package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Entities.DTO.PublishersResponseDTO;

public interface IPublisherService {
    int getPublisherByName(String publisher);
    PublishersResponseDTO getPublishers();
    PublisherDAO addPublisher(String name);
}
