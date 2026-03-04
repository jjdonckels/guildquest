from .GameTime import GameTime

"""
Created under my assumptions for how world_clock works in interaction with Game_Time
"""

class WorldClock:
    """
    World clock for an entire campaign.
    """
    def __init__(self):
        self.is_running = False
        # init the world clock to start at the literal beginning of time of a campaign
        self.current_time = GameTime(0, 0, 0, 0)

    def start(self) -> None:
        """
        Starts the world clock by setting is_running to True
        """
        self.is_running = True

    def stop(self) -> None:
        """
        Stops the world clock by setting is_running to False
        """
        self.is_running = False

    def set_time(self, game_time: GameTime) -> None:
        """
        Sets self.current_time to the given Game_Time obj

        Args:
            game_time (Game_Time): Game_Time obj to set current_time to
        """
        self.current_time = game_time

    def get_current_time(self) -> GameTime:
        """
        Getter method to get self.current_time

        Returns:
            GameTime: GameTime object of the current time
        """
        return self.current_time

    def advance(self, days: int = 0, hours: int = 0, minutes: int = 0) -> None:
        """
        Advances the world clock based on the given arguments

        Args:
            days (int, optional): Days to add to World_Clock. Defaults to 0.
            hours (int, optional): Hours to add to World_Clock. Defaults to 0.
            minutes (int, optional): Minutes to add to World_Clock. Defaults to 0.
        """
        new_day = self.current_time.current_day + days
        new_hour = self.current_time.current_hour + hours
        new_minute = self.current_time.current_minute + minutes

        # Normalize
        new_hour += new_minute // 60
        new_minute %= 60
        new_day += new_hour // 24
        new_hour %= 24

        self.current_time = GameTime(new_day, new_hour, new_minute, 0)
