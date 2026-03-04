from dataclasses import dataclass

@dataclass
class Item:
    name: str
    rarity: str
    damage: int
    description: str 
        
    def use_item(self) -> None:
        """
        Prints a statement saying the item was used.
        """
        print(f'Used: {self.name}')