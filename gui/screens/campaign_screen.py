"""
Campaign management screen for GuildQuest: Allows creating, viewing, editing, and deleting campaigns
"""

import tkinter as tk
from tkinter import messagebox, ttk
from gui.screens.base_screen import BaseScreen
from gui.themes import Fonts, Colors
from core.CommandManager import CommandManager
from commands import (
    DeleteCampaignCommand,
    CreateCampaignCommand,
    RenameCampaignCommand,
    ToggleCampaignStatusCommand
)


class CampaignScreen(BaseScreen):
    # *****NEW IMPLEMENTAION FOR A3*****
    def __init__(self, parent, app):
        """Initialize campaign screen with command manager for undo/redo"""
        # Initialize command manager BEFORE calling super().__init__
        # because super().__init__ calls create_widgets()
        self.command_manager = CommandManager()
        super().__init__(parent, app)

    def create_widgets(self):
        """
        Create the campaign management screen
        """

        # Header bar
        header_frame = tk.Frame(self, bg='#1a1a1a', height=60)
        header_frame.pack(fill='x')
        header_frame.pack_propagate(False)

        # Back button
        tk.Button(
            header_frame,
            text="← Back to Main Menu",
            command=lambda: self.navigate_to("main_menu"),
            bg='#3a3a3a',
            font=Fonts.SMALL_COURIER,
            relief='flat'
        ).pack(side='left', padx=10, pady=15)

        # Title
        tk.Label(
            header_frame,
            text="Campaign Management",
            bg='#1a1a1a',
            fg='white',
            font=('Courier', 16, 'bold')
        ).pack(side='left', padx=15, pady=15)

        # World clock
        world_time = self.app.world_clock.get_current_time().get_fulltime()
        tk.Label(
            header_frame,
            text=f"World Clock: {world_time}",
            bg='#1a1a1a',
            fg=Colors.GUILD_QUEST_GREEN,
            font=Fonts.SMALL_COURIER
        ).pack(side='right', padx=15, pady=15)

        # Main content area
        content_frame = tk.Frame(self, bg=Colors.DARK_GRAY)
        content_frame.pack(fill='both', expand=True, padx=20, pady=20)

        # Top section: Title and Create button
        top_section = tk.Frame(content_frame, bg=Colors.DARK_GRAY)
        top_section.pack(fill='x', pady=(0, 20))

        tk.Label(
            top_section,
            text=f"{self.app.current_user.username}'s Campaigns",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=('Courier', 20, 'bold')
        ).pack(side='left', padx=10)

        # Button row on the right
        button_row = tk.Frame(top_section, bg=Colors.DARK_GRAY)
        button_row.pack(side='right')

        # Create button
        tk.Button(
            button_row,
            text="+ Create New Campaign",
            command=self.show_create_campaign_dialog,
            bg='#4a8a4a',
            font=('Courier', 12, 'bold'),
            width=20,
            height=2
        ).pack(side='left', padx=5)

        # *****NEW IMPLEMENTAION FOR A3*****
        # Undo button
        self.undo_btn = tk.Button(
            button_row,
            text="↶ Undo",
            command=self.undo_last_action,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            state='disabled'  # Initially disabled
        )
        self.undo_btn.pack(side='left', padx=5)

        # *****NEW IMPLEMENTAION FOR A3*****
        # Redo button
        self.redo_btn = tk.Button(
            button_row,
            text="↷ Redo",
            command=self.redo_last_action,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            state='disabled'  # Initially disabled
        )
        self.redo_btn.pack(side='left', padx=5)

        # Campaigns list section
        self.campaigns_container = tk.Frame(content_frame, bg=Colors.DARK_GRAY)
        self.campaigns_container.pack(fill='both', expand=True)

        # Display campaigns
        self.refresh_campaigns_list()

        # *****NEW IMPLEMENTAION FOR A3*****
        self.update_undo_redo_buttons()

    def refresh_campaigns_list(self):
        """
        Refresh the list of campaigns
        """

        # Clear existing widgets
        for widget in self.campaigns_container.winfo_children():
            widget.destroy()

        campaigns = self.app.current_user.campaigns

        if not campaigns:
            # No campaigns message
            tk.Label(
                self.campaigns_container,
                text="No campaigns yet! Create your first campaign to get started.",
                bg=Colors.DARK_GRAY,
                fg=Colors.LIGHT_GRAY,
                font=('courier', 14)
            ).pack(pady=50)
        else:
            # Create scrollable frame for campaigns
            canvas = tk.Canvas(self.campaigns_container,
                               bg=Colors.DARK_GRAY, highlightthickness=0)
            scrollbar = tk.Scrollbar(
                self.campaigns_container, orient="vertical", command=canvas.yview)
            scrollable_frame = tk.Frame(canvas, bg=Colors.DARK_GRAY)

            scrollable_frame.bind(
                "<Configure>",
                lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
            )

            canvas.create_window((0, 0), window=scrollable_frame, anchor="nw")
            canvas.configure(yscrollcommand=scrollbar.set)

            canvas.pack(side="left", fill="both", expand=True)
            scrollbar.pack(side="right", fill="y")

            # Display each campaign
            for idx, campaign in enumerate(campaigns):
                self.create_campaign_card(scrollable_frame, campaign, idx)

    def create_campaign_card(self, parent, campaign, idx):
        """
        Create a card widget for a campaign
        """

        # Card frame
        card = tk.Frame(parent, bg='#3a3a3a', relief='raised', bd=2)
        card.pack(fill='x', pady=10, padx=5)

        # Main content frame
        content = tk.Frame(card, bg='#3a3a3a')
        content.pack(fill='both', expand=True, padx=15, pady=10)

        # Top row: Title and status
        top_row = tk.Frame(content, bg='#3a3a3a')
        top_row.pack(fill='x', pady=(0, 5))

        # Campaign title
        title_label = tk.Label(
            top_row,
            text=campaign.title,
            bg='#3a3a3a',
            fg='white',
            font=('Courier', 16, 'bold'),
            wraplength=600,
            justify='left',
            anchor='w'
        )
        title_label.pack(side='left', fill='x', expand=True)

        # Status badge
        status_color = '#4a8a4a' if campaign.activity else '#8a4a4a'
        status_text = 'Active' if campaign.activity else 'Archived'
        status_badge = tk.Label(
            top_row,
            text=status_text,
            bg=status_color,
            fg='white',
            font=('courier', 10, 'bold'),
            padx=10,
            pady=2
        )
        status_badge.pack(side='left', padx=10)

        # Info row
        info_row = tk.Frame(content, bg='#3a3a3a')
        info_row.pack(fill='x', pady=5)

        info_text = f"🏰 Realm: {campaign.c_realm.name}  |  📅 Started: {campaign.time.get_fulltime()}  |  📋 Quests: {campaign.get_quest_count()}"
        tk.Label(
            info_row,
            text=info_text,
            bg='#3a3a3a',
            fg='#cccccc',
            font=Fonts.SMALL_COURIER
        ).pack(side='left')

        # Buttons row
        button_row = tk.Frame(content, bg='#3a3a3a')
        button_row.pack(fill='x', pady=(10))

        button_config = {
            'font': Fonts.SMALL_COURIER,
            'width': 12,
            'height': 1,
            'relief': 'flat',
            'bd': 0,
            'highlightthickness': 0
        }

        # View/Manage Quests button
        tk.Button(
            button_row,
            text="Manage Quests",
            command=lambda c=campaign, i=idx: self.show_quest_management(c, i),
            bg='#4a7a8a',
            **button_config
        ).pack(side='left', padx=5)

        # Rename button
        tk.Button(
            button_row,
            text="Rename",
            command=lambda c=campaign: self.show_rename_dialog(c),
            bg='#4a4a4a',
            **button_config
        ).pack(side='left', padx=5)

        # Toggle Active/Archive button
        toggle_text = "Archive" if campaign.activity else "Activate"
        tk.Button(
            button_row,
            text=toggle_text,
            command=lambda c=campaign: self.toggle_campaign_status(c),
            bg='#4a4a4a',
            **button_config
        ).pack(side='left', padx=5)

        # Delete button
        tk.Button(
            button_row,
            text="Delete",
            command=lambda i=idx: self.delete_campaign(i),
            bg='#8a4a4a',
            **button_config
        ).pack(side='left', padx=5)

    # *****NEW IMPLEMENTAION FOR A3*****

    def show_create_campaign_dialog(self):
        """
        Show dialog to create a new campaign
        """
        dialog = self._create_dialog_modal("Create New Campaign", "500x400")
        form_frame = self._create_form_frame(dialog)

        # Create all form fields
        name_entry = self._create_campaign_name_field(form_frame)
        realm_var = self._create_realm_selector(form_frame)
        timeline_var = self._create_timeline_selector(form_frame)

        form_frame.columnconfigure(1, weight=1)

        # Create action buttons
        self._create_dialog_buttons(
            dialog,
            lambda: self._handle_campaign_creation(
                dialog, name_entry, realm_var, timeline_var),
            name_entry
        )

    ############################################################
    # NEW HELPER METHODS TO EXTRACT show_create_campaign_dialog
    ############################################################

    def _create_dialog_modal(self, title: str, dimensions: str) -> tk.Toplevel:
        """
        Create an already styled dialog modal window

        Extracted from show_create_campaign_dialog to reduce complexity.
        This method handles all dialog window setup including title,
        size, styling, and modal behavior.

        Args:
            title (str): Dialog window title
            dimensions (str): Window size (e.g., "500x400")

        Returns:
            tk.Toplevel: Configured dialog window
        """
        dialog = tk.Toplevel(self.app)
        dialog.title(title)
        dialog.geometry(dimensions)
        dialog.configure(bg=Colors.DARK_GRAY)
        dialog.grab_set()  # Make dialog modal

        # Title
        tk.Label(
            dialog,
            text=title,
            bg=Colors.DARK_GRAY,
            fg='white',
            font=('Courier', 18, 'bold')
        ).pack(pady=20)

        return dialog

    def _create_form_frame(self, parent: tk.Widget) -> tk.Frame:
        """
        Create the form container frame

        Extracted from show_create_campaign_dialog().
        Creates a styled frame to hold all form input fields.

        Args:
            parent: Parent widget (usually the dialog window)

        Returns:
            tk.Frame: Form frame configured and ready for field placement
        """
        form_frame = tk.Frame(parent, bg=Colors.DARK_GRAY)
        form_frame.pack(pady=10, padx=40, fill='both', expand=True)
        return form_frame

    def _create_campaign_name_field(self, parent: tk.Frame) -> tk.Entry:
        """
        Create campaign name input field with label

        Extracted from show_create_campaign_dialog().
        Creates the "Campaign Name:" label and text entry widget.

        Args:
            parent: Parent frame (the form_frame)

        Returns:
            tk.Entry: Entry widget for campaign name input
        """
        # Label
        tk.Label(
            parent,
            text="Campaign Name:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=0, column=0, sticky='w', pady=10)

        # Entry field
        name_entry = tk.Entry(parent, width=30, font=Fonts.SMALL_COURIER)
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()  # Set initial focus to this field

        return name_entry

    def _create_realm_selector(self, parent: tk.Frame) -> tk.StringVar:
        """
        Create realm dropdown with dynamic description label

        Extracted from show_create_campaign_dialog().
        Creates the realm selection dropdown and description label that
        updates automatically when the user changes their selection.

        Args:
            parent: Parent frame (the form_frame)

        Returns:
            tk.StringVar: Variable holding the selected realm name
        """
        # Realm selection label
        tk.Label(
            parent,
            text="Starting Realm:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=1, column=0, sticky='w', pady=10)

        # Realm dropdown
        realm_var = tk.StringVar()
        realm_names = list(self.app.realms.keys())
        realm_var.set(realm_names[0])  # Default to first realm

        realm_dropdown = tk.OptionMenu(parent, realm_var, *realm_names)
        realm_dropdown.config(
            bg='#4a4a4a',
            fg='white',
            font=Fonts.SMALL_COURIER,
            width=25
        )
        realm_dropdown.grid(
            row=1, column=1,
            sticky='ew',
            pady=10,
            padx=(10, 0)
        )

        # Realm description label (updates dynamically)
        realm_desc_label = tk.Label(
            parent,
            text=self.app.realms[realm_var.get()].desc,
            bg=Colors.DARK_GRAY,
            fg=Colors.LIGHT_GRAY,
            font=('Courier', 9),
            wraplength=350,
            justify='left'
        )
        realm_desc_label.grid(
            row=2, column=0,
            columnspan=2,
            sticky='w',
            pady=(0, 10)
        )

        # Callback to update description when realm selection changes
        def update_realm_desc(*args):
            realm_desc_label.config(
                text=self.app.realms[realm_var.get()].desc
            )

        realm_var.trace_add('write', update_realm_desc)

        return realm_var

    def _create_timeline_selector(self, parent: tk.Frame) -> tk.StringVar:
        """
        Create timeline view dropdown (day/week/month/year)

        Extracted from show_create_campaign_dialog().
        Creates the timeline view preference dropdown.

        Args:
            parent: Parent frame (the form_frame)

        Returns:
            tk.StringVar: Variable holding the selected timeline view
        """
        # Timeline view label
        tk.Label(
            parent,
            text="Default Timeline View:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=3, column=0, sticky='w', pady=10)

        # Timeline dropdown
        timeline_var = tk.StringVar()
        timeline_options = ["day", "week", "month", "year"]
        timeline_var.set("day")  # Default to day view

        timeline_dropdown = tk.OptionMenu(
            parent, timeline_var, *timeline_options)
        timeline_dropdown.config(
            bg='#4a4a4a',
            fg='white',
            font=Fonts.SMALL_COURIER,
            width=25
        )
        timeline_dropdown.grid(
            row=3, column=1,
            sticky='ew',
            pady=10,
            padx=(10, 0)
        )

        return timeline_var

    def _create_dialog_buttons(self, dialog: tk.Toplevel,
                               create_callback, name_entry: tk.Entry):
        """
        Create dialog action buttons (Create and Cancel)

        Extracted from show_create_campaign_dialog().
        Creates the button frame with Create and Cancel buttons,
        and binds the Enter key to the create action.

        Args:
            dialog: The dialog window
            create_callback: Function to call when Create button is clicked
            name_entry: Entry widget to bind Enter key to
        """
        button_frame = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        button_frame.pack(pady=20)

        # Bind Enter key to create action for convenience
        name_entry.bind('<Return>', lambda e: create_callback())

        # Create button
        tk.Button(
            button_frame,
            text="Create Campaign",
            command=create_callback,
            bg='#4a8a4a',
            fg=Colors.DARK_GRAY,
            font=Fonts.SMALL_COURIER,
            width=15
        ).pack(side='left', padx=5)

        # Cancel button
        # Button text color appears white for some reason here but works fine
        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER,
            width=15
        ).pack(side='left', padx=5)

    def _handle_campaign_creation(self, dialog: tk.Toplevel,
                                  name_entry: tk.Entry,
                                  realm_var: tk.StringVar,
                                  timeline_var: tk.StringVar):
        """
        Handle campaign creation with validation

        Extracted from show_create_campaign_dialog().
        Validates user input, creates the campaign, displays success message,
        closes the dialog, and refreshes the UI.

        Args:
            dialog: Dialog window to close on success
            name_entry: Entry widget containing the campaign name
            realm_var: StringVar containing the selected realm name
            timeline_var: StringVar containing the selected timeline view
        """
        # Get and validate campaign name
        name = name_entry.get().strip()

        if not name:
            messagebox.showerror("Error", "Campaign name cannot be empty!")
            return

        # Get selected realm object
        selected_realm = self.app.realms[realm_var.get()]

        # Get current time from world clock
        current_time = self.app.world_clock.get_current_time()

        # *****NEW IMPLEMENTAION FOR A3*****
        # Create campaign using Command pattern for undo support
        command = CreateCampaignCommand(
            user=self.app.current_user,
            campaign_name=name,
            activity=True,
            time=current_time,
            realm=selected_realm,
            events_display=timeline_var.get(),
            quests=[],
            permitted_users=[self.app.current_user],
            edit_users=[self.app.current_user]
        )
        
        # *****NEW IMPLEMENTAION FOR A3*****
        self.command_manager.execute(command)

        # *****NEW IMPLEMENTAION FOR A3*****
        self.app.notify('campaign_created')

        # Show success message to user
        messagebox.showinfo("Success", f"Campaign '{name}' created!")

        # Close the dialog
        dialog.destroy()

        # Refresh the campaigns list to show the new campaign
        self.refresh_campaigns_list()

        # *****NEW IMPLEMENTAION FOR A3*****
        # Update buttons after command is executed
        self.update_undo_redo_buttons()

    ############################################################
    # END OF HELPER METHODS FOR show_create_campaign_dialog
    ############################################################

    def show_rename_dialog(self, campaign):
        """
        Show dialog to rename a campaign
        """

        # *****NEW IMPLEMENTAION FOR A3*****
        dialog = self.create_dialog("Rename Campaign", 400, 250)
        self.create_dialog_title(dialog, "Rename Campaign")

        tk.Label(
            dialog,
            text=f"Current name: {campaign.title}",
            bg=Colors.DARK_GRAY,
            font=Fonts.SMALL_COURIER
        ).pack(pady=5)

        tk.Label(
            dialog,
            text="New name:",
            bg=Colors.DARK_GRAY,
            font=Fonts.SMALL_COURIER
        ).pack(pady=5)

        name_entry = tk.Entry(dialog, width=30, font=Fonts.SMALL_COURIER)
        name_entry.insert(0, campaign.title)
        name_entry.pack(pady=10)
        name_entry.focus()
        name_entry.select_range(0, tk.END)  # Select all text

        def do_rename():
            new_name = name_entry.get().strip()

            if not new_name:
                messagebox.showerror("Error", "Name cannot be empty!")
                return

            # *****NEW IMPLEMENTAION FOR A3*****
            # Rename using Command pattern for undo support
            command = RenameCampaignCommand(campaign, new_name)
            self.command_manager.execute(command)
            self.update_undo_redo_buttons()

            messagebox.showinfo(
                "Success", f"Campaign renamed to '{new_name}'!")
            dialog.destroy()

            # Refresh the campaigns list
            self.refresh_campaigns_list()

            self.update_undo_redo_buttons()

        name_entry.bind('<Return>', lambda e: do_rename())

        button_frame = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        button_frame.pack(pady=20)

        tk.Button(
            button_frame,
            text="Rename",
            command=do_rename,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            width=12
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            width=12
        ).pack(side='left', padx=5)

    def toggle_campaign_status(self, campaign):
        """Toggle campaign status using Command pattern for undo support"""

        # *****NEW IMPLEMENTAION FOR A3*****
        # Create and execute toggle command
        command = ToggleCampaignStatusCommand(campaign)
        self.command_manager.execute(command)

        status = "active" if campaign.activity else "archived"
        self.app.notify('campaign_status_changed')

        messagebox.showinfo("Success", f"Campaign is now {status}!")

        # Refresh the UI
        self.refresh_campaigns_list()
        self.update_undo_redo_buttons()

    def delete_campaign(self, campaign_idx):
        """Delete a campaign using Command pattern for undo support"""
        campaign = self.app.current_user.campaigns[campaign_idx]

        if messagebox.askyesno(
            "Confirm Delete",
            f"Are you sure you want to delete '{campaign.title}'?\n\n"
            f"This will delete all {campaign.get_quest_count()} quest(s) in this campaign.\n\n"
            f"This action can be undone using the Undo button."
        ):
            # *****NEW IMPLEMENTAION FOR A3*****
            # Create and execute delete command
            command = DeleteCampaignCommand(
                self.app.current_user, campaign_idx)
            self.command_manager.execute(command)

            # *****NEW IMPLEMENTAION FOR A3*****
            # Notify observers
            self.app.notify('campaign_deleted')

            messagebox.showinfo("Success",
                                f"Campaign '{campaign.title}' deleted!\n"
                                f"Press Undo to restore it."
                                )

            # Refresh the UI
            self.refresh_campaigns_list()
            self.update_undo_redo_buttons()

    def show_quest_management(self, campaign, campaign_idx):
        """
        Navigate to quest management for this campaign
        """

        # Imports here to avoid circular import
        from gui.screens.quest_screen import QuestScreen

        # Create a unique key for this campaign's quest screen
        screen_key = f"quest_{campaign_idx}"

        # If screen exists, remove it to force refresh
        if screen_key in self.app.screens:
            self.app.screens[screen_key].destroy()
            del self.app.screens[screen_key]

        # Create new quest screen
        quest_screen = QuestScreen(
            self.app.container, self.app, campaign, campaign_idx)
        self.app.screens[screen_key] = quest_screen

        # Hide current screen
        if self.app.current_screen and self.app.current_screen in self.app.screens:
            self.app.screens[self.app.current_screen].pack_forget()

        # Show quest screen
        quest_screen.pack(fill='both', expand=True)
        self.app.current_screen = screen_key

    # *****NEW IMPLEMENTAION FOR A3*****
    def undo_last_action(self):
        """Undo the last command"""
        if self.command_manager.can_undo():
            # Schedule undo for after button click event finishes
            self.after(10, self._perform_undo)
        else:
            messagebox.showwarning("Undo", "Nothing to undo!")

    # *****NEW IMPLEMENTAION FOR A3*****
    def redo_last_action(self):
        """Redo the last undone command"""
        if self.command_manager.can_redo():
            # Schedule redo for after button click event finishes
            self.after(10, self._perform_redo)
        else:
            messagebox.showwarning("Redo", "Nothing to redo!")

    # *****NEW IMPLEMENTAION FOR A3*****
    def _perform_undo(self):
        """Actually perform the undo (called after event finishes)"""
        if self.command_manager.undo():
            # Schedule UI updates with delays to avoid crash
            self.after(10, self.refresh_campaigns_list)
            self.after(20, self.update_undo_redo_buttons)
            self.after(30, lambda: messagebox.showinfo(
                "Undo", "Action undone!"))
        else:
            messagebox.showwarning("Undo failed")

    # *****NEW IMPLEMENTAION FOR A3*****
    def _perform_redo(self):
        """Actually perform the redo (called after event finishes)"""
        if self.command_manager.redo():

            # Schedule UI updates with delays to avoid crash
            self.after(10, self.refresh_campaigns_list)
            self.after(20, self.update_undo_redo_buttons)
            self.after(30, lambda: messagebox.showinfo(
                "Redo", "Action redone!"))
        else:
            messagebox.showwarning("Redo failed")

    # *****NEW IMPLEMENTAION FOR A3*****
    def update_undo_redo_buttons(self):
        """Update undo/redo button states"""
        # Update undo button - just enable/disable, no description
        if self.command_manager.can_undo():
            self.undo_btn.config(state='normal', text="↶ Undo")
        else:
            self.undo_btn.config(state='disabled', text="↶ Undo")

        # Update redo button - just enable/disable, no description
        if self.command_manager.can_redo():
            self.redo_btn.config(state='normal', text="↷ Redo")
        else:
            self.redo_btn.config(state='disabled', text="↷ Redo")
