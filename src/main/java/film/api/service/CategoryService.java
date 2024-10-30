package film.api.service;

import film.api.DTO.CategoryDTO;
import film.api.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategoryByFilmID(Long filmID);

    List<CategoryDTO> getList();

    Optional<Category> findById(Long id);

    List<Category> searchCategory(String categoryname);

    Category addCategory(Category category);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);

    List<Category> findAll();
}
