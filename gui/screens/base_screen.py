import tkinter as tk
from core.Observer import Observer

class BaseScreen(tk.Frame, Observer):
    """Base class for all screens"""

    def __init__(self, parent, app):
        """
        Args:
            parent: Parent widget (container)
            app: Reference to main gq_GUI instance
        """
        super().__init__(parent)
        self.app = app  # Access to app.world_clock, app.users, etc.

        # *****NEW IMPLEMENTAION FOR A3*****
        # Attach on creation
        self.app.attach(self)

        # Auto-detach when the widget is destroyed
        self.bind("<Destroy>", self._on_destroy)

        self.create_widgets()
    
    # *****NEW IMPLEMENTAION FOR A3*****
    def _on_destroy(self, event):
        # Tkinter fires Destroy events for many child widgets too; guard it:
        if event.widget is self:
            try:
                self.app.detach(self)
            except Exception:
                pass

    # *****NEW IMPLEMENTAION FOR A3*****
    def update(self, subject, event: str, data: dict) -> None:
        """Default: do nothing. Each screen decides what to handle."""
        return

    def create_widgets(self):
        """Override this in child classes"""
        pass

    def navigate_to(self, screen_name):
        """Helper to navigate to another screen"""
        self.app.show_screen(screen_name)

    # *****NEW IMPLEMENTAION FOR A3*****
    def create_dialog(self, title: str, width: int = 500, height: int = 400) -> tk.Toplevel:
        """
        Create a styled dialog window with consistent appearance.
        
        REFACTORING: Extract Method
        This method eliminates duplicated dialog creation code by centralizing
        the common setup logic. All dialogs should use this method.
        
        Args:
            title: Dialog window title
            width: Dialog width in pixels (default: 500)
            height: Dialog height in pixels (default: 400)
            
        Returns:
            tk.Toplevel: Configured dialog window (modal, styled)
            
        Example:
            dialog = self.create_dialog("Create Campaign", 500, 400)
        """
        from gui.themes import Colors
        
        dialog = tk.Toplevel(self.app)
        dialog.title(title)
        dialog.geometry(f"{width}x{height}")
        dialog.configure(bg=Colors.DARK_GRAY)
        dialog.grab_set()  # Make modal (blocks interaction with main window)
        return dialog
    
    # *****NEW IMPLEMENTAION FOR A3*****
    def create_dialog_title(self, parent: tk.Widget, text: str) -> tk.Label:
        """
        Create a styled title label for dialogs.
        
        REFACTORING: Extract Method
        This method ensures all dialog titles have consistent styling.
        
        Args:
            parent: Parent widget (usually the dialog window)
            text: Title text to display
            
        Returns:
            tk.Label: Styled title label (already packed)
            
        Example:
            self.create_dialog_title(dialog, "Create New Campaign")
        """
        from gui.themes import Colors
        
        label = tk.Label(
            parent,
            text=text,
            bg=Colors.DARK_GRAY,
            fg='white',
            font=('Courier', 18, 'bold')
        )
        label.pack(pady=20)
        return label
    
    # *****NEW IMPLEMENTAION FOR A3*****
    def create_form_frame(self, parent: tk.Widget) -> tk.Frame:
        """
        Create a styled form frame for dialog inputs.
        
        REFACTORING: Extract Method
        This method creates the standard frame used for form inputs in dialogs.
        
        Args:
            parent: Parent widget (usually the dialog window)
            
        Returns:
            tk.Frame: Styled form frame (already packed, configured for grid layout)
            
        Example:
            form_frame = self.create_form_frame(dialog)
            # Then use grid layout inside form_frame
        """
        from gui.themes import Colors
        
        frame = tk.Frame(parent, bg=Colors.DARK_GRAY)
        frame.pack(pady=10, padx=40, fill='both', expand=True)
        return frame
    
    # *****NEW IMPLEMENTAION FOR A3*****
    def create_button_frame(self, parent: tk.Widget) -> tk.Frame:
        """
        Create a styled button frame for dialog actions.
        
        REFACTORING: Extract Method
        This method creates the standard frame used for action buttons in dialogs.
        
        Args:
            parent: Parent widget (usually the dialog window)
            
        Returns:
            tk.Frame: Styled button frame (already packed)
            
        Example:
            button_frame = self.create_button_frame(dialog)
            # Then pack buttons inside button_frame
        """
        from gui.themes import Colors
        
        frame = tk.Frame(parent, bg=Colors.DARK_GRAY)
        frame.pack(pady=20)
        return frame