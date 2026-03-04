"""
Campaign Commands - Concrete Command Implementations

DESIGN PATTERN: Command Pattern (Concrete Commands)
Purpose: Encapsulate campaign-related actions as command objects

Each command can be executed and undone, supporting full undo/redo functionality.
"""

from core.Command import Command
from models import User, Campaign


class DeleteCampaignCommand(Command):
    """
    Command to delete a campaign (with undo support).
    
    Stores the deleted campaign so it can be restored on undo.
    """
    
    def __init__(self, user: User, campaign_index: int):
        """
        Initialize delete campaign command.
        
        Args:
            user: User who owns the campaign
            campaign_index: Index of campaign to delete in user.campaigns list
        """
        self.user = user
        self.campaign_index = campaign_index
        self.deleted_campaign: Campaign = None  # Stored for undo
    
    def execute(self) -> None:
        """
        Delete the campaign.
        
        Stores the campaign object before deleting so it can be restored.
        """
        # Store the campaign before deleting
        self.deleted_campaign = self.user.campaigns[self.campaign_index]
        
        # Delete the campaign
        self.user.delete_camp(self.campaign_index)
    
    def undo(self) -> None:
        """
        Restore the deleted campaign.
        
        Inserts the campaign back at its original position.
        """
        # Restore the campaign at its original position
        self.user.campaigns.insert(self.campaign_index, self.deleted_campaign)
    
    def get_description(self) -> str:
        """Get human-readable description."""
        if self.deleted_campaign:
            return f"Delete Campaign '{self.deleted_campaign.title}'"
        return "Delete Campaign"


class CreateCampaignCommand(Command):
    """
    Command to create a campaign (with undo support).
    
    Stores the created campaign so it can be removed on undo.
    """
    
    def __init__(self, user: User, campaign_name: str, activity: bool,
                 time, realm, events_display: str,
                 quests: list = None, permitted_users: list = None,
                 edit_users: list = None):
        """
        Initialize create campaign command.
        
        Args:
            user: User who will own the campaign
            campaign_name: Name of the campaign
            activity: Whether campaign is active
            time: GameTime for the campaign
            realm: Starting realm
            events_display: Timeline view preference
            quests: Initial quests (default: empty list)
            permitted_users: Users who can view (default: owner only)
            edit_users: Users who can edit (default: owner only)
        """
        self.user = user
        self.campaign_name = campaign_name
        self.activity = activity
        self.time = time
        self.realm = realm
        self.events_display = events_display
        self.quests = quests if quests is not None else []
        self.permitted_users = permitted_users if permitted_users is not None else []
        self.edit_users = edit_users if edit_users is not None else []
        
        self.created_campaign: Campaign = None  # Stored for undo
        self.campaign_index: int = -1  # Position where it was added
    
    def execute(self) -> None:
        """
        Create the campaign.
        
        Stores the campaign's position in the list for undo.
        """
        # Create the campaign
        self.user.create_camp(
            c_name=self.campaign_name,
            activity=self.activity,
            time=self.time,
            realm=self.realm,
            events_display=self.events_display,
            quests=self.quests,
            permitted_users=self.permitted_users,
            edit_users=self.edit_users
        )
        
        # Store reference to the created campaign (it's the last one in the list)
        self.campaign_index = len(self.user.campaigns) - 1
        self.created_campaign = self.user.campaigns[self.campaign_index]
    
    def undo(self) -> None:
        """
        Remove the created campaign.
        
        Deletes the campaign that was created by this command.
        """
        # Remove the campaign we created
        if self.campaign_index >= 0 and self.campaign_index < len(self.user.campaigns):
            self.user.campaigns.pop(self.campaign_index)
    
    def get_description(self) -> str:
        """Get human-readable description."""
        return f"Create Campaign '{self.campaign_name}'"


class RenameCampaignCommand(Command):
    """
    Command to rename a campaign (with undo support).
    
    Stores the old name so it can be restored on undo.
    """
    
    def __init__(self, campaign: Campaign, new_name: str):
        """
        Initialize rename campaign command.
        
        Args:
            campaign: Campaign to rename
            new_name: New name for the campaign
        """
        self.campaign = campaign
        self.old_name = campaign.title  # Store old name for undo
        self.new_name = new_name
    
    def execute(self) -> None:
        """Rename the campaign to new_name."""
        self.campaign.rename(self.new_name)
    
    def undo(self) -> None:
        """Restore the old campaign name."""
        self.campaign.rename(self.old_name)
    
    def get_description(self) -> str:
        """Get human-readable description."""
        return f"Rename Campaign '{self.old_name}' to '{self.new_name}'"


class ToggleCampaignStatusCommand(Command):
    """
    Command to toggle campaign status (active/archived) with undo support.
    
    Since toggle is its own inverse, execute and undo do the same thing.
    """
    
    def __init__(self, campaign: Campaign):
        """
        Initialize toggle status command.
        
        Args:
            campaign: Campaign to toggle
        """
        self.campaign = campaign
        self.old_status = campaign.activity  # Store for description
    
    def execute(self) -> None:
        """Toggle campaign status."""
        self.campaign.change_act()
    
    def undo(self) -> None:
        """Toggle back (since toggle is its own inverse)."""
        self.campaign.change_act()
    
    def get_description(self) -> str:
        """Get human-readable description."""
        action = "Archive" if self.old_status else "Activate"
        return f"{action} Campaign '{self.campaign.title}'"