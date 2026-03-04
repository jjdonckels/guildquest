from dataclasses import dataclass, field
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from models import User

# I could use an abstract factory design pattern here to create multiple types of realms

@dataclass
class Realm:
    name: str
    map_id: int
    time_rule: int
    selected_user: 'User'
    desc: str = "N/A"
        
    def set_desc(self, text: str) -> None:
        """
        Setter method to change a realm's description

        Args:
            text (str): New realm description
        """
        self.desc = text
    
    def change_name(self, text: str) -> None:
        """
        Helper method to change a realm's name

        Args:
            text (str): New name for realm
        """
        self.name = text
        
    def change_time_rule(self, time: int) -> None:
        """
        Changes the time rule of a realm

        Args:
            time (int): New time rule for realm
        """
        self.time_rule = time