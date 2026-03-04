"""
*****NEW IMPLEMENTAION FOR A3*****
Character Factory Registry - Central access point for character factories

This registry provides a single point of access to all character factories.
It implements a mapping from character class names to their respective factory
instances, allowing the GUI to create characters without knowing the specific
factory implementation.

Usage:
    # Create a character using the registry
    character = CharacterFactoryRegistry.create_character("Warrior", "Thorin", 5)
    
    # Or get a factory directly
    factory = CharacterFactoryRegistry.get_factory("Mage")
    character = factory.create_equipped_character("Gandalf", 10)
"""

from typing import Dict
from .CharacterFactory import CharacterFactory
from .CharacterFactories import (
    WarriorFactory,
    MageFactory,
    RogueFactory,
    ClericFactory,
    RangerFactory,
    PaladinFactory,
    BardFactory,
    DruidFactory
)
from .Character import Character


class CharacterFactoryRegistry:
    """
    Registry for accessing character factories.
    
    This class maintains a mapping of character class names to their
    respective factory instances. It provides convenient methods for
    creating characters without needing to know which factory to use.
    """
    
    # Private class variable: mapping of class names to factory instances
    _factories: Dict[str, CharacterFactory] = {
        "Warrior": WarriorFactory(),
        "Mage": MageFactory(),
        "Rogue": RogueFactory(),
        "Cleric": ClericFactory(),
        "Ranger": RangerFactory(),
        "Paladin": PaladinFactory(),
        "Bard": BardFactory(),
        "Druid": DruidFactory()
    }
    
    @classmethod
    def get_factory(cls, character_class: str) -> CharacterFactory:
        """
        Get the appropriate factory for a character class.
        
        Args:
            character_class: Name of the character class (e.g., "Warrior", "Mage")
            
        Returns:
            CharacterFactory: The factory instance for the specified class
            
        Raises:
            ValueError: If the character class is not recognized
            
        Example:
            >>> factory = CharacterFactoryRegistry.get_factory("Warrior")
            >>> character = factory.create_equipped_character("Conan", 1)
        """
        factory = cls._factories.get(character_class)
        
        if factory is None:
            valid_classes = ", ".join(cls._factories.keys())
            raise ValueError(
                f"Unknown character class: '{character_class}'. "
                f"Valid classes are: {valid_classes}"
            )
        
        return factory
    
    @classmethod
    def create_character(cls, character_class: str, name: str, level: int = 1) -> Character:
        """
        Convenience method to create a character directly.
        
        This method delegates to the appropriate factory to create a fully
        equipped character with starting items.
        
        Args:
            character_class: Name of the character class (e.g., "Warrior")
            name: Character name
            level: Starting level (default: 1)
            
        Returns:
            Character: A fully equipped character with starting inventory
            
        Raises:
            ValueError: If the character class is not recognized
            
        Example:
            >>> character = CharacterFactoryRegistry.create_character(
            ...     character_class="Mage",
            ...     name="Gandalf",
            ...     level=10
            ... )
            >>> print(character.name)
            Gandalf
            >>> print(len(character.curr_inventory.items))
            2  # Staff and Spellbook
        """
        factory = cls.get_factory(character_class)
        return factory.create_equipped_character(name, level)
    
    @classmethod
    def get_available_classes(cls) -> list[str]:
        """
        Get a list of all available character classes.
        
        Returns:
            list[str]: List of character class names
            
        Example:
            >>> classes = CharacterFactoryRegistry.get_available_classes()
            >>> print(classes)
            ['Warrior', 'Mage', 'Rogue', 'Cleric', 'Ranger', 'Paladin', 'Bard', 'Druid']
        """
        return list(cls._factories.keys())
    
    @classmethod
    def is_valid_class(cls, character_class: str) -> bool:
        """
        Check if a character class name is valid.
        
        Args:
            character_class: Name to check
            
        Returns:
            bool: True if the class name is valid, False otherwise
            
        Example:
            >>> CharacterFactoryRegistry.is_valid_class("Warrior")
            True
            >>> CharacterFactoryRegistry.is_valid_class("Ninja")
            False
        """
        return character_class in cls._factories