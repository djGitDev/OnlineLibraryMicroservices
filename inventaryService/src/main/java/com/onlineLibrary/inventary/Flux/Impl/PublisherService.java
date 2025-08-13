package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Entities.DAO.PublisherDAO;
import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IPublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PublisherService implements IPublisherService {

    private final IPublisherRepository publisherRepository;

    @Autowired
    public PublisherService(IPublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }


    @Override
    public int getPublisherByName(String publisher) {
        int idPublisher = findPublisherByNameElseCreate(publisher);
        return idPublisher;
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
