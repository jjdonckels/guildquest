import uuid

class SharePermission:
    def __init__(self, sharedWith, permissionLvl):
        self.permissionId = uuid.uuid4()
        self.sharedWith = sharedWith        
        self.permissionLvl = permissionLvl  
        self.canEdit = permissionLvl == "EDIT"

    def __str__(self):
        return f"Permission({self.sharedWith.username}, {self.permissionLvl})"
