package film.api.service.impl;

import film.api.DTO.CategoryDTO;
import film.api.models.Category;
import film.api.repository.CategoryRepository;
import film.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategoryByFilmID(Long filmID){
        return categoryRepository.getCategoryByFilmID(filmID);
    }

    @Override
    public List<CategoryDTO> getList(){
        List<CategoryDTO> categoryDTO = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        for(Category category : categories){
            CategoryDTO newDTO = new CategoryDTO(category);
            categoryDTO.add(newDTO);
        }
        return categoryDTO;
    }
    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> searchCategory(String categoryname) {
        return categoryRepository.findCategoryByName(categoryname);
    }

    @Override
    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category){
        Category old = categoryRepository.findById(id).get();
        old.setCategoryName(category.getCategoryName());
        return categoryRepository.save(old);
    }

    @Override
    public  void deleteCategory(Long id){
        categoryRepository.delete(categoryRepository.findById(id).get());
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
