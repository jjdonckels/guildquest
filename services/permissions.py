import uuid

class SharePermission:
    def __init__(self, sharedWith, permissionLevel):
        self.permissionId = uuid.uuid4()
        self.shaxwredWith = sharedWith        #
        self.permissionLevel = permissionLevel  
        self.canEdit = permissionLevel == "EDIT"

    def __str__(self):
        return f"Permission({self.sharedWith.username}, {self.permissionLevel})"
