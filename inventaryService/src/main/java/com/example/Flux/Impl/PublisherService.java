package com.example.Flux.Impl;

import com.example.Flux.IPublisherService;
import com.example.Persistance.IPublisherRepository;
import com.example.UtilInventaire.IBeansInjectionFactory;

public class PublisherService implements IPublisherService {

    private final IPublisherRepository publisherRepository;

    public PublisherService(IBeansInjectionFactory factory){
        this.publisherRepository = factory.getPublisherRepository(factory);
    }


    @Override
    public int getPublisherByName(String publisher) {
        int idPublisher = publisherRepository.findPublisherByNameElseCreate(publisher);
        return idPublisher;
    }
}
