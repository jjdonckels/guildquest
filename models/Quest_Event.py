from dataclasses import dataclass, field
from typing import List, Optional
from core import GameTime
from models import Realm, Character, Item


@dataclass
class Quest_Event:
    name: str
    c_realm: Realm
    time: GameTime
    start_time: str
    end_time: str = "N/A"
    
    partaking_users: List[Character] = field(default_factory=list)
    reward_items: List[Item] = field(default_factory=list)
    required_items: List[Item] = field(default_factory=list)
        
    def set_realm(self, realm: Realm) -> None:
        """
        Sets the realm for the Quest_Event

        Args:
            realm (Realm): Given Realm object where the Quest_Event will take place
        """
        self.c_realm = realm
    
    def set_name(self, n_name: str) -> None:
        """
        Sets the name for the Quest_Event

        Args:
            n_name (str): Name to change Quest_Event's name to
        """
        self.name = n_name
        
    def set_starttime(self, new_start: str) -> None:
        """
        Sets a new start time for the Quest_Event

        Args:
            new_start (str): New start time to update to.
        """
        self.start_time = new_start
    
    def set_endtime(self, new_end: str) -> None:
        """
        Sets a new end time for the Quest_Event

        Args:
            new_end (str): New end time to update to.
        """
        self.end_time = new_end
    
    def grant_item(self, character: Character, item: Item) -> None:
        """
        Grants the character an item after the Quest_Event

        Args:
            character (Character): Character to grant item to
            item (Item): _description_
        """
        character.curr_inventory.add_inventory(item)
    
    def take_item(self, character: Character, item: Item) -> None:
        character.curr_inventory.remove_inventory(item)