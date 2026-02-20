
class WorldClock:
    @staticmethod
    def toMins(d, h, m):
        return d * 24 * 60 + h * 60 + m

    @staticmethod
    def fromMins(total):
        d = total // (24 * 60)
        rest = total % (24 * 60)
        h = rest // 60
        m = rest % 60
        return d, h, m

    @staticmethod
    def format(total):
        d, h, m = WorldClock.fromMins(total)
        return f"Day {d} {h}:{m:02d}"

    @staticmethod
    def validRange(start, end):
        if end is None:
            return True
        return end >= start