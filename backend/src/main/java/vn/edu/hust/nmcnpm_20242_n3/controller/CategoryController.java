package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.CategoryDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Category;
import vn.edu.hust.nmcnpm_20242_n3.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryService.addCategory(categoryDTO);
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Category> findCategoryByName(@RequestParam String name) {
        return categoryService.findByName(name)
                .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCategoryByName(@RequestParam String name, @RequestBody CategoryDTO categoryDTO){
        try{
            Category updatedCategory=categoryService.updateByName(name, categoryDTO);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCategoryByName(@RequestParam String name) {
        try {
            categoryService.deleteByName(name);
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}