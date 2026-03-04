"""
*****NEW IMPLEMENTAION FOR A3*****
Abstract Factory for Character creation - Factory Method Pattern

This module defines the abstract base class for the Factory Method Pattern.
Each concrete factory (WarriorFactory, MageFactory, etc.) must implement
the factory methods to create characters with class-specific equipment.
"""

from abc import ABC, abstractmethod
from typing import List
from .Character import Character
from .Item import Item


class CharacterFactory(ABC):
    """
    Abstract Factory for creating characters.
    
    This is the "Creator" in the Factory Method Pattern. Subclasses must
    implement create_character() and get_starting_items() to provide
    class-specific character creation.
    """
    
    @abstractmethod
    def create_character(self, name: str, level: int) -> Character:
        """
        Factory Method: Create a character of a specific class.
        
        This method must be implemented by concrete factories to create
        the appropriate character type.
        
        Args:
            name: Character name
            level: Starting level (1-100)
            
        Returns:
            Character: A new character instance
        """
        pass
    
    @abstractmethod
    def get_starting_items(self) -> List[Item]:
        """
        Factory Method: Get class-specific starting equipment.
        
        Each character class starts with different items. Subclasses must
        implement this method to return the appropriate starting gear.
        
        Returns:
            List[Item]: List of starting items for this character class
        """
        pass
    
    def create_equipped_character(self, name: str, level: int = 1) -> Character:
        """
        Template Method: Create a fully equipped character.
        
        This method uses the factory methods (create_character and
        get_starting_items) to create a character and equip them with
        starting items. This is the public interface that clients should use.
        
        Args:
            name: Character name
            level: Starting level (default: 1)
            
        Returns:
            Character: A fully equipped character with starting inventory
        """
        # Call the factory method to create the character
        character = self.create_character(name, level)
        
        # Call the factory method to get starting items
        starting_items = self.get_starting_items()
        
        # Equip the character with starting items
        for item in starting_items:
            character.curr_inventory.add_inventory(item)
        
        return character