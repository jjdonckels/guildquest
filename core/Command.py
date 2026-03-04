"""
Command Pattern - Base Command Interface

DESIGN PATTERN: Command Pattern
Purpose: Encapsulate actions as objects to support undo/redo functionality

Benefits:
- Undo/Redo support - reverse actions
- Command history - track what user has done
- Macro recording - chain multiple commands
- Decouples sender from receiver
- Single Responsibility - each command handles one action
"""

from abc import ABC, abstractmethod


class Command(ABC):
    """
    Base interface for all commands.
    
    The Command pattern encapsulates a request as an object, allowing you to:
    - Store commands in history
    - Undo/redo operations
    - Queue or log requests
    - Support macro operations
    
    Each concrete command must implement:
    - execute(): Perform the action
    - undo(): Reverse the action
    - get_description(): Human-readable description for UI
    """
    
    @abstractmethod
    def execute(self) -> None:
        """
        Execute the command.
        
        This method performs the actual action (delete campaign, create character, etc.)
        Should store any information needed for undo.
        """
        pass
    
    @abstractmethod
    def undo(self) -> None:
        """
        Undo the command.
        
        This method reverses the action performed by execute().
        Must restore the system to the exact state before execute() was called.
        """
        pass
    
    @abstractmethod
    def get_description(self) -> str:
        """
        Get human-readable description of this command.
        
        Used for UI display (e.g., "Undo: Delete Campaign 'Epic Quest'")
        
        Returns:
            str: Short, clear description of what this command does
        
        Examples:
            "Delete Campaign 'Epic Quest'"
            "Create Character 'Gandalf'"
            "Rename Campaign 'Old Name' to 'New Name'"
        """
        pass