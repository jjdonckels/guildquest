from typing import Optional
from core import GameTime
from .Realm import Realm
from .User import User
from .Quest_Event import Quest_Event


class Campaign:
    def __init__(self,
                 title: str,
                 activity: bool,
                 time: GameTime,
                 c_realm: Realm,
                 events_display: str,
                 quests: list[Quest_Event] = None,
                 permitted_users: list[User] = None,
                 edit_users: list[User] = None
                 ):
        self.title = title
        self.activity = activity
        self.time = time
        self.c_realm = c_realm
        self.events_display = events_display

        # *****NEW IMPLEMENTAION FOR A3*****
        self._quests = quests if quests is not None else []
        self._permitted_users = permitted_users if permitted_users is not None else []
        self._edit_users = edit_users if edit_users is not None else []

    # *****NEW IMPLEMENTAION FOR A3*****
    # ========================================================================
    # QUEST COLLECTION METHODS
    # ========================================================================

    def get_quests(self) -> list[Quest_Event]:
        """
        Get a copy of the quests list.

        Returns a copy to prevent external modification of the internal list.
        Use add_quest() and remove_quest() to modify quests.

        Returns:
            list[Quest_Event]: Copy of the quests list
        """
        return self._quests.copy()

    def get_quest_count(self) -> int:
        """
        Get the number of quests in this campaign.

        Returns:
            int: Number of quests
        """
        return len(self._quests)

    def get_quest_at(self, index: int) -> Quest_Event:
        """
        Get a quest by index.

        Args:
            index: Quest index

        Returns:
            Quest_Event: Quest at the specified index

        Raises:
            IndexError: If index is out of range
        """
        if index < 0 or index >= len(self._quests):
            raise IndexError(
                f"Quest index {index} out of range (0-{len(self._quests)-1})")
        return self._quests[index]

    def add_quest_object(self, quest: Quest_Event) -> None:
        """
        Add an existing quest to the campaign.

        Args:
            quest: Quest to add

        Raises:
            ValueError: If quest is already in campaign
        """
        if quest in self._quests:
            raise ValueError(
                f"Quest '{quest.name}' is already in this campaign")
        self._quests.append(quest)

    def create_quest(self, name: str,
                     time: GameTime,
                     ch_realm: Realm,
                     start_time: str,
                     end_time: str = "N/A") -> Quest_Event:
        """
        Creates a new quest and adds it to the campaign.

        Returns:
            Quest_Event: The newly created quest
        """
        quest = Quest_Event(name, ch_realm, time, start_time, end_time)
        self._quests.append(quest)
        return quest

    def remove_quest_at(self, index: int) -> Quest_Event:
        """
        Remove a quest by index.

        Args:
            index: Quest index

        Returns:
            Quest_Event: The removed quest

        Raises:
            IndexError: If index is out of range
        """
        if index < 0 or index >= len(self._quests):
            raise IndexError(f"Quest index {index} out of range")
        return self._quests.pop(index)

    def remove_quest(self, quest: Quest_Event) -> None:
        """
        Remove a specific quest from the campaign.

        Args:
            quest: Quest to remove

        Raises:
            ValueError: If quest is not in campaign
        """
        if quest not in self._quests:
            raise ValueError(f"Quest '{quest.name}' is not in this campaign")
        self._quests.remove(quest)

    def clear_quests(self) -> None:
        """Remove all quests from the campaign."""
        self._quests.clear()

    def update_quest(self, q_num: int, *, name: Optional[str] = None,
                     start_time: Optional[str] = None, realm: Optional[Realm] = None) -> None:
        """
        Updates a quest's info based on the given optional keyword arguments.

        Args:
            q_num (int): Quest index 
            realm (Optional[Realm]): New Realm to change quest to. Defaults to None.
            name (Optional[str], optional): New quest name to rename. Defaults to None.
            start_time (Optional[str], optional): New start time for the quest. Defaults to None.
        """
        if q_num < 0 or q_num >= len(self._quests):
            raise IndexError("Quest index out of range")

        quest = self._quests[q_num]

        if name is not None:
            quest.set_name(name)

        if start_time is not None:
            quest.set_starttime(start_time)

        if realm is not None:
            quest.set_realm(realm)

    def delete_quest(self, q_num: int) -> None:
        """
        Removes a Quest_Event from the campaign's quests list.

        DEPRECATED: Use remove_quest_at() instead.

        Args:
            q_num (int): index of quest
        """
        self._quests.pop(q_num)

    # ========================================================================
    # PERMITTED USERS COLLECTION METHODS
    # ========================================================================

    def get_permitted_users(self) -> list[User]:
        """
        Get a copy of the permitted users list.

        Returns:
            list[User]: Copy of permitted users list
        """
        return self._permitted_users.copy()

    def get_permitted_user_count(self) -> int:
        """Get the number of permitted users."""
        return len(self._permitted_users)

    def add_permitted_user(self, user: User) -> None:
        """
        Add user to permitted_users list.

        Users in this list can view the campaign.

        Args:
            user (User): User to add
        """
        if user not in self._permitted_users:
            self._permitted_users.append(user)

    def remove_permitted_user(self, user: User) -> None:
        """
        Remove a user from permitted list.

        BUGFIX: Changed from append to remove (was incorrect logic)

        Args:
            user (User): User to remove
        """
        if user in self._permitted_users:
            self._permitted_users.remove(user)

        # Non-viewers should also not be able to edit
        if user in self._edit_users:
            self._edit_users.remove(user)

    def clear_permitted_users(self) -> None:
        """Remove all permitted users."""
        self._permitted_users.clear()

    # ========================================================================
    # EDIT USERS COLLECTION METHODS
    # ========================================================================

    def get_edit_users(self) -> list[User]:
        """
        Get a copy of the edit users list.

        Returns:
            list[User]: Copy of edit users list
        """
        return self._edit_users.copy()

    def get_edit_user_count(self) -> int:
        """Get the number of edit users."""
        return len(self._edit_users)

    def add_edit_user(self, user: User) -> None:
        """
        Add user to have edit permissions.

        Args:
            user (User): User to add
        """
        if user not in self._edit_users:
            self._edit_users.append(user)

        # Editor should also have view permissions
        if user not in self._permitted_users:
            self._permitted_users.append(user)

    def clear_edit_users(self) -> None:
        """Remove all edit users."""
        self._edit_users.clear()

    # ========================================================================
    # EXISTING METHODS UPDATED
    # ========================================================================

    def start_campaign(self) -> None:
        """ 
        Starts the campaign by setting the activity status to True
        """
        self.activity = True

    def rename(self, name: str) -> None:
        """
        Renames the campaign based on the given new name

        Args:
            name (str): New name to rename campaign
        """
        self.title = name

    def change_act(self) -> None:
        """
        Changes the activity status to the opposite state.
        """
        self.activity = not self.activity

    def delete_camp(self) -> None:
        """
        Deletes the campaign.
        """
        # handle fr after storage implementation
        self.activity = False
        self.clear_quests()
        self.clear_permitted_users()
        self.clear_edit_users()

    def change_camp_realm(self, cho_realm: Realm) -> None:
        """
        Changes the current realm of the campaign.

        Args:
            cho_realm (Realm): New realm to change current realm to.
        """
        self.c_realm = cho_realm

    def change_eventD_type(self, selected: str) -> None:
        """
        Changes display type of event

        Args:
            selected (str): Selected display type to change to
        """
        self.events_display = selected

    # New functions separate from class diagram

    def can_view(self, user: User) -> bool:
        """
        Returns bool of whether a user can view campaign

        Args:
            user (User): User to check view status

        Returns:
            bool: View status of user
        """
        return user in self._permitted_users or user in self._edit_users

    def can_edit(self, user: User) -> bool:
        """
        Returns bool of whether user has edit permission

        Args:
            user (User): User to check edit status

        Returns:
            bool: Edit status of user
        """

        return user in self._edit_users
