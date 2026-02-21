import uuid
from models.user import User
from models.campaign import Campaign
from models.realm import Realm
from services.worldClock import WorldClock
from models.questEvent import QuestEvent
from commands.questEventCommands import ListEventsCommand, AddEventCommand, RemoveEventCommand

def pickInt(prompt, minVal=None, maxVal=None):
    while True:
        temp =  input(prompt).strip()
        try:
            x = int(temp)
            if minVal is not None and x < minVal:
                print(f"Please enter a number >= {minVal}")
                continue
            if maxVal is not None and x > maxVal:
                print(f"Please enter a number <= {maxVal}")
                continue
            return x
        except:
            print("Please enter a valid number.")

def pause():
    input("\nPress Enter to continue...")

def formatEvent(event, user):
    strat = user.settings.getTimeDisplayStrategy()
    realm = user.settings.currentRealm if user.settings.currentRealm else event.realm
    start = strat.format(event.startTime, realm)
    end = "None" if event.endTime is None else strat.format(event.endTime, realm)

    realmName = event.realm.name
    return f"- {event.title} | {start} -> {end} | Realm: {realmName}"

users = [
    User(uuid.uuid4(), "Gavin1"),
    User(uuid.uuid4(), "Gavin2"),
]

realms = [
    Realm("Earth", "Default realm", 0),
    Realm("MoonBase", "Example realm", 60),
]
def mainMenu():
    currUser = users[0]

    while True:
        print("\n=== GuildQuest ===")
        print(f"Current user: {currUser.username}")
        print("1) Switch user")
        print("2) Settings")
        print("3) Realms")
        print("4) Campaigns")
        print("0) Quit")

        choice = input("Choose: ").strip()

        if choice == "1":
            currUser = switchMenu(currUser)
        elif choice == "2":
            settingsMenu(currUser)
        elif choice == "3":
            realmsMenu()
        elif choice == "4":
            campaignsMenu(currUser)
        elif choice == "0":
            print("Bye!")
            return
        else:
            print("Invalid choice.")

def switchMenu(currUser):
    print("\n--- Switch User ---")
    for x, u in enumerate(users):
        print(f"{x+1}) {u.username}")
    idx = pickInt("Pick user #: ", 1, len(users)) - 1
    return users[idx]

def settingsMenu(user):
    while True:
        print("\n--- Settings ---")
        mode = user.settings.timeDisplayMode if hasattr(user, "settings") and hasattr(user.settings, "timeDisplayMode") else "BOTH"
        currRealm = user.settings.currentRealm.name if hasattr(user, "settings") and hasattr(user.settings, "currentRealm") and user.settings.currentRealm else "None"

        print(f"Time display mode: {mode}")
        print(f"Current realm: {currRealm}")
        print("1) Set time display mode (WORLD / LOCAL / BOTH)")
        print("2) Set current realm")
        print("0) Back")

        c = input("Choose: ").strip()
        if c == "1":
            newMode = input("Enter WORLD, LOCAL, or BOTH: ").strip().upper()
            if hasattr(user.settings, "setTimeDisplayMode"):
                user.settings.setTimeDisplayMode(newMode)
            else:
                user.settings.timeDisplayMode = newMode
            print("Updated.")
        elif c == "2":
            if not realms:
                print("No realms exist. Create one first.")
                continue
            for x, r in enumerate(realms):
                print(f"{x}) {r.name} (offset={getattr(r, 'offset', getattr(r, 'offsetMinutes', 0))})")
            idx = pickInt("Pick realm #: ", 1, len(realms)) - 1
            if hasattr(user.settings, "setCurrentRealm"):
                user.settings.setCurrentRealm(realms[idx])
            else:
                user.settings.currentRealm = realms[idx]
            print("Updated.")
        elif c == "0":
            return
        else:
            print("Invalid choice.")

def realmsMenu():
    while True:
        print("\n--- Realms ---")
        print("1) List realms")
        print("2) Create realm")
        print("0) Back")
        c = input("Choose: ").strip()

        if c == "1":
            if not realms:
                print("No realms yet.")
            else:
                for r in realms:
                    offset = getattr(r, "offset", getattr(r, "offsetMinutes", 0))
                    print(f"- {r.name} | id={r.realmId} | offset={offset} minutes")
            pause()

        elif c == "2":
            name = input("Realm name: ").strip()
            desc = input("Description (optional): ").strip()
            offset = pickInt("Offset minutes from WorldClock (ex: 60): ")
            realms.append(Realm(name, desc, offset))
            print("Realm created.")

        elif c == "0":
            return
        else:
            print("Invalid choice.")

