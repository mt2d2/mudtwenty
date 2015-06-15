# Introduction #

"Includes Design Doc with team name and members listed. Describes how each portion of the spec has been implemented and identifies/justifies uses of each Design Pattern correctly. Describes and extra features, how to find them and any known problems."

I'm writing this wiki page with some html formatting because I think that it would be convenient if, when this doc is done, we just save it as a html file. This would make it easy to keep the same formatting between this draft and the final copy. It's a ugly right now on the wiki, but it can be made prettier later (by adding CSS to it in its own HTML doc, for example). Note, Everything in `blockquote` is a quote from the spec.

<h1>Design Doc</h1>

<h2>Introduction paragraph</h2>

  * We created a basic MUD server and client.
  * It has potential to be expanded in various ways.

<h2>Specification</h2>

<h3>Connections:</h3>

<blockquote><i>Multiple players must be able to connect to the MUD at the same time over the Internet.‭ ‬To do so,‭ ‬they will launch a MUD Client program which will allow them to connect to the MUD Server running at a different location.‭ ‬Read the Client/Server section for more details.</i></blockquote>

Connection over the Internet is possible because we are using sockets to communicate between client and server. Simultaneous connections are possible because when a client connects, a separate thread (a `ServerThread`) is spawned to handle the client. A connection to a player can happen in two different apps, the bundled Client or via telnet. The system automatically decides which system to use and calls each subsystem as appropriate.

In the GUI Client, serialized Java objects are passed over the network to provide some useful metadata. The `Externalizable` interface is implemented for these message objects to circumvent some issues with normal Java Serialization issues. We also considered using different Java serialization routines (e.g. `JBossSerialization`) or Google Protocol Buffers. Ultimately, the default Java method won out, due to the ban on external libraries.

<h3>Players:</h3>

<blockquote><i>Players must have a name.</i></blockquote>

Player names are accessed by a method `getName()` which is inherited from the abstract class `Creature`. The method is mandated by `Entity`, which is implemented by `Creature`.

<blockquote><i>Players must have a location‎ (‏A room they are in‭)‬.</i></blockquote>

The location of players (and MOBs) is kept track centrally, by the class `Universe`, in a `Map` from players to rooms. This was done because keeping track of player locations in both the `Player` class and the `Room` class could have led to inconsistencies. Keeping track of location in a Map ensures that Players can only have one location.

<blockquote><i>Players must have statistics that are changed via their actions in the MUD.‎ ‏In a traditional RPG,‭ ‬this would be things like hp‭ (‬health point/hit points‭)‬,‭ ‬defense,‭ ‬level,‭ ‬etc.‭ ‬For something less traditional,‭ ‬it could be number of questions answered correctly or amount of money won.</i></blockquote>

Both players and MOBs have the same sorts of statistics because they are both subtypes of the abstract class `Creature`, which keeps track of statistics. In our game, there are several statistics that are implemented as fields in the `Creature` class, including health, money, strength of attacks

<blockquote><i>Players must be able to carry items they find/buy/earn with them as they travel.</i></blockquote>

Players have a list of items inherited from the abstract class `Creature`.

<blockquote><i>Players must be able to move between rooms through the use of commands‎ (‏See Interaction section‭)‬.</i></blockquote>

Because the `Universe` class keeps track of the locations of players and MOBs in the game, it includes a method, `changeRoomOfCreature`, that changes the location of a mob or player. This method is called on a player by a `MoveResponse`, which is created when a player uses a move command.

<blockquote><i>Players must be able to‎ ‏interact with one another,‭ ‬and be able to see the locations and movements of other players in the same room as them.</i></blockquote>

Players can see the locations of other players in the same room because the players in the room are listed when the look command is used. Players can see the movements of others because when a move command is used, a notification is sent to players both in the room they're leaving and the room they're arriving in. Players can also interact with each other through chat commands.

<blockquote><i>Players must be able to‎ ‏interact with MOBs.</i></blockquote>

Players can interact with MOBs by talking to them and by attacking them. Players can also, of course, indirectly interact with MOBs by unlocking doors or leaving items in a room.

