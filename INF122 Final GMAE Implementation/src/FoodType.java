import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodType implements ItemType{
    private String name;
    private String description;
    private int healPoints;

    public FoodType(String name, String description, int healPoints) {
        this.name = name;
        this.description = description;
        this.healPoints = healPoints;
    }

    @Override
    public String displayInfo() {
        return String.format("""
                %s
                %s
                Heals %s HP when eaten.
                """, this.name, this.description, this.healPoints);
    }
}
