package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.CategoryDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping // Get All
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/search/{name}") // Get By Name
    public CategoryDTO getCategoryByName(@PathVariable String name) {
        return categoryService.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Category with name " + name + " not found"));
    }

    @PostMapping // Add New
    public CategoryDTO addCategory(@RequestBody CategoryDTO dto) {
        return categoryService.addCategory(dto);
    }

    @PutMapping("/update/{id}") // Update By Id
    public CategoryDTO updateCategory(@PathVariable int id, @RequestBody CategoryDTO dto) {
        return categoryService.updateById(id, dto);
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
    }
}
