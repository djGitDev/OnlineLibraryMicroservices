package com.example.Flux.Impl;

import com.example.Flux.ICategoryService;
import com.example.Persistance.ICategoryBookRepository;
import com.example.Persistance.ICategoryRepository;
import com.example.UtilInventaire.IBeansInjectionFactory;

public class CategoryService implements ICategoryService {

    private  ICategoryRepository categoryRepository;
    private  ICategoryBookRepository categoryBookRepository;


    public CategoryService(IBeansInjectionFactory factory){
        this.categoryRepository = factory.getCategoryRepository(factory);
        this.categoryBookRepository = factory.getCategoryBookRepository(factory);
    }

    @Override
    public int generateRetlationBookCategorie(String category, int idBook) {
        int idCategory = categoryRepository.findCategoryByNameElseCreate(category);
        return  categoryBookRepository.createRelation(idBook,idCategory);
    }
}
