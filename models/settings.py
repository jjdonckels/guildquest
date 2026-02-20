from services.timeDisplay import(worldTime, localTime, bothTime)

class Settings:
    def __init__(self, currentRealm=None, theme="Light", timeDisplayMode="WORLD"):
        self.currentRealm = currentRealm      
        self.theme = theme                      
        self.timeDisplayMode = timeDisplayMode  

    def updateTheme(self, theme):
        self.theme = theme

    def setCurrentRealm(self, realm):
        self.currentRealm = realm

    def setTimeDisplayMode(self, mode):
        self.timeDisplayMode = mode

    def getTimeDisplayStrategy(self):
        if self.timeDisplayMode == "WORLD":
            return worldTime()
        elif self.timeDisplayMode == "LOCAL":
            return localTime()
        elif self.timeDisplayMode == "BOTH":
            return bothTime()
        else:
            # Default fallback
            return worldTime()

    def __str__(self):
        return (f"Settings(theme={self.theme}, " f"time_display={self.timeDisplayMode}, " f"realm={self.currentRealm})")