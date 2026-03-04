"""
Command Manager - Handles Command Execution and History

DESIGN PATTERN: Command Pattern (Invoker)
Purpose: Manages command execution, undo/redo stacks, and history

The CommandManager is the "Invoker" in the Command pattern.
Each screen (CampaignScreen, CharacterScreen) will have its own CommandManager
to maintain screen-specific undo/redo history.
"""

from typing import List, Optional
from core.Command import Command


class CommandManager:
    """
    Manages command execution and undo/redo functionality.

    Each screen creates its own CommandManager to maintain
    separate undo/redo histories for better UX.

    Example:
        >>> manager = CommandManager()
        >>> cmd = DeleteCampaignCommand(user, 0)
        >>> manager.execute(cmd)  # Executes and adds to history
        >>> manager.undo()  # Undoes the deletion
        >>> manager.redo()  # Re-applies the deletion
    """

    def __init__(self):
        """Initialize empty command history and redo stack."""
        self._history: List[Command] = []  # Commands that have been executed
        self._redo_stack: List[Command] = []  # Commands that have been undone

    def execute(self, command: Command) -> None:
        """
        Execute a command and add it to history.

        Args:
            command: Command to execute

        Side effects:
            - Executes the command
            - Adds command to history
            - Clears redo stack (can't redo after new action)

        Example:
            >>> cmd = DeleteCampaignCommand(user, 0)
            >>> manager.execute(cmd)
            # Campaign is deleted and command is in history
        """
        command.execute()
        self._history.append(command)
        self._redo_stack.clear()

    def undo(self) -> bool:
        """
        Undo the last command.

        Returns:
            bool: True if undo was successful, False if nothing to undo

        Example:
            >>> manager.undo()  # Undoes last action
            True
            >>> manager.undo()  # Nothing left to undo
            False
        """
        if not self.can_undo():
            return False

        command = self._history.pop()
        command.undo()
        self._redo_stack.append(command)
        return True

    def redo(self) -> bool:
        """
        Redo the last undone command.

        Returns:
            bool: True if redo was successful, False if nothing to redo

        Example:
            >>> manager.undo()  # Undo delete
            >>> manager.redo()  # Redo delete
            True
        """
        if not self.can_redo():
            return False

        command = self._redo_stack.pop()
        command.execute()

        self._history.append(command)

        return True

    def can_undo(self) -> bool:
        """
        Check if undo is available.

        Returns:
            bool: True if there are commands that can be undone

        Used to enable/disable undo button in UI.
        """
        return len(self._history) > 0

    def can_redo(self) -> bool:
        """
        Check if redo is available.

        Returns:
            bool: True if there are commands that can be redone

        Used to enable/disable redo button in UI.
        """
        return len(self._redo_stack) > 0

    def peek_undo(self) -> Optional[Command]:
        """
        Get the command that would be undone (without undoing it).

        Returns:
            Command: The command that undo() would reverse, or None if history is empty

        Used to display "Undo: Delete Campaign 'Epic Quest'" in UI.

        Example:
            >>> cmd = manager.peek_undo()
            >>> if cmd:
            ...     print(f"Undo: {cmd.get_description()}")
            Undo: Delete Campaign 'Epic Quest'
        """
        return self._history[-1] if self._history else None

    def peek_redo(self) -> Optional[Command]:
        """
        Get the command that would be redone (without redoing it).

        Returns:
            Command: The command that redo() would execute, or None if redo stack is empty

        Used to display "Redo: Delete Campaign 'Epic Quest'" in UI.
        """
        return self._redo_stack[-1] if self._redo_stack else None

    def clear_history(self) -> None:
        """
        Clear all command history and redo stack.

        Useful when switching contexts (e.g., logging out, changing screens).
        """
        self._history.clear()
        self._redo_stack.clear()

    def get_history_size(self) -> int:
        """
        Get the number of commands in history.

        Returns:
            int: Number of executed commands
        """
        return len(self._history)

    def get_redo_size(self) -> int:
        """
        Get the number of commands that can be redone.

        Returns:
            int: Number of undone commands
        """
        return len(self._redo_stack)
