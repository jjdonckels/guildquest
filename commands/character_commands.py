"""
Character Commands - Concrete Command Implementations

DESIGN PATTERN: Command Pattern (Concrete Commands)
Purpose: Encapsulate character-related actions as command objects

Each command can be executed and undone, supporting full undo/redo functionality.
"""

from core.Command import Command
from models import User, Character


class DeleteCharacterCommand(Command):
    """
    Command to delete a character (with undo support).
    
    Stores the deleted character so it can be restored on undo.
    """
    
    def __init__(self, user: User, character_index: int):
        """
        Initialize delete character command.
        
        Args:
            user: User who owns the character
            character_index: Index of character to delete in user.characters list
        """
        self.user = user
        self.character_index = character_index
        self.deleted_character: Character = None  # Stored for undo
    
    def execute(self) -> None:
        """
        Delete the character.
        
        Stores the character object before deleting so it can be restored.
        """
        # Store the character before deleting
        self.deleted_character = self.user.characters[self.character_index]
        
        # Delete the character
        self.user.characters.pop(self.character_index)
    
    def undo(self) -> None:
        """
        Restore the deleted character.
        
        Inserts the character back at its original position.
        """
        # Restore the character at its original position
        self.user.characters.insert(self.character_index, self.deleted_character)
    
    def get_description(self) -> str:
        """Get human-readable description."""
        if self.deleted_character:
            return f"Delete Character '{self.deleted_character.name}'"
        return "Delete Character"


class CreateCharacterCommand(Command):
    """
    Command to create a character (with undo support).
    
    Stores the created character so it can be removed on undo.
    """
    
    def __init__(self, user: User, character: Character):
        """
        Initialize create character command.
        
        Args:
            user: User who will own the character
            character: The character object to add
        """
        self.user = user
        self.character = character
        self.character_index: int = -1  # Position where it was added
    
    def execute(self) -> None:
        """
        Add the character to the user's character list.
        
        Stores the character's position in the list for undo.
        """
        # Add the character to user's list
        self.user.characters.append(self.character)
        
        # Store its position for undo
        self.character_index = len(self.user.characters) - 1
    
    def undo(self) -> None:
        """
        Remove the created character.
        
        Deletes the character that was created by this command.
        """
        # Remove the character we created
        if self.character_index >= 0 and self.character_index < len(self.user.characters):
            self.user.characters.pop(self.character_index)
    
    def get_description(self) -> str:
        """Get human-readable description."""
        return f"Create Character '{self.character.name}'"