class Visibliity:
    def __init__(self, is_visible: bool = True):
        self.is_visible = is_visible
        
class Campaign_Visibility(Visibliity):
    def __init__(self, is_visible = True):
        super().__init__(is_visible)
        
    def toggle_visibility(self) -> None:
        self.is_visible = not self.is_visible
        
class Quest_Visibility(Visibliity):
    def __init__(self, is_visible = True):
        super().__init__(is_visible)
    
    def check_c_visibility(self, CV: Campaign_Visibility) -> None:
        # guessing this is will check on quest sharing perms depending on the cv
        pass