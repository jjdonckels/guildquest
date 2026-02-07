import time

class WorldClock:
    def __init__(self):
        self.currentTime = self.now()

    def now(self):
        return int(time.time() // 60)

    def compare(self, t1, t2):
        return t1 <= t2
