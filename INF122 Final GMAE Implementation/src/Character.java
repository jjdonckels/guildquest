import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Character implements Entity{
    private String name;

    public Character(String name){ //needs other attributes
        this.name = name;
    }
}
