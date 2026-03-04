from dataclasses import dataclass, field
from .Inventory import Inventory

@dataclass
class Character:
    name: str
    character_class: str
    level: int = 0
    curr_inventory: Inventory = field(default_factory=Inventory) # composition - character owns an inventory