"""
*****NEW IMPLEMENTAION FOR A3*****
Concrete Character Factories - Factory Method Pattern Implementation

This module contains all 8 concrete factory classes, one for each character class.
Each factory implements the Factory Method Pattern by providing class-specific
implementations of create_character() and get_starting_items().

Character Classes:
- Warrior: Heavy melee fighter with sword and shield
- Mage: Spellcaster with staff and spellbook
- Rogue: Stealthy assassin with daggers and lockpicks
- Cleric: Holy warrior with mace and prayer beads
- Ranger: Ranged combatant with bow and arrows
- Paladin: Holy knight with longsword and holy symbol
- Bard: Musical support with lute and dagger
- Druid: Nature guardian with staff and herbs
"""

from typing import List
from .CharacterFactory import CharacterFactory
from .Character import Character
from .Item import Item


# ============================================================================
# CONCRETE FACTORIES - One for each character class
# ============================================================================

class WarriorFactory(CharacterFactory):
    """
    Concrete Factory for creating Warrior characters.
    
    Warriors are heavy melee fighters who start with a sword and shield.
    They excel at close-quarters combat and protecting their allies.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Warrior character"""
        return Character(
            name=name,
            character_class="Warrior",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Warriors start with sword and shield"""
        return [
            Item(
                name="Iron Sword",
                rarity="Common",
                damage=10,
                description="A sturdy blade forged for battle"
            ),
            Item(
                name="Wooden Shield",
                rarity="Common",
                damage=0,
                description="Basic protection for novice warriors"
            )
        ]


class MageFactory(CharacterFactory):
    """
    Concrete Factory for creating Mage characters.
    
    Mages are powerful spellcasters who start with a staff and spellbook.
    They harness arcane energies to devastate enemies from afar.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Mage character"""
        return Character(
            name=name,
            character_class="Mage",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Mages start with staff and spellbook"""
        return [
            Item(
                name="Apprentice Staff",
                rarity="Common",
                damage=8,
                description="A wooden staff to channel magical energy"
            ),
            Item(
                name="Spellbook of Minor Cantrips",
                rarity="Uncommon",
                damage=0,
                description="Contains basic fire and frost spells"
            )
        ]


class RogueFactory(CharacterFactory):
    """
    Concrete Factory for creating Rogue characters.
    
    Rogues are stealthy assassins who start with daggers and lockpicks.
    They excel at sneaking, stealing, and striking from the shadows.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Rogue character"""
        return Character(
            name=name,
            character_class="Rogue",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Rogues start with dagger and lockpicks"""
        return [
            Item(
                name="Steel Dagger",
                rarity="Common",
                damage=7,
                description="Quick and deadly, perfect for backstabs"
            ),
            Item(
                name="Thieves' Tools",
                rarity="Common",
                damage=0,
                description="Lockpicks and tools for bypassing security"
            )
        ]


class ClericFactory(CharacterFactory):
    """
    Concrete Factory for creating Cleric characters.
    
    Clerics are holy warriors who start with a mace and prayer beads.
    They balance combat prowess with divine healing magic.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Cleric character"""
        return Character(
            name=name,
            character_class="Cleric",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Clerics start with mace and prayer beads"""
        return [
            Item(
                name="Holy Mace",
                rarity="Common",
                damage=6,
                description="A blessed weapon that smites the undead"
            ),
            Item(
                name="Prayer Beads",
                rarity="Common",
                damage=0,
                description="Sacred beads used for healing rituals"
            )
        ]


class RangerFactory(CharacterFactory):
    """
    Concrete Factory for creating Ranger characters.
    
    Rangers are skilled hunters who start with a bow and quiver.
    They excel at tracking, survival, and ranged combat.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Ranger character"""
        return Character(
            name=name,
            character_class="Ranger",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Rangers start with bow and arrows"""
        return [
            Item(
                name="Hunting Bow",
                rarity="Common",
                damage=9,
                description="Reliable bow for taking down prey from afar"
            ),
            Item(
                name="Quiver of Arrows",
                rarity="Common",
                damage=0,
                description="Holds 20 sharp arrows"
            )
        ]


class PaladinFactory(CharacterFactory):
    """
    Concrete Factory for creating Paladin characters.
    
    Paladins are holy knights who start with a longsword and holy symbol.
    They combine martial prowess with divine magic to protect the innocent.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Paladin character"""
        return Character(
            name=name,
            character_class="Paladin",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Paladins start with longsword and holy symbol"""
        return [
            Item(
                name="Longsword",
                rarity="Uncommon",
                damage=12,
                description="A knight's blade blessed by the gods"
            ),
            Item(
                name="Holy Symbol",
                rarity="Common",
                damage=0,
                description="Sacred amulet that grants divine protection"
            )
        ]


class BardFactory(CharacterFactory):
    """
    Concrete Factory for creating Bard characters.
    
    Bards are charismatic performers who start with a lute and dagger.
    They inspire allies with music while defending themselves when needed.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Bard character"""
        return Character(
            name=name,
            character_class="Bard",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Bards start with lute and dagger"""
        return [
            Item(
                name="Enchanted Lute",
                rarity="Common",
                damage=4,
                description="Musical instrument for inspiring allies"
            ),
            Item(
                name="Backup Dagger",
                rarity="Common",
                damage=5,
                description="For when music isn't enough"
            )
        ]


class DruidFactory(CharacterFactory):
    """
    Concrete Factory for creating Druid characters.
    
    Druids are nature guardians who start with a staff and herb pouch.
    They harness the power of nature and can shapeshift into animals.
    """
    
    def create_character(self, name: str, level: int) -> Character:
        """Create a Druid character"""
        return Character(
            name=name,
            character_class="Druid",
            level=level
        )
    
    def get_starting_items(self) -> List[Item]:
        """Druids start with staff and herbs"""
        return [
            Item(
                name="Wooden Staff",
                rarity="Common",
                damage=7,
                description="Staff carved from ancient oak, channels nature's power"
            ),
            Item(
                name="Herb Pouch",
                rarity="Common",
                damage=0,
                description="Contains medicinal plants and brewing ingredients"
            )
        ]