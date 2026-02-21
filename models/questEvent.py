import uuid
from services.worldClock import WorldClock


class QuestEvent:
    def __init__(self, title, startTime, realm, endTime = None):
        if not WorldClock.validRange(startTime, endTime):
            raise ValueError("Error try again")
        self.eventId = uuid.uuid4()
        self.title = title
        self.startTime = startTime
        self.endTime = endTime
        self.realm = realm
        self.participants = []

    def updateTime(self, start, end = None):
        if not WorldClock.validRange(start, end):
            raise ValueError("Error try ag ain")
        self.startTime = start
        self.endTime = end

    def addParticipant(self, character):
        for x in self.participants:
            if x.characterId == character.characterId:
                return
        self.participants.append(character)

    def removeParticipant(self, character):
        self.participants = [x for x in self.participants if x.characterId != character.characterId]

    def __str__(self):
        startStr = WorldClock.format(self.startTime)
        if self.endTime is None:
            endStr = "None"
        else:
            endStr = WorldClock.format(self.endTime)

        return f"{self.title} | {startStr} -> {endStr} | Realm: {self.realm.name}"
    
    def toDisplayString(self, settings):
        strategy = settings.getTimeDisplayStrategy()
        realm = settings.currentRealm if settings.currentRealm else self.realm
        start = strategy.format(self.startTime, realm)

        if self.endTime is None:
            endStr = "None"
        else:
            endStr = strategy.format(self.endTime, realm)

        return f"{self.title} | {start} -> {endStr} | Realm: {self.realm.name}"
