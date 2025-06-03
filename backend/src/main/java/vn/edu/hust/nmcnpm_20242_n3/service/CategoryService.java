package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.CategoryDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.entity.Category;
import vn.edu.hust.nmcnpm_20242_n3.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        
        return categories.stream()
                .map(this::convertToDTO)
                .sorted((java.util.Comparator.comparing(CategoryDTO::getId)))
                .toList();
    }

    public Optional<CategoryDTO> findByName(String name) {
        return categoryRepository.findByName(name)
                .map(this::convertToDTO);
    }

    public CategoryDTO findById(int id) {
        return categoryRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    public CategoryDTO addCategory(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Category with name " + dto.getName() + " already exists");
        }
        Category category = new Category();
        category.setName(dto.getName());
        return convertToDTO(categoryRepository.save(category));
    }

    public CategoryDTO updateById(int id, CategoryDTO dto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        existingCategory.setName(dto.getName());
        return convertToDTO(categoryRepository.save(existingCategory));
    }

    @Transactional
    public void deleteById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " does not exist"));
                
        if (category.getBooks() != null) {
            for (Book book : category.getBooks()) {
                book.getCategories().remove(category);
            }
        }
        
        categoryRepository.deleteById(id);
    }
    
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
