import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Setter;

@Setter
public class RealmController {

    private List<Realm> realms;

    public RealmController(){
        realms = new ArrayList<>();
        realms.add(new Realm()); //hub realm
        realms.add(new Realm("SampleRealm", "An example realm to allow for testing with time offsets, etc", 100)); //sample realm
    }

    public Realm hub(){
        //hub realm is first position in list
        return realms.get(0);
    }

    public Realm findRealm(String realm_name){
        for(Realm realm : realms){ if(realm.getName().equalsIgnoreCase(realm_name)){ return realm;}}
        return null;
    }

    public void displayRealms(){
        for(int i = 1; i < realms.size() + 1; i++){
            System.out.printf("%s. %s\n", i, realms.get(i - 1).getName());
        }
    }

    public List<Realm> getRealms(){
        return Collections.unmodifiableList(this.realms);
    }

}
