"""
*****NEW IMPLEMENTAION FOR A3*****
"""

from __future__ import annotations
from abc import ABC, abstractmethod
from typing import Any, Dict, List, Optional


class Observer(ABC):
    @abstractmethod
    def update(self, subject: "Subject", event: str, data: Optional[Dict[str, Any]] = None) -> None:
        """Called by Subject when a relevant event occurs."""
        pass


class Subject:
    def __init__(self) -> None:
        self._observers: List[Observer] = []

    def attach(self, observer: Observer) -> None:
        if observer not in self._observers:
            self._observers.append(observer)

    def detach(self, observer: Observer) -> None:
        if observer in self._observers:
            self._observers.remove(observer)

    def notify(self, event: str, data: Optional[Dict[str, Any]] = None) -> None:
        # Iterate over a copy in case observers detach during update
        for obs in list(self._observers):
            obs.update(self, event, data or {})
