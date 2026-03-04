from core import GameTime
from models import Realm


def convert_to_realm_time(world_time: GameTime, realm: Realm) -> GameTime:
    """
    Convert World Clock time to realm local time using the realm's time_rule.
    
    The time_rule is an offset in minutes:
    - Positive values = realm is ahead of World Clock
    - Negative values = realm is behind World Clock
    
    Args:
        world_time: Time in World Clock
        realm: The realm with time_rule offset
        
    Returns:
        Game_Time: Local time in the realm
    """
    # Convert world time to total minutes
    total_mins = world_time.to_total_minutes() + realm.time_rule
    
    # Don't allow negative time - clamp to Day 0, 00:00:00
    if total_mins < 0:
        total_mins = 0
    
    # Convert back to days, hours, minutes
    days = total_mins // (24 * 60)
    remaining = total_mins % (24 * 60)
    hours = remaining // 60
    minutes = remaining % 60
    
    return GameTime(days, hours, minutes, 0)


def format_time_range(start_time: GameTime, end_time: GameTime = None) -> str:
    """
    Format a time range for display.
    
    Args:
        start_time: Start time
        end_time: Optional end time
        
    Returns:
        str: Formatted time range
    """
    if end_time:
        return f"{start_time.get_fulltime()} - {end_time.get_fulltime()}"
    else:
        return start_time.get_fulltime()