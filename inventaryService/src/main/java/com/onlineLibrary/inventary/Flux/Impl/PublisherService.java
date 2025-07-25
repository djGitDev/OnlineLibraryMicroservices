package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IPublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PublisherService implements IPublisherService {

    private final IPublisherRepository publisherRepository;

    @Autowired
    public PublisherService(IPublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }


    @Override
    public int getPublisherByName(String publisher) {
        int idPublisher = publisherRepository.findPublisherByNameElseCreate(publisher);
        return idPublisher;
    }
}
