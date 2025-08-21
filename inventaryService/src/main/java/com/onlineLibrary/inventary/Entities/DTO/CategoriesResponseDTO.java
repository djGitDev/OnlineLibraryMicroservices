package com.onlineLibrary.inventary.Entities.DTO;

import com.onlineLibrary.inventary.Entities.DAO.CategoryDAO;

import java.util.List;

public class CategoriesResponseDTO {
    private List<CategoryDAO> categories;

    public CategoriesResponseDTO(List<CategoryDAO> categories) {
        this.categories = categories;
    }

    public List<CategoryDAO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDAO> categories) {
        this.categories = categories;
    }
}