<h3>Rooms:</h3>

<blockquote><i>The MUD must have at least‎ ‏30‎ ‏rooms.</i></blockquote>

To make the MUD have 30 not-entirely-boring rooms when we were short on time, we used a class called `RandomRoomFactory` to generate rooms of different types.

<blockquote><i>A room must have a‎ ‏description and a‭ ‬list of possible exits,‭ ‬as well as a‭ ‬list of contents‭ (‬items,‭ ‬MOBs,‭ ‬and other players in the room‭)‬.‭ ‬This must be‭ ‬viewable by the player via‭ ‘‬look‭’ ‬and should be displayed by default upon their entrance into the room.</i></blockquote>

The `Room` class, which represents rooms in the game, contain a list of items that can be accessed by the method `getItems` and modified with the methods `addItem` and `removeItem`. `Room` also keeps track of the exits from the room; this was implemented as a Because `Universe` keeps track of which rooms each player and MOB are in, it also contains a method for getting a list of players or MOBs in any particular room.

All of these things that can be in a room are listed by a `LookResponse`, which is created when a player uses ‘‬look‭’. They are also listed when a player enters a room because this action also creates a `LookResponse`.

<h3>Items:</h3>

<blockquote><i>The MUD must have at least‎ ‏10‎ ‏different types of items.</i></blockquote>

Our MUD has the following concrete Item classes: Key, SmallPotion, LargePotion, Cannon, Spear Sword, Cloth, Hydes, SteelMesh, CheapTreat, FancyTreat, Book.

‎‏<blockquote><i>All items must have a use in the game.‭ ‬You could have potions to heal damage taken,‭ ‬keys to open doors,‭ ‬special items that affect the player’s stats,‭ ‬equipment the player can wear or items needed to finish a quest.‭</i></blockquote>

`Item`, the supertype of all items in the game, has an abstract `use` method which determines what happens when a player uses an item.

<h3>MOBs:</h3>

<blockquote><i>The MUD must have at least‎ ‏10‎ ‏different types of MOBs.</i></blockquote>

Our MUD has the following concrete MOB classes:
Merchant, ICritterCat, ICritterDog, ICritterPenguin, Kitten,
Bunny, Deer, RoomGiftMob, RoomThiefMob, Troll.

<blockquote><i>MOBs must be able to move between rooms.</i></blockquote>

The mechanism for moving MOBs between rooms is the same as the mechanism for moving Players; the method `changeRoomOfCreature` in `Universe` can be used.
However, this method is called not by a `MoveResponse`, which only responds to players' commands, but by a MOB's `BehaviorStrategy`.

<blockquote><i>MOBs must have‎ ‏complex behaviors—these can involve player interactions that take into account player statistics or items a player is carrying,‭ ‬or the contents of the room that the MOB is in,‭ ‬and MOBs should have actions that are triggered periodically,‭ ‬without direct interaction from players.‭ ‬For instance,‭ ‬movement,‭ ‬speech,‭ ‬interacting with other MOBs,‭ ‬etc.</i></blockquote>

In our server, there is a thread for each MOB; the class `MOB` implements `Runnable`. In the `run()` method for `MOB`, there is a loop wherein the MOB performs some action and then sleeps for an amount of time. This allows MOBs to perform actions that are triggered periodically. To allow MOBs to have complex behaviors, MOBs have a `BehaviorStrategy` object, which determines how they behave at a particular time, and a `takeTurn` method, which determines when a particular MOB switches strategy.

MOBs also have a `DialogStrategy` object, which determines how they respond to speech from players and other MOBs. When some creature says something to a MOB, a method in the MOB's `DialogStrategy` is called, which returns the MOB's response to the speech. `DialogStrategy` was made a separate interface so that complex dialog behavior could be added that could be common to several different types of MOBs.

<h3>Interaction:‭</h3>

<blockquote><i>When players are in the system,‎ ‏they should be able to send‭ ‬input and receive‭ ‬output without interference from other players.</i></blockquote>

