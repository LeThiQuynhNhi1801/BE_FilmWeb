package film.api.DTO.request;


import film.api.models.Director;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectorDTO {

    private Long id;

    private String DirectorName;

    private Integer Age;

    private String nativeLand;

    private Integer Sex;

    public DirectorDTO(Director actor) {
        this.id = actor.getId();
        this.DirectorName = actor.getDirectorName();
        this.Age = actor.getAge();
        this.nativeLand = actor.getNativeLand();
        this.Sex = actor.getSex();
    }
}
