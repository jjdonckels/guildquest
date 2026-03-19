import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealmController {
    private List<Realm> realms;

    public RealmController() {
        this.realms = new ArrayList<Realm>();
    }

    public void addRealm(Realm realm){
        this.realms.add(realm);
    }
}