Each player independently sends input and receives output from the server because each player is handled by a different thread.
‭
<blockquote><i>Players must be able to‎ ‏interact with the game with a set of commands that include‭ ‬at least the following: [ooc, who, say, tell, score, give, get, inventory, drop, use, quit, shutdown]</i></blockquote>

When a player types one of the above commands, a `ServerThread` receives it and uses the `ResponseFactory` to create a `ServerResponse` object. Each of the different types of commands is handled by a different `ServerResponse` object.

<h3>System:</h3>

<blockquote><i>MUD must be‭ ‬persistent.‭ ‬Changes made to players and the MUD must remain even after the MUD is shutdown and restarted.</i></blockquote>

Our MUD is persistent because the `Universe` and `Player` objects are periodically serialized and saved to disk by the server. The players are saved and loaded separately from the server in order to avoid loading all logged-out players into memory when the server is started. However, the `Universe`, which is saved periodically, does contain a map of all player names to rooms so that players can be put into the room they were last in when they log back in.

<blockquote><i>MUD must be able to be‭ ‬shutdown from within the game.‭ ‬This function should not be available to all players‭ (‬ie.‭ ‬Password protected,‭ ‬reserved to specific players,‭ ‬etc‭)‬.</i></blockquote>

<h3>Consistency:</h3>

<blockquote><i>The MUD should be logically consistent.‭ ‬For example,‭ ‬a player should only be able to give items to another player/MOB if they are in the same room.‭ ‬Also,‭ ‬walking north to the next room and then south should take the player back to the original room they were in‭ (‬Unless the area is designed to randomly connect rooms to create a maze like environment.‭ ‬If so,‭ ‬the area should be clearly marked with a sign before entering it or a warning in the description of the room before entering it‭)‬.</i></blockquote>

Players can only give items to others in the same room because a check is made for this when the player tries to give an item, in the class `GiveResponse`. Similar checks exist for commands such as get. Consistency in the connections between the rooms is ensured by the method `addRoom` of the class `Room`, which creates an exit going back to the first room from the room that is added. The flexibility to create a maze-link environment exists, though.

<h3>Client/Server:</h3>

<blockquote><i><p>The main portion of the MUD will be written as the MUD Server.‭ ‬All interactions with the client should be sent to the server and should trigger an appropriate response by the server that is sent back to the client.‭ ‬The server should run as a terminal based program on one computer and must accept connections from MUD Client programs that run on other computers over the internet.</p>
<p>The MUD Client will be a simple GUI which will allow the user to connect to the server.‭ ‬The MUD Client will connect to the server using a Socket/ServerSocket relationship.‭ ‬It must provide a GUI which consists of at least two frames‭ (‬or panels‭)‬,‭ ‬one which handles chat‭ (‬referred to as Chat‭) ‬and one which handles the users‭’ ‬interactions with the Server and the Server’s responses‭ (‬referred to as System‭)‬.‭ ‬The specifics of the implementation are up to you.</p></i></blockquote>

Our server and client fulfills these requirements.‭

<blockquote><i>Note:‭ ‬All commands listed above should work in the System frame‭ (‬or panel‭)‬,‭ ‬even if they are a chat command‭ (‬such as‭ “‬ooc‭”)‬.‭ ‬On the other hand,‭ ‬the Chat frame‭ (‬or panel‭) ‬should only respond to commands that relate to Chat functions,‭ ‬such as‭ ‬ooc,‭ ‬who,‭ ‬say,‭ ‬and tell.</i></blockquote>

<h3>Extra credit:</h3>

<blockquote><i>Dropped Connections (2 Points) – Announce to all when a player’s connection is dropped and save that player’s state.</i></blockquote>

If a player exits the game without properly exiting, a `ReaperTask` in `Server` reports this to the logged in players when it is removing a player's `ServerThread` from the list that the `Server` class has. Players' states are periodically saved automatically.

<blockquote><i>Room Behaviors (4 Points) - Rooms that have behaviors (for instance, weather, or keys that unlock exits).</i></blockquote>

