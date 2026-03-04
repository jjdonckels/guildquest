import tkinter as tk
from core import WorldClock
from models import User, Realm
from core.Observer import Subject

class gq_GUI(tk.Tk, Subject):
    def __init__(self):
        tk.Tk.__init__(self)
        
        # *****NEW IMPLEMENTAION FOR A3*****
        Subject.__init__(self)

        # GuildQuest data
        self.world_clock: WorldClock = WorldClock()
        self.users: dict = {}
        self.realms: dict = self._create_default_realms()
        self.current_user: User = None

        # Window Setup
        self.title("GuildQuest")
        self.geometry('800x600')

        self.container = tk.Frame(self)
        self.container.pack(fill='both', expand=True)

        # Dictionary to hold screen frames
        self.screens = {}
        self.current_screen = None  # Track current screen

        # Start with login screen
        self.show_screen("login")

    def _create_default_realms(self):
        """
        Create default realms
        """

        return {
            "Central": Realm(
                name="Central Kingdom",
                map_id=1,
                time_rule=0,
                selected_user=None,
                desc="The central realm where all adventures begin"
            ),
            "Eastern": Realm(
                name="Eastern Highlands",
                map_id=2,
                time_rule=120,
                selected_user=None,
                desc="Mountains and ancient ruins"
            ),
            "Western": Realm(
                name="Western Shores",
                map_id=3,
                time_rule=-180,
                selected_user=None,
                desc="Coastal cities and trading ports"
            )
        }

    def show_screen(self, screen_name):
        """
        Display the correct screen on the GUI

        Args:
            screen_name (str): Name of screen to be displayed
        """
        # If this screen already exists and is current, do nothing
        if self.current_screen == screen_name and screen_name in self.screens:
            return

        # Hide current screen if exists
        if self.current_screen and self.current_screen in self.screens:
            self.screens[self.current_screen].pack_forget()

        if screen_name == "login":
            from gui.screens.login_screen import LoginScreen
            if screen_name not in self.screens:
                self.screens[screen_name] = LoginScreen(self.container, self)

        elif screen_name == "main_menu":
            from gui.screens.main_menu import MainMenu
            if screen_name not in self.screens:
                self.screens[screen_name] = MainMenu(self.container, self)

        elif screen_name == "campaign":
            from gui.screens.campaign_screen import CampaignScreen
            self.screens[screen_name] = CampaignScreen(self.container, self)

        elif screen_name == "character":
            from gui.screens.character_screen import CharacterScreen
            self.screens[screen_name] = CharacterScreen(self.container, self)

        self.screens[screen_name].pack(fill='both', expand=True)
        self.current_screen = screen_name

    def refresh_screen(self, screen_name):
        """        
        Refresh a screen by destroying and recreating it.
        Useful when data has changed.

        Args:
            screen_name (str): Name of screen to be refreshed
        """
        # Destroy the old screen if it exists
        if screen_name in self.screens:
            self.screens[screen_name].destroy()
            del self.screens[screen_name]

        # Show the screen (will create it fresh)
        self.show_screen(screen_name)
