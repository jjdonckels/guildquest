class Settings:
    def __init__(self, currentRealm=None, theme="Light", timeDisplayMode="24h"):
        self.currentRealm = currentRealm      
        self.theme = theme                      
        self.timeDisplayMode = timeDisplayMode  

    def updateTheme(self, theme):
        self.theme = theme

    def setCurrentRealm(self, realm):
        self.currentRealm = realm

    def setTimeDisplayMode(self, mode):
        self.timeDisplayMode = mode

    def __str__(self):
        return (f"Settings(theme={self.theme}, " f"time_display={self.timeDisplayMode}, " f"realm={self.currentRealm})")