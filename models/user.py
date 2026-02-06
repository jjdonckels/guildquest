from models.campaign import Campaign
from  models.settings import Settings

class User:
    def __init__(self, userId, username):
        self.userId = userId
        self.username = username
        self.campaigns = []   
        self.characters = []
        self.settings = Settings()

    def create_campaign(self, name):
        campaign = Campaign(name, self)
        self.campaigns.append(campaign)
        return campaign

    def delete_campaign(self, campaign_id):
        self.campaigns = [
            c for c in self.campaigns if c.campaign_id != campaign_id
        ]

    def share_campaign(self, campaign, other_user, permission):
        campaign.add_permission(other_user, permission)

    def __str__(self):
        return f"User({self.username})"