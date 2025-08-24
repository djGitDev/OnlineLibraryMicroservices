package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Entities.DTO.PublishersResponseDTO;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IPublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PublisherService implements IPublisherService {

    private final IPublisherRepository publisherRepository;

    @Autowired
    public PublisherService(IPublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }


    @Override
    public PublishersResponseDTO getPublishers() {
        List<PublisherDAO> publishers = publisherRepository.findAll();
        return new PublishersResponseDTO(publishers);
    }

    @Override
    public PublisherDAO addPublisher(String name) {
        Optional<PublisherDAO> existing = publisherRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get(); // retourne l'éditeur existant
        }
        PublisherDAO newPublisher = new PublisherDAO(name);
        return publisherRepository.save(newPublisher); // sauvegarde et retourne le DAO
    }

    @Override
    public Optional<PublisherDAO> getPublisherById(int publisherId) {
        return publisherRepository.findById(publisherId);
    }

    @Transactional
    public int findPublisherByNameElseCreate(String publisherName) {
        Optional<PublisherDAO> optionalPublisher = publisherRepository.findByName(publisherName);
        if (optionalPublisher.isPresent()) {
            return optionalPublisher.get().getId();
        } else {
            PublisherDAO newPublisher = new PublisherDAO(publisherName);
            PublisherDAO savedPublisher = publisherRepository.save(newPublisher);
            return savedPublisher.getId();
        }
    }
}
