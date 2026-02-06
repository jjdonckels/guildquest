import uuid

class Campaign:
    def __init__(self, name, owner, visibility="PRIVATE"):
        self.campaignId = uuid.uuid4()
        self.name = name
        self.visibility = visibility     
        self.archived = False
        self.owner = owner               
        self.events = []                

    # ---------- Event management ----------
    def addQuestEvent(self, event):
        self.events.append(event)

    def removeQuestEvent(self, event_id):
        self.events = [e for e in self.events if e.eventId != event_id]

    # ---------- Campaign state ----------
    def setVisibility(self, visibility_type):
        self.visibility = visibility_type

    def archive(self):
        self.archived = True

    def __str__(self):
        return (
            f"Campaign(name={self.name}, "
            f"visibility={self.visibility}, "
            f"archived={self.archived}, "
            f"id={self.campaignId})"
        )
