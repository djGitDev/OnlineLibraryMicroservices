package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Persistance.ICategoryBookRepository;
import com.onlineLibrary.inventary.Persistance.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryService implements ICategoryService {

    private  ICategoryRepository categoryRepository;
    private  ICategoryBookRepository categoryBookRepository;

    @Autowired
    public CategoryService(ICategoryRepository categoryRepository,ICategoryBookRepository categoryBookRepository){
        this.categoryRepository = categoryRepository;
        this.categoryBookRepository = categoryBookRepository;
    }

    @Override
    public int generateRetlationBookCategorie(String category, int idBook) {
        int idCategory = categoryRepository.findCategoryByNameElseCreate(category);
        return  categoryBookRepository.createRelation(idBook,idCategory);
    }
}
