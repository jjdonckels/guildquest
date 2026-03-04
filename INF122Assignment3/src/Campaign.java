import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campaign {
    private UUID campaign_id;
    private String name;
    private boolean is_public;
    private boolean archived;
    private User owner;
    private List<QuestEvent> events;
    private boolean can_edit;

    public Campaign(String name, User user){
        this.name = name;
        this.owner = user;
        this.campaign_id = UUID.randomUUID();
        this.is_public = true; //default public
        this.archived = false;
        this.can_edit = true;
        this.events = new ArrayList<>();
    }

    public void addQuestEvent(QuestEvent questEvent){
        this.events.add(questEvent);
    }

    public boolean removeQuestEvent(UUID eventId){
        for (QuestEvent questEvents : this.events){
            if(questEvents.getEvent_id().equals(eventId)){
                this.events.remove(questEvents);
                return true;
            }
        }
        return false;
    }

    public QuestEvent findQuestEvent(String name){
        for (QuestEvent event : this.events){
            if(event.getTitle().equalsIgnoreCase(name)){
                return event;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        String visibility = (is_public) ? "public" : "private";
        String archive = (archived) ? "archived" : "not archived";
        return String.format("""
                %s
                Events in this campaign: %s.
                This campaign is %s and %s.
                Owner: %s""", name, listEvents(), visibility, archive, this.owner.getUsername());
    }

    public String listEvents(){
        if(events.isEmpty()) return "None";
        StringBuilder events = new StringBuilder();
        for(int i = 0; i < this.events.size(); i++){
            events.append(this.events.get(i).getTitle());
            if(i !=  this.events.size() - 1){
                events.append(", ");
            }
        }
        return events.toString();
    }

    public void displayAllEventDetails(User user){
        for(QuestEvent qe : this.events){
            System.out.println(qe.display(user) + "\n");
        }
    }

}
