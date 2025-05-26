from dataclasses import dataclass, field
from typing import List
from .place import Place

@dataclass
class Day:
    day: str
    title_es: str
    htmlDescription_es: str
    htmlContent_es: str
    resourceIDs: List[Place] = field(default_factory=list)

    def add_place(self, place: Place):
        # Añade una instancia de Place a la lista resourceIDs
        if not isinstance(place, Place):
            raise TypeError("Expected an instance of Place")
        self.resourceIDs.append(place)