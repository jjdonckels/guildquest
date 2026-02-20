import uuid
from services.worldClock import WorldClock

class Realm:
    def __init__(self, name, description="", offset=0):
        self.realmId = uuid.uuid4()
        self.name = name
        self.description = description
        self.offset = offset   

    def toLocalTime(self, worldMins):
        return worldMins + self.offset

    def formatTime(self, worldMins):
        local = self.toLocalTime(worldMins)
        return WorldClock.format(local)

    def __str__(self):
        return self.name
