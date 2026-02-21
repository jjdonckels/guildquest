import uuid
from services.worldClock import WorldClock

class Campaign:
    def __init__(self, name, owner, visibil="PRIVATE"):
        self.campaignId = uuid.uuid4()
        self.name = name
        self.visibility = visibil     
        self.archived = False
        self.owner = owner               
        self.events = []      
        
    def rename(self, newName):
        self.name = newName

    def setVisibil(self, visibilType):
        self.visibility = visibilType  

    def archive(self):
        self.archived = True         

    def addQuestEvent(self, event):
        self.events.append(event)

    def removeQuestEvent(self, eventId):
        self.events = [x for x in self.events if x.eventId != eventId]

    def getEventsForDay(self, day):
        start = WorldClock.toMins(day, 0, 0)
        end = WorldClock.toMins(day, 23, 59)
        return [x for x in self.events if start <= x.startTime <= end]

    def getEventsForWeek(self, startDay):
        start = WorldClock.toMins(startDay, 0, 0)
        end = WorldClock.toMins(startDay + 6, 23, 59)
        return [x for x in self.events if start <= x.startTime <= end]

    def __str__(self):
        return (f"Campaign(name={self.name}, "f"visibility={self.visibility}, " f"archived={self.archived}, " f"id={self.campaignId})")