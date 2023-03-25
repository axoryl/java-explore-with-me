package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicCategoryController {

    private final CategoryService categoryService;
    private static final String logTemplate = StringTemplate.PUBLIC_CATEGORY_LOG;

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(required = false, defaultValue = "10") @Positive int size) {
        log.info(String.format(logTemplate, "GET ALL"));
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info(String.format(logTemplate, "GET BY ID"));
        return categoryService.getCategoryById(catId);
    }
}
