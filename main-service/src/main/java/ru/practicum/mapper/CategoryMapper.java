package ru.practicum.mapper;

import ru.practicum.dto.category.CategoryCreationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.category.Category;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category mapToCategory(CategoryCreationDto category) {
        return Category.builder()
                .name(category.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
