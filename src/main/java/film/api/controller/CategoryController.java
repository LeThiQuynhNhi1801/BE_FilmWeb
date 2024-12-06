package film.api.controller;

import film.api.DTO.response.CategoryDTO;
import film.api.exception.NotFoundException;
import film.api.models.Category;
import film.api.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(path = "/ApiV1", produces = "application/json")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/AllCategory")
    public ResponseEntity<?> getList() {
        List<CategoryDTO> categories = categoryService.getList();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/CategoryByName/{Categoryname}")
    public ResponseEntity<?> searchCategory(@PathVariable String Categoryname) {
        List<Category> categories = categoryService.searchCategory(Categoryname);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> findById(@PathVariable("CategoryId") Long id) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/AllCategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO category) {
        Category newCategory = categoryService.addCategory(
                Category.builder().CategoryName(category.getCategoryName()).build()
        );
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("CategoryId") Long id, @RequestBody CategoryDTO category) {

        Category updatedCategory = categoryService.updateCategory(id,
                Category.builder().CategoryName(category.getCategoryName()).build()
        );
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/CategoryById/{CategoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("CategoryId") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
