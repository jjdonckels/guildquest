from dataclasses import dataclass
from .Realm import Realm

@dataclass
class User_Settings:
    time_display: str = '12hr'
    switch_theme: bool = False
    current_realm: Realm = None

    def set_display(self, s:str) -> None:
        """
        Setter method for new time display setting

        Args:
            s (str): New time display setting
        """
        self.time_display = s

    def set_theme(self) -> None:
        """
        Setter method to change theme of GUI
        """
        
        # Not implemented
        pass
    
    def set_curr_realm(self, r: Realm) -> None:
        """
        Changes the current realm of a user

        Args:
            r (Realm): New realm to change to
        """
        
        # Not implemented
        pass