Rooms can be lockable, and `Key` objects can be created that correspond to a particular room. When a player uses the correct key while they are in a locked room, the room becomes temporarily unlocked.

<blockquote><i>ANSI Color Support (4 Points) - ANSI color schemes for output.</i></blockquote>

Color of messages that are sent to clients is kept track of in `ClientMessage`; if the user is using our GUI client, then our client uses the color information inside the message. If the user is not using our GUI client, then the color of the message is encoded into the string sent to the client using ANSI escape codes.

<blockquote><i>Emoting/Posing (1 Point) - A command that allows a player to write a sentence from the third person perspective: for instance ‘emote looks around the room curiously.’</i></blockquote>

Emoting is implemented by including an appropriate `ServerResponse` class.

<blockquote><i>Room Exit Description (1 Point) - Room exits that have descriptions viewable with the ‘look’ command.</i></blockquote>

The `Exit` class has a method `getDescription` which is given to the user when they use the look command on an exit (e.g 'look north').

<blockquote><i>Social Commands (1 Point) - A set of “social” commands (such as, ‘giggle, laugh, wink, slap,’ etc, that would display something like, ‘player giggles at target’ or just ‘player; giggles’).</i></blockquote>

Social commands are implemented by including an appropriate `ServerResponse` class.

<blockquote><i>Abbreviated Commands (1 Point) - Players able to type abbreviated versions of command names (for instance, ‘l’ for look, ‘sc’ for score, ‘”’ for say, etc).</i></blockquote>

Abbreviated forms of commands were implemented inside of `ResponseFactory`, which, for example, returns the same `ServerResponse` if the user types 'look', 'l', or 'ls'. Likewise, the same action is performed for each of the commands 'north', 'n', 'move north', and 'cd north'.

<h2>Design Patterns</h2>

<h3>Prototype</h3>

Items and MOBs use the prototype pattern. They are cloneable, and in constructing the world, we can use this fact to make a mob, change something about it, and make several clones. This allows us to create many similar groups of items that do not necessarily match any particular hard-coded class.

<h3>Factory</h3>

`ResponseFactory` is a factory; it has a method, `getResponse` which constructs different types of `ServerResponse` depending on the input given. Although `ResponseFactory` is not abstract and does not have any subtypes, it could potentially be extended by a class which parsed input differently but still used the same method name for making `ServerResponse` objects.

<h3>Template</h3>

MOB uses the template pattern. The `run()` method of MOB is an invariant behavior, common to all MOBs, but it depends on variant behavior, `takeTurn()`, which is specific to particular types of MOBs. `run()` is not abstract but `takeTurn()` is.

<h3>Abstract Factory</h3>

`RoomFactory`, an inner class of `RandomRoomFactory`, is an abstract factory. It is an interface for creating a collection of products, which are parts of rooms. It is implemented by several concrete factories which make differently-themed room parts. This is useful for assembling complete rooms that have a particular theme.

<h3>Command</h3>

`ServerResponse` uses the command pattern; Each `ServerResponse` instance is an object that represents an action. This is useful because the

<h3>Strategy</h3>

`BehaviorStrategy`, a class used of MOBs use the strategy pattern. `BehaviorStrategy` objects encapsulate some strategy. The particular `BehaviorStrategy` that MOB is using can be changed dynamically depending on what is happening around the MOB. For example, a MOB might switch between `NullStrategy` and `MoveStrategy`. This could be expanded by adding any other behavior that a MOB might switch to.

<h3>MVC</h3>

Our MUD could be roughly considered to follow an MVC pattern. The `Universe` and its contents could be considered the model, our GUI client could be considered a view, and `ServerResponse` objects could be considered the controller.

<h2>Extra features</h2>

Our Server can accept connections from telnet or other MUD clients as well as our GUI client. This is accomplished by using an interface for sending and receiving messages, `Communicable`, that is implemented by two classes; one for our GUI client and one for other clients. The ability to send messages through Communicable objects which can be either of the two types is an example of polymorphism.

<h2>Known problems</h2>

There are no known problems.