def campaignsMenu(user):
    while True:
        print("\n--- Campaigns ---")
        print("1) List my campaigns")
        print("2) Create campaign")
        print("3) Select campaign")
        print("0) Back")
        c = input("Choose: ").strip()

        if c == "1":
            if not user.campaigns:
                print("No campaigns yet.")
            else:
                for i, camp in enumerate(user.campaigns):
                    print(f"{i+1}) {camp.name} | visibility={camp.visibility} | archived={camp.archived}")
            pause()

        elif c == "2":
            name = input("Campaign name: ").strip()
            camp = Campaign(name, user)
            user.campaigns.append(camp)
            print("Campaign created.")

        elif c == "3":
            if not user.campaigns:
                print("No campaigns to select.")
                continue
            for i, camp in enumerate(user.campaigns):
                print(f"{i+1}) {camp.name} | visibility={camp.visibility} | archived={camp.archived}")
            idx = pickInt("Pick campaign #: ", 1, len(user.campaigns)) - 1
            campaignMenu(user, user.campaigns[idx])

        elif c == "0":
            return
        else:
            print("Invalid choice.")

def campaignMenu(user, campaign):
    while True:
        print(f"\n=== Campaign: {campaign.name} ===")
        print(f"Visibility: {campaign.visibility} | Archived: {campaign.archived}")
        print("1) Rename campaign")
        print("2) Toggle visibility (PUBLIC/PRIVATE)")
        print("3) Archive campaign")
        print("4) Quest events")
        print("0) Back")
        c = input("Choose: ").strip()

        if c == "1":
            newName = input("New name: ").strip()
            campaign.rename(newName)
            print("Renamed.")

        elif c == "2":
            campaign.visibility = "PUBLIC" if campaign.visibility == "PRIVATE" else "PRIVATE"
            print("Toggled visibility.")

        elif c == "3":
            campaign.archive()
            print("Archived.")

        elif c == "4":
            questEventsMenu(user, campaign)

        elif c == "0":
            return
        else:
            print("Invalid choice.")


#helpers for questeventmenu
def pickEvent(campaign):
    if not campaign.events:
        print("No events to edit.")
        return None

    for i, x in enumerate(campaign.events):
        print(f"{i+1}) {x.title} | id={x.eventId}")
    idx = pickInt("Pick event #: ", 1, len(campaign.events)) - 1
    return campaign.events[idx]

def editEventTitle(e):
    newTitle = input("New title: ").strip()
    if hasattr(e, "updateTitle"):
        e.updateTitle(newTitle)
    else:
        e.title = newTitle
    print("Updated.")


def editEventTime(e):
    d = pickInt("New start day: ", 0)
    h = pickInt("New start hour (0-23): ", 0, 23)
    m = pickInt("New start minute (0-59): ", 0, 59)
    start = WorldClock.toMins(d, h, m)

    hasEnd = input("Has end time? (y or yes)/n or no: ").strip().lower()
    end = None
    if hasEnd == ["y", "yes"]:
        ed = pickInt("New end day: ", 0)
        eh = pickInt("New end hour (0-23): ", 0, 23)
        em = pickInt("New end minute (0-59): ", 0, 59)
        end = WorldClock.toMins(ed, eh, em)

    try:
        e.updateTime(start, end)
        print("Updated.")
    except Exception as ex:
        print("Couldn't update time:", ex)


def editEventRealm(e, realms):
    for i, x in enumerate(realms):
        print(f"{i+1}) {x.name}")
    ridx = pickInt("Realm #: ", 1, len(realms)) - 1
    newRealm = realms[ridx]

    if hasattr(e, "updateRealm"):
        e.updateRealm(newRealm)
    else:
        e.realm = newRealm
    print("Updated.")

def questEventsMenu(user, campaign):
    while True:
        print("\n--- Quest Events ---")
        print("1) List events")
        print("2) Add event")
        print("3) Edit event time/title/realm")
        print("4) Remove event")
        print("5) View events for a DAY")
        print("6) View events for a WEEK")
        print("0) Back")
        c = input("Choose: ").strip()

        commands = {
            "1": ListEventsCommand(user, campaign, formatEvent, pause),
            "2": AddEventCommand(campaign, realms, pickInt),
            "4": RemoveEventCommand(campaign, pickInt),
        }

        if c in commands:
            commands[c].execute()
            continue

        if c == "3":
            e = pickEvent(campaign)
            if e is None:
                continue

            print("Edit what?")
            print("1) Title")
            print("2) Time")
            print("3) Realm")
            sub = input("Choose: ").strip()

            if sub == "1":
                editEventTitle(e)
            elif sub == "2":
                editEventTime(e)
            elif sub == "3":
                editEventRealm(e, realms)
            else:
                print("Invalid choice.")       

if __name__ == "__main__":
    mainMenu()
