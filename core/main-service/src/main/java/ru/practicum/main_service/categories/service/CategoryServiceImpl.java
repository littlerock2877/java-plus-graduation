package ru.practicum.main_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.categories.dto.CategoryDto;
import ru.practicum.main_service.categories.dto.NewCategoryDto;
import ru.practicum.main_service.categories.mapper.CategoryMapper;
import ru.practicum.main_service.categories.model.Category;
import ru.practicum.main_service.categories.repository.CategoryRepository;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exception.NotFoundException;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(
                categoryMapper.toModelByNew(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) {
        Category category = getCategoryById(catId);
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer catId) {
        Category category = getCategoryById(catId);
        if (eventRepository.findByCategory(category).isPresent()) {
            throw new InvalidParameterException("Category is related to event");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getCategory(Integer catId) {
        Category category = getCategoryById(catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);

        List<Category> categories = categoryRepository.findAll(page).getContent();
        if (categories.isEmpty()) {
            return List.of();
        } else {
            return categories.stream()
                    .map(categoryMapper::toCategoryDto)
                    .toList();
        }
    }

    private Category getCategoryById(Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
