from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from .WorldClock import WorldClock # Fixes circular dependency


class GameTime:
    def __init__(self, current_day: int = 0, current_hour: int = 0, current_minute: int = 0, current_seconds: int = 0):
        self.current_day = current_day
        self.current_hour = current_hour
        self.current_minute = current_minute
        self.current_seconds = current_seconds
        self.full_time = self._format_time()

    def _format_time(self) -> str:
        """
        Internal helper function to format the full date into string format

        Returns:
            str: Full formatted date as a string.
        """
        return f"Day {self.current_day}, {self.current_hour:02d}:{self.current_minute:02d}:{self.current_seconds:02d}"

    def start_time(self, c: 'WorldClock') -> None:
        """
        Starts the world clock

        Args:
            c (WorldClock): WorldClock object to start clock
        """
        c.start()

    def pause_time(self, c: 'WorldClock') -> None:
        """
        Pauses the world clock

        Args:
            c (WorldClock): WorldClock object to pause clock
        """
        c.stop()

    def get_day(self) -> int:
        """
        Returns current day

        Returns:
            int: current day
        """
        return self.current_day

    def get_hour(self) -> int:
        """
        Returns current hour

        Returns:
            int: current hour
        """
        return self.current_hour

    def get_minute(self) -> int:
        """
        Returns current minute

        Returns:
            int: current minute
        """
        return self.current_minute

    def get_seconds(self) -> int:
        """
        Returns current seconds

        Returns:
            int: current seconds
        """
        return self.current_seconds

    def get_fulltime(self) -> str:
        """
        Returns the full formatted date string

        Returns:
            str: Full date
        """
        return self.full_time

    def to_total_minutes(self) -> int:
        """
        Convert this time to total minutes since Day 0, 00:00:00.
        Used for comparing and sorting events chronologically.

        Returns:
            int: Total minutes
        """
        return (self.current_day * 24 * 60) + (self.current_hour * 60) + self.current_minute
