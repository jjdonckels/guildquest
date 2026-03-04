"""
Quest Management Screen for GuildQuest
Allows creating, viewing, editing, and deleting quest events within a campaign
"""

import tkinter as tk
from tkinter import messagebox, ttk
from gui.screens.base_screen import BaseScreen
from core import GameTime
from utils import convert_to_realm_time


class QuestScreen(BaseScreen):
    def __init__(self, parent, app, campaign, campaign_idx):
        """
        Initialize quest management screen for a specific campaign

        Args:
            parent: Parent widget
            app: Main app reference
            campaign: The campaign to manage quests for
            campaign_idx: Index of the campaign in user's campaigns list
        """
        self.campaign = campaign
        self.campaign_idx = campaign_idx
        super().__init__(parent, app)

    def create_widgets(self):
        """
        Create the quest management screen
        """

        # Header bar
        header_frame = tk.Frame(self, bg='#1a1a1a', height=60)
        header_frame.pack(fill='x')
        header_frame.pack_propagate(False)

        # Back button
        tk.Button(
            header_frame,
            text="← Back to Campaigns",
            command=self.go_back,
            bg='#3a3a3a',
            font=('Courier', 12),
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=20, pady=15)

        # Campaign title
        tk.Label(
            header_frame,
            text=f"Quests: {self.campaign.title}",
            bg='#1a1a1a',
            fg='white',
            font=('Courier', 16, 'bold')
        ).pack(side='left', padx=20, pady=15)

        # World clock
        world_time = self.app.world_clock.get_current_time().get_fulltime()
        tk.Label(
            header_frame,
            text=f"World Clock: {world_time}",
            bg='#1a1a1a',
            fg='#00ff00',
            font=('Courier', 12)
        ).pack(side='right', padx=20, pady=15)

        # Main content area
        content_frame = tk.Frame(self, bg='#2b2b2b')
        content_frame.pack(fill='both', expand=True, padx=20, pady=20)

        # Top section: Info and Create button
        top_section = tk.Frame(content_frame, bg='#2b2b2b')
        top_section.pack(fill='x', pady=(0, 20))

        # Quest count and timeline selector
        info_frame = tk.Frame(top_section, bg='#2b2b2b')
        info_frame.pack(side='left')

        tk.Label(
            info_frame,
            text=f"Quest Events ({self.campaign.get_quest_count()})",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 20, 'bold')
        ).pack(side='left')

        tk.Label(
            info_frame,
            text=f"  •  {self.campaign.c_realm.name}",
            bg='#2b2b2b',
            fg='#888888',
            font=('Arial', 12)
        ).pack(side='left', padx=10)

        # Create quest button
        tk.Button(
            top_section,
            text="+ Create New Quest",
            command=self.show_create_quest_dialog,
            bg='#4a8a4a',
            font=('Courier', 12, 'bold'),
            width=18,
            height=2,
            relief='flat',
            highlightthickness=0
        ).pack(side='right')

        # Timeline view selector
        timeline_frame = tk.Frame(content_frame, bg='#2b2b2b')
        timeline_frame.pack(fill='x', pady=(0, 10))

        tk.Label(
            timeline_frame,
            text="View:",
            bg='#2b2b2b',
            fg='white',
            font=('Courier', 11)
        ).pack(side='left', padx=(0, 10))

        self.view_mode = tk.StringVar(value="all")

        view_buttons = [
            ("All Quests", "all"),
            ("Single Day", "day"),
            ("Week", "week"),
            ("Month", "month"),
            ("Year", "year")
        ]

        for text, mode in view_buttons:
            tk.Radiobutton(
                timeline_frame,
                text=text,
                variable=self.view_mode,
                value=mode,
                command=self.refresh_quest_list,
                bg='#2b2b2b',
                fg='white',
                selectcolor='#4a4a4a',
                font=('Arial', 10),
                activebackground='#2b2b2b',
                activeforeground='white'
            ).pack(side='left', padx=5)

        # Quests list section
        self.quests_container = tk.Frame(content_frame, bg='#2b2b2b')
        self.quests_container.pack(fill='both', expand=True)

        # Display quests
        self.refresh_quest_list()

    def refresh_quest_list(self):
        """
        Refresh the list of quests based on current view mode
        """

        # Clear existing widgets
        for widget in self.quests_container.winfo_children():
            widget.destroy()

        quests = self.campaign.get_quests()

        if not quests:
            # No quests message
            tk.Label(
                self.quests_container,
                text="No quest events yet! Create your first quest to get started.",
                bg='#2b2b2b',
                fg='#888888',
                font=('Courier', 14)
            ).pack(pady=50)
            return

        # Sort quests by time
        sorted_quests = sorted(quests, key=lambda q: q.time.to_total_minutes())

        # Filter based on view mode
        view_mode = self.view_mode.get()
        if view_mode != "all":
            sorted_quests = self.filter_quests_by_view(
                sorted_quests, view_mode)

            if not sorted_quests:
                tk.Label(
                    self.quests_container,
                    text=f"No quests in selected {view_mode} view.",
                    bg='#2b2b2b',
                    fg='#888888',
                    font=('Courier', 14)
                ).pack(pady=50)
                return

        # Create scrollable frame for quests
        canvas = tk.Canvas(self.quests_container,
                           bg='#2b2b2b', highlightthickness=0)
        scrollbar = tk.Scrollbar(
            self.quests_container, orient="vertical", command=canvas.yview)
        scrollable_frame = tk.Frame(canvas, bg='#2b2b2b')

        scrollable_frame.bind(
            "<Configure>",
            lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
        )

        canvas_window = canvas.create_window(
            (0, 0), window=scrollable_frame, anchor="nw")
        canvas.configure(yscrollcommand=scrollbar.set)

        # Bind canvas width to scrollable_frame width so cards expand to fill
        def configure_scroll_region(event):
            canvas.configure(scrollregion=canvas.bbox("all"))
            # Make the window width match the canvas width
            canvas.itemconfig(canvas_window, width=event.width)

        canvas.bind("<Configure>", configure_scroll_region)

        canvas.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        # Display each quest
        for idx, quest in enumerate(sorted_quests):
            original_idx = self.campaign.get_quests().index(quest)
            self.create_quest_card(scrollable_frame, quest, original_idx)

    def filter_quests_by_view(self, quests: list, view_mode: str):
        """
        Filter quests based on timeline view mode

        Args:
            quests (list): List of quest events
            view_mode (str): Type of view mode to show quests (quests in a week, month, etc.)
        """
        if view_mode == "all":
            return quests

        # Get the target time range (you'd typically ask user for this)
        # For now, we'll use current world clock time as reference
        current_time = self.app.world_clock.get_current_time()
        current_day = current_time.get_day()

        filtered = []

        if view_mode == "day":
            # Show quests for current day (or you could prompt for specific day)
            filtered = [q for q in quests if q.time.get_day() == current_day]

        elif view_mode == "week":
            # Show quests for current week (7 days from current day)
            week_start = current_day
            week_end = current_day + 6
            filtered = [q for q in quests if week_start <=
                        q.time.get_day() <= week_end]

        elif view_mode == "month":
            # Show quests for current month (30 days from current day)
            month_start = current_day
            month_end = current_day + 29
            filtered = [q for q in quests if month_start <=
                        q.time.get_day() <= month_end]

        elif view_mode == "year":
            # Show quests for current year (365 days from current day)
            year_start = current_day
            year_end = current_day + 364
            filtered = [q for q in quests if year_start <=
                        q.time.get_day() <= year_end]

        return filtered

    def create_quest_card(self, parent, quest, idx):
        """
        Create a card widget for a quest

        Args:
            parent (Frame): Parent window for where the card will populate
            quest (Quest_Event): Indiivdual quest to create the card for
            idx (int): Index for specific quest
        """

        # Card frame
        card = tk.Frame(parent, bg='#3a3a3a', relief='raised', bd=2)
        card.pack(fill='x', pady=8, padx=5)

        # Main content frame
        content = tk.Frame(card, bg='#3a3a3a')
        content.pack(fill='both', expand=True, padx=15, pady=12)

        # Quest title
        title_label = tk.Label(
            content,
            text=quest.name,
            bg='#3a3a3a',
            font=('Courier', 14, 'bold'),
            wraplength=600,
            justify='left',
            anchor='w'
        )
        title_label.pack(fill='x', pady=(0, 8))

        # Time info - stacked vertically
        time_container = tk.Frame(content, bg='#3a3a3a')
        time_container.pack(fill='x', pady=5)

        # World Clock time
        tk.Label(
            time_container,
            text=f"🌍 World Time: {quest.time.get_fulltime()}",
            bg='#3a3a3a',
            fg='#00ff00',
            font=('Courier', 11)
        ).pack(anchor='w')

        # Realm local time (if different) - appears below world time
        if quest.c_realm.time_rule != 0:
            local_time = convert_to_realm_time(quest.time, quest.c_realm)
            tk.Label(
                time_container,
                text=f"🏰 Local Time ({quest.c_realm.name}): {local_time.get_fulltime()}",
                bg='#3a3a3a',
                fg='#ffaa00',
                font=('Courier', 11)
            ).pack(anchor='w', pady=(2, 0))
        else:
            tk.Label(
                time_container,
                text=f"🏰 Realm: {quest.c_realm.name}",
                bg='#3a3a3a',
                fg='#cccccc',
                font=('Courier', 11)
            ).pack(anchor='w', pady=(2, 0))

        # End time if specified
        if quest.end_time != "N/A":
            tk.Label(
                content,
                text=f"⏱️  Ends: {quest.end_time}",
                bg='#3a3a3a',
                fg='#888888',
                font=('Courier', 9)
            ).pack(anchor='w', pady=(2, 5))

        # Participants and items info
        info_items = []
        if quest.partaking_users:
            info_items.append(f"👥 {len(quest.partaking_users)} participant(s)")
        if quest.reward_items:
            info_items.append(f"🎁 {len(quest.reward_items)} reward(s)")
        if quest.required_items:
            info_items.append(
                f"📦 {len(quest.required_items)} required item(s)")

        if info_items:
            tk.Label(
                content,
                text="  •  ".join(info_items),
                bg='#3a3a3a',
                fg='#888888',
                font=('Courier', 9)
            ).pack(anchor='w', pady=(0, 10))

        # Buttons row
        button_row = tk.Frame(content, bg='#3a3a3a')
        button_row.pack(fill='x')

        button_config = {
            'font': ('Courier', 9),
            'width': 10,
            'height': 1,
            'relief': 'flat',
            'bd': 0,
            'highlightthickness': 0
        }

        # Edit button
        tk.Button(
            button_row,
            text="Edit",
            command=lambda q=quest, i=idx: self.show_edit_quest_dialog(q, i),
            bg='#4a7a8a',
            **button_config
        ).pack(side='left', padx=5)

        # Delete button
        tk.Button(
            button_row,
            text="Delete",
            command=lambda i=idx: self.delete_quest(i),
            bg='#8a4a4a',
            **button_config
        ).pack(side='left', padx=5)

    def show_create_quest_dialog(self):
        """
        Show dialog to create a new quest
        """

        dialog = tk.Toplevel(self.app)
        dialog.title("Create New Quest")
        dialog.geometry("550x700")  # Increased height for end time fields
        dialog.configure(bg='#2b2b2b')
        dialog.grab_set()

        # Title
        tk.Label(
            dialog,
            text="Create New Quest Event",
            bg='#2b2b2b',
            fg='white',
            font=('Courier', 18, 'bold')
        ).pack(pady=20)

        # Form frame
        form_frame = tk.Frame(dialog, bg='#2b2b2b')
        form_frame.pack(pady=10, padx=40, fill='both', expand=True)

        # Quest name
        tk.Label(
            form_frame,
            text="Quest Name:",
            bg='#2b2b2b',
            font=('Courier', 12)
        ).grid(row=0, column=0, sticky='w', pady=10)

        name_entry = tk.Entry(form_frame, width=35, font=('Arial', 12))
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()

        # Start time section
        tk.Label(
            form_frame,
            text="Start Time:",
            bg='#2b2b2b',
            fg='white',
            font=('Courier', 12, 'bold')
        ).grid(row=1, column=0, columnspan=2, sticky='w', pady=(15, 5))

        # Day
        tk.Label(form_frame, text="Day:", bg='#2b2b2b', fg='white', font=(
            'Courier', 11)).grid(row=2, column=0, sticky='w', pady=5, padx=(20, 0))
        day_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        day_entry.insert(
            0, str(self.app.world_clock.get_current_time().get_day()))
        day_entry.grid(row=2, column=1, sticky='w', pady=5, padx=(10, 0))

        # Hour
        tk.Label(form_frame, text="Hour (0-23):", bg='#2b2b2b', fg='white',
                 font=('Courier', 11)).grid(row=3, column=0, sticky='w', pady=5, padx=(20, 0))
        hour_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        hour_entry.insert(0, "12")
        hour_entry.grid(row=3, column=1, sticky='w', pady=5, padx=(10, 0))

        # Minute
        tk.Label(form_frame, text="Minute (0-59):", bg='#2b2b2b', fg='white',
                 font=('Courier', 11)).grid(row=4, column=0, sticky='w', pady=5, padx=(20, 0))
        minute_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        minute_entry.insert(0, "0")
        minute_entry.grid(row=4, column=1, sticky='w', pady=5, padx=(10, 0))

        # End time section (optional)
        tk.Label(
            form_frame,
            text="End Time (Optional):",
            bg='#2b2b2b',
            fg='white',
            font=('Courier', 12, 'bold')
        ).grid(row=5, column=0, columnspan=2, sticky='w', pady=(15, 5))

        tk.Label(
            form_frame,
            text="Leave blank for no end time",
            bg='#2b2b2b',
            fg='#888888',
            font=('Courier', 9)
        ).grid(row=6, column=0, columnspan=2, sticky='w', pady=(0, 5))

        # End Day
        tk.Label(form_frame, text="Day:", bg='#2b2b2b', fg='white', font=(
            'Courier', 11)).grid(row=7, column=0, sticky='w', pady=5, padx=(20, 0))
        end_day_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        end_day_entry.grid(row=7, column=1, sticky='w', pady=5, padx=(10, 0))

        # End Hour
        tk.Label(form_frame, text="Hour (0-23):", bg='#2b2b2b', fg='white',
                 font=('Courier', 11)).grid(row=8, column=0, sticky='w', pady=5, padx=(20, 0))
        end_hour_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        end_hour_entry.grid(row=8, column=1, sticky='w', pady=5, padx=(10, 0))

        # End Minute
        tk.Label(form_frame, text="Minute (0-59):", bg='#2b2b2b', fg='white',
                 font=('Courier', 11)).grid(row=9, column=0, sticky='w', pady=5, padx=(20, 0))
        end_minute_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        end_minute_entry.grid(row=9, column=1, sticky='w',
                              pady=5, padx=(10, 0))

        # Realm selection
        tk.Label(
            form_frame,
            text="Realm:",
            bg='#2b2b2b',
            fg='white',
            font=('Courier', 12)
        ).grid(row=10, column=0, sticky='w', pady=(15, 5))

        realm_var = tk.StringVar(dialog)
        realm_names = list(self.app.realms.keys())
        # Default to campaign's realm
        default_realm = next((name for name, realm in self.app.realms.items(
        ) if realm == self.campaign.c_realm), realm_names[0])
        realm_var.set(default_realm)

        realm_dropdown = tk.OptionMenu(form_frame, realm_var, *realm_names)
        realm_dropdown.config(bg='#4a4a4a', fg='white',
                              font=('Arial', 11), width=30)
        realm_dropdown.grid(row=10, column=1, sticky='ew',
                            pady=(15, 5), padx=(10, 0))

        form_frame.columnconfigure(1, weight=1)

        # Buttons
        button_frame = tk.Frame(dialog, bg='#2b2b2b')
        button_frame.pack(pady=20)

        def do_create():
            name = name_entry.get().strip()

            if not name:
                messagebox.showerror("Error", "Quest name cannot be empty!")
                return

            try:
                day = int(day_entry.get())
                hour = int(hour_entry.get())
                minute = int(minute_entry.get())

                if day < 0:
                    messagebox.showerror("Error", "Day cannot be negative!")
                    return
                if hour < 0 or hour > 23:
                    messagebox.showerror(
                        "Error", "Hour must be between 0 and 23!")
                    return
                if minute < 0 or minute > 59:
                    messagebox.showerror(
                        "Error", "Minute must be between 0 and 59!")
                    return

            except ValueError:
                messagebox.showerror(
                    "Error", "Please enter valid numbers for time!")
                return

            # Create quest time
            quest_time = GameTime(day, hour, minute, 0)

            # Handle optional end time
            end_time_str = "N/A"
            end_day_val = end_day_entry.get().strip()
            end_hour_val = end_hour_entry.get().strip()
            end_minute_val = end_minute_entry.get().strip()

            # If any end time field is filled, validate all of them
            if end_day_val or end_hour_val or end_minute_val:
                try:
                    end_day = int(end_day_val) if end_day_val else day
                    end_hour = int(end_hour_val) if end_hour_val else 0
                    end_minute = int(end_minute_val) if end_minute_val else 0

                    if end_day < 0:
                        messagebox.showerror(
                            "Error", "End day cannot be negative!")
                        return
                    if end_hour < 0 or end_hour > 23:
                        messagebox.showerror(
                            "Error", "End hour must be between 0 and 23!")
                        return
                    if end_minute < 0 or end_minute > 59:
                        messagebox.showerror(
                            "Error", "End minute must be between 0 and 59!")
                        return

                    end_quest_time = GameTime(end_day, end_hour, end_minute, 0)

                    # Validate end time is after start time
                    if end_quest_time.to_total_minutes() <= quest_time.to_total_minutes():
                        messagebox.showerror(
                            "Error", "End time must be after start time!")
                        return

                    end_time_str = end_quest_time.get_fulltime()

                except ValueError:
                    messagebox.showerror(
                        "Error", "Please enter valid numbers for end time!")
                    return

            # Get selected realm
            selected_realm = self.app.realms[realm_var.get()]

            # Create quest using campaign's method
            quest = self.campaign.create_quest(
                name=name,
                time=quest_time,
                ch_realm=selected_realm,
                start_time=quest_time.get_fulltime(),
                end_time=end_time_str
            )

            messagebox.showinfo("Success", f"Quest '{name}' created!")
            dialog.destroy()

            # Refresh the quest list
            self.refresh_quest_list()

        # Bind Enter key on name entry
        name_entry.bind('<Return>', lambda e: do_create())

        tk.Button(
            button_frame,
            text="Create Quest",
            command=do_create,
            bg='#4a8a4a',
            font=('Arial', 12),
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=('Arial', 12),
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

    def show_edit_quest_dialog(self, quest, quest_idx):
        """
        Show dialog to edit a quest

        Args:
            quest (Quest_Event): Quest_Event obj to be edited
            quest_idx (int): Index of Quest_Event
        """

        dialog = tk.Toplevel(self.app)
        dialog.title("Edit Quest")
        dialog.geometry("550x700")  # Increased height for end time fields
        dialog.configure(bg='#2b2b2b')
        dialog.grab_set()

        # Title
        tk.Label(
            dialog,
            text="Edit Quest Event",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 18, 'bold')
        ).pack(pady=20)

        # Form frame
        form_frame = tk.Frame(dialog, bg='#2b2b2b')
        form_frame.pack(pady=10, padx=40, fill='both', expand=True)

        # Quest name
        tk.Label(
            form_frame,
            text="Quest Name:",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 12)
        ).grid(row=0, column=0, sticky='w', pady=10)

        name_entry = tk.Entry(form_frame, width=35, font=('Arial', 12))
        name_entry.insert(0, quest.name)
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()
        name_entry.select_range(0, tk.END)

        # Start time section
        tk.Label(
            form_frame,
            text="Start Time:",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 12, 'bold')
        ).grid(row=1, column=0, columnspan=2, sticky='w', pady=(15, 5))

        # Day
        tk.Label(form_frame, text="Day:", bg='#2b2b2b', fg='white', font=(
            'Arial', 11)).grid(row=2, column=0, sticky='w', pady=5, padx=(20, 0))
        day_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        day_entry.insert(0, str(quest.time.get_day()))
        day_entry.grid(row=2, column=1, sticky='w', pady=5, padx=(10, 0))

        # Hour
        tk.Label(form_frame, text="Hour (0-23):", bg='#2b2b2b', fg='white',
                 font=('Arial', 11)).grid(row=3, column=0, sticky='w', pady=5, padx=(20, 0))
        hour_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        hour_entry.insert(0, str(quest.time.get_hour()))
        hour_entry.grid(row=3, column=1, sticky='w', pady=5, padx=(10, 0))

        # Minute
        tk.Label(form_frame, text="Minute (0-59):", bg='#2b2b2b', fg='white',
                 font=('Arial', 11)).grid(row=4, column=0, sticky='w', pady=5, padx=(20, 0))
        minute_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        minute_entry.insert(0, str(quest.time.get_minute()))
        minute_entry.grid(row=4, column=1, sticky='w', pady=5, padx=(10, 0))

        # End time section (optional)
        tk.Label(
            form_frame,
            text="End Time (Optional):",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 12, 'bold')
        ).grid(row=5, column=0, columnspan=2, sticky='w', pady=(15, 5))

        tk.Label(
            form_frame,
            text="Leave blank for no end time",
            bg='#2b2b2b',
            fg='#888888',
            font=('Arial', 9)
        ).grid(row=6, column=0, columnspan=2, sticky='w', pady=(0, 5))

        # Parse existing end time if it exists
        end_day_val = ""
        end_hour_val = ""
        end_minute_val = ""

        if quest.end_time != "N/A":
            # Try to parse end time string like "Day 15, 14:30:00"
            try:
                parts = quest.end_time.replace(
                    "Day ", "").replace(",", "").split()
                if len(parts) >= 2:
                    end_day_val = parts[0]
                    time_parts = parts[1].split(":")
                    if len(time_parts) >= 2:
                        end_hour_val = time_parts[0]
                        end_minute_val = time_parts[1]
            except:
                pass  # If parsing fails, leave blank

        # End Day
        tk.Label(form_frame, text="Day:", bg='#2b2b2b', fg='white', font=(
            'Arial', 11)).grid(row=7, column=0, sticky='w', pady=5, padx=(20, 0))
        end_day_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        if end_day_val:
            end_day_entry.insert(0, end_day_val)
        end_day_entry.grid(row=7, column=1, sticky='w', pady=5, padx=(10, 0))

        # End Hour
        tk.Label(form_frame, text="Hour (0-23):", bg='#2b2b2b', fg='white',
                 font=('Arial', 11)).grid(row=8, column=0, sticky='w', pady=5, padx=(20, 0))
        end_hour_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        if end_hour_val:
            end_hour_entry.insert(0, end_hour_val)
        end_hour_entry.grid(row=8, column=1, sticky='w', pady=5, padx=(10, 0))

        # End Minute
        tk.Label(form_frame, text="Minute (0-59):", bg='#2b2b2b', fg='white',
                 font=('Arial', 11)).grid(row=9, column=0, sticky='w', pady=5, padx=(20, 0))
        end_minute_entry = tk.Entry(form_frame, width=10, font=('Arial', 11))
        if end_minute_val:
            end_minute_entry.insert(0, end_minute_val)
        end_minute_entry.grid(row=9, column=1, sticky='w',
                              pady=5, padx=(10, 0))

        # Realm selection
        tk.Label(
            form_frame,
            text="Realm:",
            bg='#2b2b2b',
            fg='white',
            font=('Arial', 12)
        ).grid(row=10, column=0, sticky='w', pady=(15, 5))

        realm_var = tk.StringVar(dialog)
        realm_names = list(self.app.realms.keys())
        # Set current realm
        current_realm_name = next((name for name, realm in self.app.realms.items(
        ) if realm == quest.c_realm), realm_names[0])
        realm_var.set(current_realm_name)

        realm_dropdown = tk.OptionMenu(form_frame, realm_var, *realm_names)
        realm_dropdown.config(bg='#4a4a4a', fg='white',
                              font=('Arial', 11), width=30)
        realm_dropdown.grid(row=10, column=1, sticky='ew',
                            pady=(15, 5), padx=(10, 0))

        form_frame.columnconfigure(1, weight=1)

        # Buttons
        button_frame = tk.Frame(dialog, bg='#2b2b2b')
        button_frame.pack(pady=20)

        def do_save():
            name = name_entry.get().strip()

            if not name:
                messagebox.showerror("Error", "Quest name cannot be empty!")
                return

            try:
                day = int(day_entry.get())
                hour = int(hour_entry.get())
                minute = int(minute_entry.get())

                if day < 0:
                    messagebox.showerror("Error", "Day cannot be negative!")
                    return
                if hour < 0 or hour > 23:
                    messagebox.showerror(
                        "Error", "Hour must be between 0 and 23!")
                    return
                if minute < 0 or minute > 59:
                    messagebox.showerror(
                        "Error", "Minute must be between 0 and 59!")
                    return

            except ValueError:
                messagebox.showerror(
                    "Error", "Please enter valid numbers for time!")
                return

            # Create new quest time
            new_time = GameTime(day, hour, minute, 0)

            # Handle optional end time
            end_time_str = "N/A"
            end_day_val = end_day_entry.get().strip()
            end_hour_val = end_hour_entry.get().strip()
            end_minute_val = end_minute_entry.get().strip()

            # If any end time field is filled, validate all of them
            if end_day_val or end_hour_val or end_minute_val:
                try:
                    end_day = int(end_day_val) if end_day_val else day
                    end_hour = int(end_hour_val) if end_hour_val else 0
                    end_minute = int(end_minute_val) if end_minute_val else 0

                    if end_day < 0:
                        messagebox.showerror(
                            "Error", "End day cannot be negative!")
                        return
                    if end_hour < 0 or end_hour > 23:
                        messagebox.showerror(
                            "Error", "End hour must be between 0 and 23!")
                        return
                    if end_minute < 0 or end_minute > 59:
                        messagebox.showerror(
                            "Error", "End minute must be between 0 and 59!")
                        return

                    end_quest_time = GameTime(end_day, end_hour, end_minute, 0)

                    # Validate end time is after start time
                    if end_quest_time.to_total_minutes() <= new_time.to_total_minutes():
                        messagebox.showerror(
                            "Error", "End time must be after start time!")
                        return

                    end_time_str = end_quest_time.get_fulltime()

                except ValueError:
                    messagebox.showerror(
                        "Error", "Please enter valid numbers for end time!")
                    return

            # Get selected realm
            selected_realm = self.app.realms[realm_var.get()]

            # Update quest using campaign's method
            self.campaign.update_quest(
                quest_idx,
                name=name,
                start_time=new_time.get_fulltime(),
                realm=selected_realm
            )

            # Also update the time object and end time directly
            quest.time = new_time
            quest.end_time = end_time_str

            messagebox.showinfo("Success", "Quest updated!")
            dialog.destroy()

            # Refresh the quest list
            self.refresh_quest_list()

        name_entry.bind('<Return>', lambda e: do_save())

        tk.Button(
            button_frame,
            text="Save Changes",
            command=do_save,
            bg='#4a8a4a',
            font=('Arial', 12),
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=('Arial', 12),
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

    def delete_quest(self, quest_idx):
        """
        Delete a quest

        Args:
            quest_idx (int): Index of quest to delete
        """

        quest = self.campaign.get_quests()[quest_idx]

        if messagebox.askyesno(
            "Confirm Delete",
            f"Are you sure you want to delete quest '{quest.name}'?\n\nThis action cannot be undone!"
        ):
            self.campaign.delete_quest(quest_idx)
            messagebox.showinfo("Success", "Quest deleted!")

            # Refresh the quest list
            self.refresh_quest_list()

    def go_back(self):
        """
        Go back to campaign screen
        """

        # Clear quest screen from cache to refresh campaign data
        if "campaign" in self.app.screens:
            self.app.screens["campaign"].destroy()
            del self.app.screens["campaign"]

        self.navigate_to("campaign")
