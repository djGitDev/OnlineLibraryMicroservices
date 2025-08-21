package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;
import com.onlineLibrary.inventary.Entities.DTO.CategoriesResponseDTO;

public interface ICategoryService {
    int generateRetlationBookCategorie(String category, int id);
    CategoriesResponseDTO getCategories();
    CategoryDAO addCategory(String name);
}
