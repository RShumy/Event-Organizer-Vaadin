﻿## Event Organizer WIP (Spring Boot - backend, Vaadin - frontend)
### Back End (Controlers included for Vaadin WebClient(not created yet)) 
#### Simple Event Organizer WebApp. 
Models:
- 3 Base Entity Clases:
	 - User, Event, Consumable
- 2 Relational Entity Classes:
	 - Participant (n:n) (User, Event) - CompositeKeys/ParticipantKey Class
	 - ParticipantConsumable (n:n) (Participant, Consumable) - CompositeKeys/ParticipantConsumableKey Class
- 1 Relational Entity - declared only by Hibarante @ManyToMany:
	-  EventConsumable (n:n) (Event - owner entity)
#### JPA Repositories
#### Services (UserService, EventService, ConsumableService ,ParticipantService , PaticipantConsumableService)
#### Spring WebControllers (UserController, EventController, ParticipantController)
#### H2 In-Memory Data Base with Generated, Inserted data.

### Vaadin FrontEnd
- For now Views are calling Service Clasess Directly, Vaadin is auto-routing

### TO DO:
	- Create EventConsumables list view, selection functionalities and adding to participants
	- Create Participant list view and it's respective Consumables, add/delete consumable functionalities
	- Implement Vaadin WebClient
	- Create Log-in Page 
	- Implement Spring Boot Security 
	- Implement Method access restricitons
	- Functionalities with invite/join and Event privacy statuses (Public(free, ticket based),Private(free invite only, ticket based))
	



