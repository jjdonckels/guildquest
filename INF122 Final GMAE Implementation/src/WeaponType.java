import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeaponType implements ItemType{
    private String name;
    private int damage;
    private String description;

    public WeaponType(String name, int damage, String description){
        this.name = name;
        this.damage = damage;
        this.description = description;
    }

    public String displayInfo(){
        return String.format("""
                %s
                %s
                Damage: %s HP
                """, this.name, this.description, this.damage);
    }
}
