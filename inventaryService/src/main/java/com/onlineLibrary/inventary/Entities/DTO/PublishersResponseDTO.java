package com.onlineLibrary.inventary.Entities.DTO;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import java.util.List;

public class PublishersResponseDTO {

    private List<PublisherDAO> publishers;

    public PublishersResponseDTO(List<PublisherDAO> publishers) {
        this.publishers = publishers;
    }

    public List<PublisherDAO> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<PublisherDAO> publishers) {
        this.publishers = publishers;
    }
}