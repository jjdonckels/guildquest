from models.campaign import Campaign
from  models.settings import Settings

class User:
    def __init__(self, userId, username):
        self.userId = userId
        self.username = username
        self.campaigns = []   
        self.characters = []
        self.settings = Settings()

    def createCampaign(self, name):
        campaign = Campaign(name, self)
        self.campaigns.append(campaign)
        return campaign

    def deleteCampaign(self, campaignId):
        self.campaigns = [x for x in self.campaigns if x.campaignId != campaignId]

    def shareCampaign(self, campaign, otherUser, permission):
        campaign.addPermission(otherUser, permission)

    def __str__(self):
        return f"User({self.username})"