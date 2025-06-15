package ru.practicum.main_service.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),
                category.getName());
    }

    public Category toModelByNew(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public Category toModel(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}
