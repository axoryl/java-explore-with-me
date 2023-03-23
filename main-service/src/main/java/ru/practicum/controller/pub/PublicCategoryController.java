package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.CategoryService;
import ru.practicum.util.StringTemplate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final String logTemplate = StringTemplate.PUBLIC_CATEGORY_LOG;

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info(String.format(logTemplate + "from: [%d] size: [%d]", "GET ALL", from, size));
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info(String.format(logTemplate + "category id: [%d]", "GET BY ID", catId));
        return categoryService.getCategoryById(catId);
    }
}
