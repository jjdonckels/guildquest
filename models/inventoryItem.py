import uuid

class InvetoryItem:
    def __init__(self, name, description, rarity):
        self.itemId = uuid.uuid4()
        self.name = name
        self.description = description
        self.rarity = rarity   

    def __str__(self):
        return f"{self.name} ({self.rarity}) - {self.description}"