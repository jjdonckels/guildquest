from services.worldClock import WorldClock
from models.questEvent import QuestEvent

class ListEventsCommand:
    def __init__(self, user, campaign, formatEvent, pause):
        self.user = user
        self.campaign = campaign
        self.formatEvent = formatEvent 
        self.pause = pause             

    def execute(self):
        if not self.campaign.events:
            print("No events yet.")
        else:
            for x in self.campaign.events:
                print(self.formatEvent(x, self.user))
        self.pause()

class AddEventCommand:

    def __init__(self, campaign, realms, pickInt):
        self.campaign = campaign
        self.realms = realms
        self.pickInt = pickInt

    def execute(self):
        if not self.realms:
            print("No realms exist. Create one first.")
            return

        title = input("Event title: ").strip()

        #start time
        day = self.pickInt("Start day: ", 0)
        hour = self.pickInt("Start hour (0-23): ", 0, 23)
        minute = self.pickInt("Start minute (0-59): ", 0, 59)
        start = WorldClock.toMins(day, hour, minute)

        #end time
        hasEnd = input("Has end time? (y/n): ").strip().lower()
        end = None
        if hasEnd == "y":
            eday = self.pickInt("End day: ", 0)
            ehour = self.pickInt("End hour (0-23): ", 0, 23)
            emin = self.pickInt("End minute (0-59): ", 0, 59)
            end = WorldClock.toMins(eday, ehour, emin)

        #choose realm
        print("Pick realm:")
        for i, r in enumerate(self.realms):
            print(f"{i+1}) {r.name} (offset={r.offset}m)")
        ridx = self.pickInt("Realm #: ", 1, len(self.realms)) - 1
        realm = self.realms[ridx]

        #create event
        try:
            event = QuestEvent(title, start, realm, end)
            self.campaign.addQuestEvent(event)
            print("Event added.")
        except Exception as ex:
            print("Could not create event:", ex)

class RemoveEventCommand:

    def __init__(self, campaign, pickInt):
        self.campaign = campaign
        self.pickInt = pickInt

    def execute(self):
        if not self.campaign.events:
            print("No events to remove.")
            return

        for i, x in enumerate(self.campaign.events):
            print(f"{i+1}) {x.title} | id={x.eventId}")

        idx = self.pickInt("Pick event #: ", 1, len(self.campaign.events)) - 1
        eventId = self.campaign.events[idx].eventId

        # remove event
        self.campaign.removeQuestEvent(eventId)
        print("Removed.")