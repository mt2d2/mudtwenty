# Introduction #

A list of things to concentrate on. Delete them from this list when they're done, and add one if it's not here and it's important. Should include quotes from spec.


---


**Before we generate the UML, we need to have a few more MOB and item classes: most or all of these can be simple subclasses or variations of the currently existing items/mobs.**

**In order to have a universe consisting of rooms that are slightly interesting, I want to create a class, `RandomRoomFactory` or something like that -- this can incorporate both builder and abstract factory patterns (`patterns += 2`) and return random rooms of slightly different types, depending on a string or enum that is passed to a method called something like `getRandomRoom( RoomType type )`. I will have enough time to do this later tonight, but I have to finish homework for another class and I got sick at a really inconvenient time :-( sorry guys.** --Quinten


---


# Common Spec #

  * **UML**: "Complete UML class diagram(s), sequence diagram of inner loop. ...  Matches final submission.  ... The UML class diagrams and sequence diagram should be submitted in paper form. They should clearly show the names of each member of the team, as well as the name of the project being designed."

  * **Design Doc**: "Includes Design Doc with team name and members listed.  Describes how each portion of the spec has been implemented and identifies/justifies uses of each Design Pattern correctly.  Describes and extra features, how to find them and any known problems."

  * **Design Patterns**: "Project fully uses at least 6 design patterns."

# Project Spec #

  * **Expand the universe**: "The MUD must have at least 30 rooms."

  * **Expand items, add uses**: "The MUD must have at least 10 different types of items. ... All items must have a use in the game. You could have potions to heal damage taken, keys to open doors, special items that affect the player’s stats, equipment the player can wear or items needed to finish a quest."

  * **Items so far (10/10)**: Key, Large Potion, Small Potion, FancyTreat, CheapTreat, Sword, Spear, Cloth, SteelMesh, Cannon

  * **Expand MOBs, add behavior**: "The MUD must have at least 10 different types of MOBs ... Some MOBs may offer quests, services or games to the players ... MOBs must be able to move between rooms ... MOBs must have complex behaviors ... that take into account player statistics or items a player is carrying, or the contents of the room that the MOB is in ... MOBs should have actions that are triggered ... without direct interaction from players."

  * **Mobs so far (3/10)**: Kitten, Thief, and Gift

  * **Add combat system**.

  * **Make look give a description of an entity for any "look ENTITY"**.

# Extra Credit #

  * ~~Dropped Connections (2 Points)~~ – Announce to all when a player’s connection is dropped and save that player’s state.

  * ~~Some~~ Room Behaviors (4 Points) - Rooms that have behaviors (for instance, weather, or keys that unlock exits).

  * Dynamic MUD Editor (4-10 Points) - An in-game menu system for creating new items, mobs and rooms. This could also use a GUI to simplify the process. This option should not be available to all players.

  * Shareable MUDs (2 Points) – Provide a way for MUDs created with the editor above to be shared.

  * Complex Skills/Classes (4 Points) - Complex skills and classes that allow players to have more sophisticated and a wider variety of interactions with their environment.

  * ~~ANSI Color Support~~

  * Player Grouping (4 Points) - if a player joins a group, he or she follows the movement of the leader, and executes certain commands (for instance, attack) if the leader executes them.

  * ~~Emoting/Posing (1 Point)~~ - A command that allows a player to write a sentence from the third person perspective: for instance ‘emote looks around the room curiously.’

  * Player Preferences (4+ Points depending on complexity) - Player options such as removing oneself from the ooc channel, turning color—if implemented—on and off, etc.

  * ~~Room Exit Description~~

  * ~~Social Commands~~ - "A set of “social” commands (such as, ‘giggle, laugh, wink, slap,’ etc.)

  * ~~Abbreviated Commands~~

  * Mini-Games (2+ Points depending on complexity) – Mini games are games within a game. This could take the form of a card game/dice game or something more complicated. This could be played between players or between players and MOBs.

  * Server-side commands (1 Point per command, up to 4 Points) – Allow an “administrator” to type commands directly into MUD Server which cause responses in the game for all players. Examples include shutdown, restart, kick, ban, banbyIP, deleteprofile, and many more.