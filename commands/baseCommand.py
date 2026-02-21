#base comand class
class Command:
    def execute(self):
        raise NotImplementedError("Subclasses must implement execute()")