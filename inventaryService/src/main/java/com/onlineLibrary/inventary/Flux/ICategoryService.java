package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;
import com.onlineLibrary.inventary.Entities.DTO.CategoriesResponseDTO;

import java.util.Optional;

public interface ICategoryService {
    void generateRetlationBookCategorie(String category, int id);
    CategoriesResponseDTO getCategories();
    CategoryDAO addCategory(String name);
    void removeRelationsByBookId(int id);
    Optional<CategoryDAO> getCategoryById(int categoryId);
}
