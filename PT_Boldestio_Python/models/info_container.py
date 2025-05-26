from dataclasses import dataclass, field
from typing import List
from .tour import Tour

@dataclass
class InfoContainer:
    syncData: List[Tour] = field(default_factory=list)
    schema_sera_sustituido: str = ''

    def add_tour(self, tour: Tour):
        # Añade un Tour a la lista syncData
        if not isinstance(tour, Tour):
            raise TypeError("Expected an instance of Tour")
        self.syncData.append(tour)
