package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryCreationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CategoryMapper.mapToCategory;
import static ru.practicum.mapper.CategoryMapper.mapToCategoryDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories(final int from, final int size) {
        final var pageable = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(final Long catId) {
        final var category = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found",
                        "Incorrectly made request.")
        );
        return mapToCategoryDto(category);
    }

    @Transactional
    @Override
    public CategoryDto saveCategory(final CategoryCreationDto category) {
        return mapToCategoryDto(categoryRepository.save(mapToCategory(category)));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(final Long catId, final CategoryDto category) {
        final var categoryToUpdate = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found",
                        "The required object was not found."));
        categoryToUpdate.setName(category.getName());
        categoryRepository.save(categoryToUpdate);

        return mapToCategoryDto(categoryToUpdate);
    }

    @Transactional
    @Override
    public void deleteCategory(final Long catId) {
        categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found",
                        "The required object was not found."));
        categoryRepository.deleteById(catId);
    }
}
