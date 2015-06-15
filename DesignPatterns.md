# Introduction #

We have to use 6 patterns, and any more patterns are extra credit. Therefore, we should use as many patterns as humanly possible! Note: We might make separate pages for discussion of each pattern, but it might also be good to just add notes to this one page.

# Creational Patterns #

## [Prototype](http://en.wikipedia.org/wiki/Prototype_Pattern) ##

**Items and MOBs use the prototype pattern**: they are cloneable, and in constructing the world, we can use this fact to make a mob, change something about it, and make several clones. This would prevent us having to create many similar groups of MOBs that do not necessarily match any particular hard-coded class.

## [Factory](http://en.wikipedia.org/wiki/Factory_Pattern) ##

**`ResponseFactory` uses the factory pattern**: it contains a method which is designed to create different `ServerResponse`s depending on the input given (which is a string).

## [Singleton](http://en.wikipedia.org/wiki/Singleton_Pattern) ##

The Server could be a singleton -- but it might not actually make much sense to do this?

# Structural Patterns #

## [Composite](http://en.wikipedia.org/wiki/Composite_Pattern) ##

**Bags will use the composite pattern**: Bags will be items that can contain items, including bags.

In order to properly use it, we would have to have an type (like item) that has leaf subtypes (like ordinary items) and composite subtypes (like bags, containers, etc.)

## [Template](http://en.wikipedia.org/wiki/Template_pattern) ##

**MOB uses the template pattern**: run() method is an invariant behavior, common to all MOBs, but it depends on variant behavior, takeTurn(), which is specific to particular types of MOBs. run() is not abstract but takeTurn() is.

Creature could be an abstract class that relies on functionality implemented in MOB and Player?

## [Flyweight](http://en.wikipedia.org/wiki/Flyweight_Pattern) ##

`BehaviorStrategy`s or `ServerReponse`s could by flyweight? (I tried this with `BehaviorStrategy` and it wasn't pretty. Advise against -- Quinten)

# Behavioral Patterns #

## [Command](http://en.wikipedia.org/wiki/Command_Pattern) ##

`ServerResponse`s use the command pattern: each `ServerResponse` instance is an object that represents an action -- a response of the server to a client's input.

We might also include a more general Interaction class that represents actions/events that do not necessarily return a message to a player.

## [Strategy](http://en.wikipedia.org/wiki/Strategy_Pattern) ##

**`BehaviorStrategy`s of MOBs use the strategy pattern**: strategies are objects that encapsulate some strategy. The particular `BehaviorStrategy` that MOB is using can be changed dynamically depending on what is happening around the MOB. For example, there might be classes `AggressiveStrategy` and `RetreatStrategy`, and a MOB will switch from one to the other if it gets very hurt.

## [MVC](http://en.wikipedia.org/wiki/MVC_Pattern) ##

Perhaps the client could be considered the view, something else (parser stuff, possibly server?) could be considered controller, and universe could be considered model?

## [Observer](http://en.wikipedia.org/wiki/Observer_Pattern) ##

Any way that this could be used? How might it be used in the server or client?

# Misc/Uncategorized #

## [Null object](http://en.wikipedia.org/wiki/Null_object_pattern) ##
**`NullDialog` and `NullBehavior` use the null object pattern.**