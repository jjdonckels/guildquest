import uuid

class questEvent:
    def __init__(self, title, startTime, realm, endTime = None):
        self.eventId = uuid.uuid4()
        self.title = title
        self.startTime = startTime
        self.endTime = endTime
        self.realm = realm
        self.participants = []

    def updateTIme(self, start, end = None):
        self.startTIme = start
        self.endTime = end

    def addParticipant(self, character):
        for p in self.participants:
            if p.characterId == character.characterId:
                return
        self.participants.append(character)

    def removeParticipant(self, character):
        self.participants = [
            p for p in self.participants
            if p.characterId != character.characterId
        ]
    def __str__(self):
                return f"QuestEvent({self.title}, {self.startTime}, realm={self.realm})"
