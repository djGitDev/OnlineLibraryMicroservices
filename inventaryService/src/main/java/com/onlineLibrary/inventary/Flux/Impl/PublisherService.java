package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.IPublisherService;
import com.onlineLibrary.inventary.Persistance.IPublisherRepository;
import com.onlineLibrary.inventary.UtilInventaire.IBeansInjectionFactory;

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
