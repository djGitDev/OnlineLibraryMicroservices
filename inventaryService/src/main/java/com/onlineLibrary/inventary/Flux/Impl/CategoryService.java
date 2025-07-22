package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Persistance.ICategoryBookRepository;
import com.onlineLibrary.inventary.Persistance.ICategoryRepository;
import com.onlineLibrary.inventary.UtilInventaire.IBeansInjectionFactory;

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
