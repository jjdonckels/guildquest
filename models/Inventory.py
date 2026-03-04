from dataclasses import dataclass, field
from typing import List
from models import Item

@dataclass
class Inventory:
    # association - Inventory references Items to be put into the list
    items: List[Item] = field(default_factory=list)

    def add_inventory(self, i: Item) -> None:
        """
        Adds a given item, i, to the Inventory

        Args:
            i (Item): Item to add to inventory
        """
        self.items.append(i)
        
    def remove_inventory(self, i: Item) -> None:
        """
        Removes a given item from the inventory

        Args:
            i (Item): Item to remove from
        """
        self.items.remove(i)
    
    def update_inventory(self) -> None:
        pass
    