package com.onlineLibrary.inventary.Flux.Impl;

import com.onlineLibrary.inventary.Entities.DAO.BookCategoryDAO;
import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;
import com.onlineLibrary.inventary.Entities.DTO.CategoriesResponseDTO;
import com.onlineLibrary.inventary.Flux.ICategoryService;
import com.onlineLibrary.inventary.Persistance.ICategoryBookRepository;
import com.onlineLibrary.inventary.Persistance.ICategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
        int idCategory = findCategoryByNameElseCreate(category);
        BookCategoryDAO relation = new BookCategoryDAO(idBook,idCategory);
        relation = categoryBookRepository.save(relation);
        return relation.getId();
    }

    @Override
    public CategoriesResponseDTO getCategories() {
        List<CategoryDAO> categories = categoryRepository.findAll();
        return new CategoriesResponseDTO(categories);
    }

    @Override
    public CategoryDAO addCategory(String name) {
        Optional<CategoryDAO> existing = categoryRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get(); // retourne la catégorie existante
        }
        CategoryDAO newCategory = new CategoryDAO(name);
        return categoryRepository.save(newCategory); // sauvegarde et retourne le DAO
    }
    @Transactional
    protected int findCategoryByNameElseCreate(String categoryName) {
        Optional<CategoryDAO> optionalCategory = categoryRepository.findByName(categoryName);
        if (optionalCategory.isPresent()) {
            return optionalCategory.get().getId();
        } else {
            CategoryDAO newCategory = new CategoryDAO(categoryName);
            CategoryDAO savedCategory = categoryRepository.save(newCategory);
            return savedCategory.getId();
        }
    }


}
