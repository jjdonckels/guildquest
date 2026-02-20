import uuid

class InvetoryItem:
    def __init__(self, name, descript, rarity):
        self.itemId = uuid.uuid4()
        self.name = name
        self.descript = descript
        self.rarity = rarity   

    def __str__(self):
        return f"{self.name} ({self.rarity}) - {self.descript}"