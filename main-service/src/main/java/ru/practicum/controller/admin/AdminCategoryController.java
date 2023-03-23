package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryCreationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.CategoryService;
import ru.practicum.util.StringTemplate;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final String logTemplate = StringTemplate.ADMIN_CATEGORY_LOG;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@Valid @RequestBody CategoryCreationDto category) {
        log.info(String.format(logTemplate + "category: [%s]", "SAVE", category));
        return categoryService.saveCategory(category);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto category) {
        log.info(String.format(logTemplate + "category id: [%d] category: [%s]", "UPDATE", catId, category));
        return categoryService.updateCategory(catId, category);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info(String.format(logTemplate + "category id: [%d]", "DELETE", catId));
        categoryService.deleteCategory(catId);
    }
}
