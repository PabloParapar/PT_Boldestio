from dataclasses import dataclass

@dataclass
class Place:
    externalID: str
    submap: str
    #day: str
    title_es: str
    htmlDescription_es: str
    htmlContent_es: str
    locationLat: str
    locationLon: str