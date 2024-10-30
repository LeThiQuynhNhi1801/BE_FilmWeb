package film.api.DTO;

import film.api.models.Actor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActorDTO {

    private Long id;

    private String ActorName;

    private Integer Age;

    private String nativeLand;

    private Integer Sex;

    public ActorDTO(Actor actor) {
        this.id = actor.getId();
        this.ActorName = actor.getActorName();
        this.Age = actor.getAge();
        this.nativeLand = actor.getNativeLand();
        this.Sex = actor.getSex();
    }
}
