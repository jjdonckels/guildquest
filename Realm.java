package guildquest.realm;

import java.util.UUID;

public class Realm {

    private final String id;
    private String name;
    private String description;
    private LocalTimeRule localTimeRule;

    public Realm(String name, String description, LocalTimeRule rule) {
        this.id            = UUID.randomUUID().toString();
        this.name          = name;
        this.description   = description;
        this.localTimeRule = rule;
    }

    public String getId()                  { return id; }
    public String getName()                { return name; }
    public String getDescription()         { return description; }
    public LocalTimeRule getLocalTimeRule() { return localTimeRule; }

    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLocalTimeRule(LocalTimeRule r)  { this.localTimeRule = r; }
}
