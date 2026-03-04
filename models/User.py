from typing import TYPE_CHECKING, Optional
from core import GameTime

if TYPE_CHECKING:
    from .User_Settings import User_Settings
    from .Character import Character
    from .Realm import Realm
    from .Quest_Event import Quest_Event


class User:
    def __init__(self, 
                 username: str,
                 number_of_campaigns: int,
                 user_settings: 'User_Settings',
                 campaigns: Optional[list['Campaign']] = None,
                 characters: Optional[list['Character']] = None):

        self.username = username
        self.number_of_campaigns = number_of_campaigns if number_of_campaigns is not None else len(
            campaigns)
        self.user_settings = user_settings
        self.campaigns = campaigns if campaigns is not None else []
        self.characters = characters if characters is not None else []

    def create_camp(self, c_name: str,
                    activity: bool,
                    time: GameTime,
                    realm: 'Realm',
                    events_display: str,
                    quests: list['Quest_Event'],
                    # should be list of users and same for edit_users
                    permitted_users: list['User'],
                    edit_users: list['User']) -> None:
        """
        Creates a new campaign for the user

        Args:
            c_name (str): Campaign name
            activity (bool): Campaign's activity status
            time (GameTime): GameTime object for the Campaign's time
            realm (Realm): Realm of the new Campaign
            events_display (str): Event display type of campaign
            quests (list[&#39;Quest_Event&#39;]): List of Quest_Events for Campaign
            permitted_users (list[&#39;User&#39;]): List of permitted users for Campaign
            edit_users (list[&#39;User&#39;]): List of users with edit permissions for Campaign
        """

        from .Campaign import Campaign  # avoids circular import at module level

        new_camp = Campaign(c_name, activity, time, realm,
                            events_display, quests, permitted_users, edit_users)

        self.campaigns.append(new_camp)

    def update_camp(self, c_num: int, name: str, change_act_status: bool) -> None:
        """ 
        Updates a campaigns attributes (name and activity status)

        Args:
            c_num (int): Campaign identifier
            name (str): New name for campaign
            change_act_status (bool): New activity status of campaign

        Raises:
            IndexError: _description_
        """
        if c_num < 0 or c_num >= len(self.campaigns):
            raise IndexError("Quest index out of range")

        camp = self.campaigns[c_num]

        if name is not None:
            camp.rename(name)

        if not change_act_status:
            camp.change_act()

    def delete_camp(self, c_num: int) -> None:
        """ 
        Deletes a campaign

        Args:
            c_num (int): Campaign identifier
        """
        if c_num < 0 or c_num >= len(self.campaigns):
            raise IndexError("Quest index out of range")

        self.campaigns.pop(c_num)
