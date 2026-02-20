import uuid

class Character:
    def __init__(self, name, classType="Explorer", lvl=1):
        self.characterId = uuid.uuid4()
        self.name = name
        self.classType = classType
        self.lvl = lvl
        self.inventory = []  

    def levelUp(self):
        self.lvl += 1

    def addItem(self, item):
        self.inventory.append(item)

    def removeItem(self, item):
        if item in self.inventory:
            self.inventory.remove(item)

    def __str__(self):
        return f"Character(name={self.name}, class={self.classType}, level={self.level})"
