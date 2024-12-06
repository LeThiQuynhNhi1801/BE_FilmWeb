package film.api.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import  film.api.models.Category;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String CategoryName;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.CategoryName = category.getCategoryName();
    }
}
