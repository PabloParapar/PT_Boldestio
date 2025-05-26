from dataclasses import dataclass, field
from .day import Day
from typing import List

@dataclass
class Tour:
    externalID: str
    title_es: str
    htmlDescription_es: str
    htmlContent_es: str
    program: List[Day] = field(default_factory=list)

    def add_day(self, day: Day):
        # Añade una instancia de Day a la lista program
        if not isinstance(day, Day):
            raise TypeError("Expected an instance of Day")
        self.program.append(day)