from services.worldClock import WorldClock

class worldTime: #displaying the world time
     def format(self, worldMins, realm=None):
        return WorldClock.format(worldMins)

class localTime: #displaying the local time
    def format(self, worldMins, realm=None):
        if realm is None:
            return WorldClock.format(worldMins)
        return WorldClock.format(worldMins + realm.offset)

class bothTime: #displaying both of them
    def format(self, worldMins, realm=None):
        world = WorldClock.format(worldMins) #format workold them formal local
        if realm is None:
            return f"World: {world}"
        local = WorldClock.format(worldMins + realm.offset)
        return f"World: {world} | Local({realm.name}): {local}"