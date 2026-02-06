import uuid
from models.user import User
from models.campaign import Campaign

u = User(uuid.uuid4(), "Gavin")
c = Campaign("Guild Run", u)

print(c)

c.setVisibility("PUBLIC")
print("Visibility:", c.visibility)

c.archive()
print("Archived:", c.archived)
