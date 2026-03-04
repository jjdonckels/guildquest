"""
Character management screen for GuildQuest: Allows creating, viewing, editing, and deleting characters and their inventory
"""

import tkinter as tk
from tkinter import messagebox
from gui.screens.base_screen import BaseScreen
from gui.themes import Fonts, Colors
from models import Item
from models.CharacterFactoryRegistry import CharacterFactoryRegistry
from core.CommandManager import CommandManager
from commands import DeleteCharacterCommand, CreateCharacterCommand


class CharacterScreen(BaseScreen):

    # *****NEW IMPLEMENTAION FOR A3*****
    def __init__(self, parent, app):
        """Initialize character screen with command manager for undo/redo"""
        self.command_manager = CommandManager()
        super().__init__(parent, app)

    def create_widgets(self):
        """
        Create the character management screen
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
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=20, pady=15)

        # Title
        tk.Label(
            header_frame,
            text="Character Management",
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
            fg=Colors.GUILD_QUEST_GREEN,
            font=Fonts.SMALL_COURIER
        ).pack(side='right', padx=5, pady=15)

        # Main content area
        content_frame = tk.Frame(self, bg=Colors.DARK_GRAY)
        content_frame.pack(fill='both', expand=True, padx=20, pady=20)

        # Top section: Title and Create button
        top_section = tk.Frame(content_frame, bg=Colors.DARK_GRAY)
        top_section.pack(fill='x', pady=(0, 20))

        tk.Label(
            top_section,
            text=f"{self.app.current_user.username}'s Characters",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=('Courier', 20, 'bold')
        ).pack(side='left', padx=20)

        # Button row on the right
        button_row = tk.Frame(top_section, bg=Colors.DARK_GRAY)
        button_row.pack(side='right')

        tk.Button(
            top_section,
            text="+ Create New Character",
            command=self.show_create_character_dialog,
            bg='#4a8a4a',
            font=('Courier', 12, 'bold'),
            width=20,
            height=2,
            relief='flat',
            highlightthickness=0
        ).pack(side='right')

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

        # Characters list section
        self.characters_container = tk.Frame(content_frame, bg=Colors.DARK_GRAY)
        self.characters_container.pack(fill='both', expand=True)

        # Display characters
        self.refresh_characters_list()

        # *****NEW IMPLEMENTAION FOR A3*****
        self.update_undo_redo_buttons()

    def refresh_characters_list(self):
        """
        Refresh the list of characters
        """

        # Clear existing widgets
        for widget in self.characters_container.winfo_children():
            widget.destroy()

        characters = self.app.current_user.characters

        if not characters:
            # No characters message
            tk.Label(
                self.characters_container,
                text="No characters yet! Create your first character to get started.",
                bg=Colors.DARK_GRAY,
                fg='#888888',
                font=('Courier', 14)
            ).pack(pady=50)
        else:
            # Create scrollable frame for characters
            canvas = tk.Canvas(self.characters_container,
                               bg=Colors.DARK_GRAY, highlightthickness=0)
            scrollbar = tk.Scrollbar(
                self.characters_container, orient="vertical", command=canvas.yview)
            scrollable_frame = tk.Frame(canvas, bg=Colors.DARK_GRAY)

            scrollable_frame.bind(
                "<Configure>",
                lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
            )

            canvas_window = canvas.create_window(
                (0, 0), window=scrollable_frame, anchor="nw")
            canvas.configure(yscrollcommand=scrollbar.set)

            # Bind canvas width to scrollable_frame width
            def configure_scroll_region(event):
                canvas.configure(scrollregion=canvas.bbox("all"))
                canvas.itemconfig(canvas_window, width=event.width)

            canvas.bind("<Configure>", configure_scroll_region)

            canvas.pack(side="left", fill="both", expand=True)
            scrollbar.pack(side="right", fill="y")

            # Display each character
            for idx, character in enumerate(characters):
                self.create_character_card(scrollable_frame, character, idx)

    def create_character_card(self, parent, character, idx):
        """
        Create a card widget for a character
        """

        # Card frame
        card = tk.Frame(parent, bg='#3a3a3a', relief='raised', bd=2)
        card.pack(fill='x', pady=10, padx=5)

        # Main content frame
        content = tk.Frame(card, bg='#3a3a3a')
        content.pack(fill='both', expand=True, padx=15, pady=10)

        # Top row: Name and Level
        top_row = tk.Frame(content, bg='#3a3a3a')
        top_row.pack(fill='x', pady=(0, 5))

        # Character name
        tk.Label(
            top_row,
            text=character.name,
            bg='#3a3a3a',
            fg='white',
            font=('Courier', 16, 'bold'),
            wraplength=500,
            justify='left',
            anchor='w'
        ).pack(side='left', fill='x', expand=True)

        # Level badge
        tk.Label(
            top_row,
            text=f"Level {character.level}",
            bg='#4a6a8a',
            fg='white',
            font=('Courier', 12, 'bold'),
            padx=15,
            pady=5
        ).pack(side='left', padx=10)

        # Class row
        tk.Label(
            content,
            text=f"⚔️ {character.character_class}",
            bg='#3a3a3a',
            fg='#cccccc',
            font=Fonts.SMALL_COURIER
        ).pack(anchor='w', pady=5)

        # Inventory info
        inventory_count = len(character.curr_inventory.items)
        tk.Label(
            content,
            text=f"🎒 Inventory: {inventory_count} item(s)",
            bg='#3a3a3a',
            fg='#888888',
            font=Fonts.SMALL_COURIER
        ).pack(anchor='w', pady=5)

        # Buttons row
        button_row = tk.Frame(content, bg='#3a3a3a')
        button_row.pack(fill='x', pady=(10, 0))

        button_config = {
            'font': Fonts.SMALL_COURIER,
            'width': 12,
            'height': 1,
            'relief': 'flat',
            'bd': 0,
            'highlightthickness': 0
        }

        # Manage Inventory button
        tk.Button(
            button_row,
            text="Manage Inventory",
            command=lambda c=character, i=idx: self.show_inventory_management(
                c, i),
            bg='#4a7a8a',
            **button_config
        ).pack(side='left', padx=5)

        # Edit button
        tk.Button(
            button_row,
            text="Edit",
            command=lambda c=character, i=idx: self.show_edit_character_dialog(
                c, i),
            bg='#4a4a4a',
            **button_config
        ).pack(side='left', padx=5)

        # Delete button
        tk.Button(
            button_row,
            text="Delete",
            command=lambda i=idx: self.delete_character(i),
            bg='#8a4a4a',
            **button_config
        ).pack(side='left', padx=5)

    def show_create_character_dialog(self):
        """
        Show dialog to create a new character
        """
        # *****NEW IMPLEMENTAION FOR A3*****
        dialog = self.create_dialog("Create New Character", 500, 450)
        self.create_dialog_title(dialog, "Create New Character")
        form_frame = self.create_form_frame(dialog)

        # Character name
        tk.Label(
            form_frame,
            text="Character Name:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=0, column=0, sticky='w', pady=10)

        name_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()

        # Character class
        tk.Label(
            form_frame,
            text="Class:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=1, column=0, sticky='w', pady=10)

        class_var = tk.StringVar(dialog)
        class_options = ["Warrior", "Mage", "Rogue",
                         "Cleric", "Ranger", "Paladin", "Bard", "Druid"]
        class_var.set(class_options[0])

        class_dropdown = tk.OptionMenu(form_frame, class_var, *class_options)
        class_dropdown.config(bg='#4a4a4a', fg='white',
                              font=('Courier', 11), width=25)
        class_dropdown.grid(row=1, column=1, sticky='ew',
                            pady=10, padx=(10, 0))

        # Level
        tk.Label(
            form_frame,
            text="Starting Level:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=2, column=0, sticky='w', pady=10)

        level_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        level_entry.insert(0, "1")
        level_entry.grid(row=2, column=1, sticky='ew', pady=10, padx=(10, 0))

        form_frame.columnconfigure(1, weight=1)

        # *****NEW IMPLEMENTAION FOR A3*****
        # Button frame
        button_frame = self.create_button_frame(dialog)

        def do_create():
            name = name_entry.get().strip()

            if not name:
                messagebox.showerror(
                    "Error", "Character name cannot be empty!")
                return

            try:
                level = int(level_entry.get())
                if level < 1:
                    messagebox.showerror("Error", "Level must be at least 1!")
                    return
                if level > 100:
                    messagebox.showerror("Error", "Level cannot exceed 100!")
                    return
            except ValueError:
                messagebox.showerror(
                    "Error", "Please enter a valid level number!")
                return

            # Create character (curr_inventory auto-created by default_factory)
            # *****NEW IMPLEMENTAION FOR A3*****
            character = CharacterFactoryRegistry.create_character(
                character_class=class_var.get(),
                name=name,
                level=level
            )

            # *****NEW IMPLEMENTAION FOR A3*****
            # Handle character creation through command pattern
            command = CreateCharacterCommand(self.app.current_user, character)
            self.command_manager.execute(command)
            self.update_undo_redo_buttons()

            # *****NEW IMPLEMENTAION FOR A3*****
            self.app.notify("characters_changed")

            messagebox.showinfo("Success", f"Character '{name}' created!")
            dialog.destroy()

            self.refresh_characters_list()

            # *****NEW IMPLEMENTAION FOR A3*****
            self.update_undo_redo_buttons()

        name_entry.bind('<Return>', lambda e: do_create())

        tk.Button(
            button_frame,
            text="Create Character",
            command=do_create,
            bg='#4a8a4a',
            font=Fonts.SMALL_COURIER,
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

    def show_edit_character_dialog(self, character, char_idx):
        """
        Show dialog to edit a character
        """

        dialog = self.create_dialog("Edit Character", 500, 450)
        self.create_dialog_title(dialog, "Edit Character")
        form_frame = self.create_form_frame(dialog)

        # Character name
        tk.Label(
            form_frame,
            text="Character Name:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=0, column=0, sticky='w', pady=10)

        name_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        name_entry.insert(0, character.name)
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()
        name_entry.select_range(0, tk.END)

        # Character class
        tk.Label(
            form_frame,
            text="Class:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=1, column=0, sticky='w', pady=10)

        class_var = tk.StringVar(dialog)
        class_options = ["Warrior", "Mage", "Rogue",
                         "Cleric", "Ranger", "Paladin", "Bard", "Druid"]
        class_var.set(character.character_class)

        class_dropdown = tk.OptionMenu(form_frame, class_var, *class_options)
        class_dropdown.config(bg='#4a4a4a', fg='white',
                              font=('Arial', 11), width=25)
        class_dropdown.grid(row=1, column=1, sticky='ew',
                            pady=10, padx=(10, 0))

        # Level
        tk.Label(
            form_frame,
            text="Level:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=2, column=0, sticky='w', pady=10)

        level_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        level_entry.insert(0, str(character.level))
        level_entry.grid(row=2, column=1, sticky='ew', pady=10, padx=(10, 0))

        form_frame.columnconfigure(1, weight=1)

        # Buttons
        button_frame = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        button_frame.pack(pady=20)

        def do_save():
            name = name_entry.get().strip()

            if not name:
                messagebox.showerror(
                    "Error", "Character name cannot be empty!")
                return

            try:
                level = int(level_entry.get())
                if level < 1:
                    messagebox.showerror("Error", "Level must be at least 1!")
                    return
                if level > 100:
                    messagebox.showerror("Error", "Level cannot exceed 100!")
                    return
            except ValueError:
                messagebox.showerror(
                    "Error", "Please enter a valid level number!")
                return

            # Update character
            character.name = name
            character.level = level
            character.character_class = class_var.get()

            # *****NEW IMPLEMENTAION FOR A3*****
            self.app.notify("characters_changed")

            messagebox.showinfo("Success", "Character updated!")
            dialog.destroy()

        name_entry.bind('<Return>', lambda e: do_save())

        tk.Button(
            button_frame,
            text="Save Changes",
            command=do_save,
            bg='#4a8a4a',
            font=Fonts.SMALL_COURIER,
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            fg='white',
            font=Fonts.SMALL_COURIER,
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

    def delete_character(self, char_idx):
        """
        Delete a character
        """

        character = self.app.current_user.characters[char_idx]

        if messagebox.askyesno(
            "Confirm Delete",
            f"Are you sure you want to delete '{character.name}'?\n\n"
            f"This will also delete {len(character.curr_inventory.items)} item(s) in their inventory.\n\n"
            "This action can be undone using the Undo button!"
        ):

            # *****NEW IMPLEMENTAION FOR A3*****
            # handle character delete through command pattern
            command = DeleteCharacterCommand(self.app.current_user, char_idx)
            self.command_manager.execute(command)
            self.update_undo_redo_buttons()

            # *****NEW IMPLEMENTAION FOR A3*****
            self.app.notify("characters_changed")

            messagebox.showinfo("Success", "Character deleted!")

    def show_inventory_management(self, character, char_idx):
        """
        Show inventory management dialog
        """

        dialog = self.create_dialog(f"Inventory: {character.name}", 600, 500)

        # Title
        title_frame = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        title_frame.pack(fill='x', pady=20, padx=20)

        tk.Label(
            title_frame,
            text=f"🎒 {character.name}'s Inventory",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=('Courier', 16, 'bold')
        ).pack(side='left')

        tk.Button(
            title_frame,
            text="+ Add Item",
            command=lambda: self.show_add_item_dialog(character, dialog),
            bg='#4a8a4a',
            font=('Courier', 10, 'bold'),
            relief='flat',
            highlightthickness=0,
            padx=15,
            pady=5
        ).pack(side='right')

        # Inventory list container
        inventory_container = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        inventory_container.pack(
            fill='both', expand=True, padx=20, pady=(0, 20))

        def refresh_inventory():
            # Clear existing widgets
            for widget in inventory_container.winfo_children():
                widget.destroy()

            if not character.curr_inventory.items:
                tk.Label(
                    inventory_container,
                    text="No items in inventory",
                    bg=Colors.DARK_GRAY,
                    fg='#888888',
                    font=Fonts.SMALL_COURIER
                ).pack(pady=50)
            else:
                # Create scrollable frame
                canvas = tk.Canvas(inventory_container,
                                   bg=Colors.DARK_GRAY, highlightthickness=0)
                scrollbar = tk.Scrollbar(
                    inventory_container, orient="vertical", command=canvas.yview)
                scrollable_frame = tk.Frame(canvas, bg=Colors.DARK_GRAY)

                scrollable_frame.bind(
                    "<Configure>",
                    lambda e: canvas.configure(scrollregion=canvas.bbox("all"))
                )

                canvas_window = canvas.create_window(
                    (0, 0), window=scrollable_frame, anchor="nw")
                canvas.configure(yscrollcommand=scrollbar.set)

                def configure_scroll_region(event):
                    canvas.configure(scrollregion=canvas.bbox("all"))
                    canvas.itemconfig(canvas_window, width=event.width)

                canvas.bind("<Configure>", configure_scroll_region)

                canvas.pack(side="left", fill="both", expand=True)
                scrollbar.pack(side="right", fill="y")

                # Display each item
                for idx, item in enumerate(character.curr_inventory.items):
                    item_frame = tk.Frame(
                        scrollable_frame, bg='#3a3a3a', relief='raised', bd=1)
                    item_frame.pack(fill='x', pady=5)

                    content = tk.Frame(item_frame, bg='#3a3a3a')
                    content.pack(fill='x', padx=10, pady=8)

                    # Item name and rarity
                    info_frame = tk.Frame(content, bg='#3a3a3a')
                    info_frame.pack(fill='x')

                    tk.Label(
                        info_frame,
                        text=item.name,
                        bg='#3a3a3a',
                        fg='white',
                        font=('Courier', 12, 'bold')
                    ).pack(side='left')

                    tk.Label(
                        info_frame,
                        text=f"  •  {item.rarity}",
                        bg='#3a3a3a',
                        fg='#888888',
                        font=Fonts.SMALL_COURIER
                    ).pack(side='left')

                    # Damage
                    tk.Label(
                        info_frame,
                        text=f"  •  Damage: {item.damage}",
                        bg='#3a3a3a',
                        fg='#ffaa00',
                        font=Fonts.SMALL_COURIER
                    ).pack(side='left')

                    # Description
                    if item.description:
                        tk.Label(
                            content,
                            text=item.description,
                            bg='#3a3a3a',
                            fg='#cccccc',
                            font=('Courier', 9),
                            wraplength=500,
                            justify='left'
                        ).pack(anchor='w', pady=(5, 0))

                    # Delete button
                    tk.Button(
                        content,
                        text="Remove",
                        command=lambda i=item: remove_item(i),
                        bg='#8a4a4a',
                        font=('Courier', 9),
                        relief='flat',
                        highlightthickness=0,
                        padx=10,
                        pady=2
                    ).pack(anchor='w', pady=(5, 0))

        def remove_item(item):
            if messagebox.askyesno("Confirm", f"Remove '{item.name}' from inventory?"):
                character.curr_inventory.remove_inventory(item)

                refresh_inventory()

        refresh_inventory()

        # Store refresh function so add item dialog can use it
        dialog.refresh_inventory = refresh_inventory

        # Close button
        tk.Button(
            dialog,
            text="Close",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            width=15,
            relief='flat',
            highlightthickness=0
        ).pack(pady=(0, 20))

    def show_add_item_dialog(self, character, parent_dialog):
        """
        Show dialog to add an item to character inventory
        """
        dialog = self.create_dialog("Add Item", 500, 500)
        self.create_dialog_title(dialog, "Add Item to Inventory")
        form_frame = self.create_form_frame(dialog)

        # Item name
        tk.Label(
            form_frame,
            text="Item Name:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=0, column=0, sticky='w', pady=10)

        name_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        name_entry.grid(row=0, column=1, sticky='ew', pady=10, padx=(10, 0))
        name_entry.focus()

        # Rarity
        tk.Label(
            form_frame,
            text="Rarity:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=1, column=0, sticky='w', pady=10)

        rarity_var = tk.StringVar(dialog)
        rarity_options = ["Common", "Uncommon",
                          "Rare", "Epic", "Legendary", "Mythic"]
        rarity_var.set(rarity_options[0])

        rarity_dropdown = tk.OptionMenu(
            form_frame, rarity_var, *rarity_options)
        rarity_dropdown.config(bg='#4a4a4a', fg='white',
                               font=('Arial', 11), width=25)
        rarity_dropdown.grid(row=1, column=1, sticky='ew',
                             pady=10, padx=(10, 0))

        # Damage
        tk.Label(
            form_frame,
            text="Damage:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=2, column=0, sticky='w', pady=10)

        damage_entry = tk.Entry(form_frame, width=30, font=('Arial', 12))
        damage_entry.insert(0, "0")
        damage_entry.grid(row=2, column=1, sticky='ew', pady=10, padx=(10, 0))

        # Description
        tk.Label(
            form_frame,
            text="Description:",
            bg=Colors.DARK_GRAY,
            fg='white',
            font=Fonts.SMALL_COURIER
        ).grid(row=3, column=0, sticky='nw', pady=10)

        desc_text = tk.Text(form_frame, width=30, height=5, font=('Arial', 11))
        desc_text.grid(row=3, column=1, sticky='ew', pady=10, padx=(10, 0))

        form_frame.columnconfigure(1, weight=1)

        # Buttons
        button_frame = tk.Frame(dialog, bg=Colors.DARK_GRAY)
        button_frame.pack(pady=20)

        def do_add():
            name = name_entry.get().strip()
            description = desc_text.get("1.0", tk.END).strip()

            if not name:
                messagebox.showerror("Error", "Item name cannot be empty!")
                return

            try:
                damage = int(damage_entry.get())
                if damage < 0:
                    messagebox.showerror("Error", "Damage cannot be negative!")
                    return
            except ValueError:
                messagebox.showerror(
                    "Error", "Please enter a valid damage number!")
                return

            # Create item matching UML structure
            item = Item(
                name=name,
                rarity=rarity_var.get(),
                damage=damage,
                description=description
            )

            # Use proper method from Inventory class
            character.curr_inventory.add_inventory(item)

            messagebox.showinfo("Success", f"'{name}' added to inventory!")
            dialog.destroy()

            # Refresh parent inventory dialog
            if hasattr(parent_dialog, 'refresh_inventory'):
                parent_dialog.refresh_inventory()

        name_entry.bind('<Return>', lambda e: do_add())

        tk.Button(
            button_frame,
            text="Add Item",
            command=do_add,
            bg='#4a8a4a',
            font=Fonts.SMALL_COURIER,
            width=12,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

        tk.Button(
            button_frame,
            text="Cancel",
            command=dialog.destroy,
            bg='#4a4a4a',
            font=Fonts.SMALL_COURIER,
            width=12,
            relief='flat',
            highlightthickness=0
        ).pack(side='left', padx=5)

    # *****NEW IMPLEMENTAION FOR A3*****
    def update(self, subject, event: str, data: dict) -> None:
        if event == "characters_changed":
            self.after(0, self.refresh_characters_list)
        elif event == "inventory_changed":
            # only refresh if change impacts current user
            if data.get("user") is self.app.current_user:
                self.after(0, self.refresh_characters_list)

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

    def _perform_undo(self):
        """Actually perform the undo (called after event finishes)"""

        if self.command_manager.undo():

            # Schedule UI updates with delays to avoid crash
            self.after(10, self.refresh_characters_list)
            self.after(20, self.update_undo_redo_buttons)
            self.after(30, lambda: messagebox.showinfo(
                "Undo", "Action undone!"))
        else:
            messagebox.showwarning("Undo failed")

    def _perform_redo(self):
        """Actually perform the redo (called after event finishes)"""

        if self.command_manager.redo():
            
            # Schedule UI updates with delays to avoid crash
            self.after(10, self.refresh_characters_list)
            self.after(20, self.update_undo_redo_buttons)
            self.after(30, lambda: messagebox.showinfo(
                "Redo", "Action redone!"))
        else:
            messagebox.showwarning("Redo failed")

    # *****NEW IMPLEMENTAION FOR A3*****
    def update_undo_redo_buttons(self):
        """Update undo/redo button states and text"""
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
