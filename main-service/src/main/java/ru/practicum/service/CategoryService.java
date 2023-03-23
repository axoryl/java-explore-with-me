package ru.practicum.service;

import ru.practicum.dto.category.CategoryCreationDto;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto saveCategory(CategoryCreationDto category);

    CategoryDto updateCategory(Long catId, CategoryDto category);

    void deleteCategory(Long catId);
}
