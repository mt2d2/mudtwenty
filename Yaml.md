# Introduction #
Yaml is a concise,  human-readable serialization of an object. Using [JYaml](http://jyaml.sourceforge.net/download.html), we can easily serialize back and forth between Java objects and these Yaml files. I think this would be perfect for representing things in the Universe (Rooms especially). Any ideas? Thoughts?


---


Advantages:
  * We might be able to modify the world and the items therein by editing text files.

Disadvantages:
  * **We might need to get section leader permission**. During the presentation of sample projects, somebody asked about using a 3rd party library and Alex Jerabek said it wasn't allowed. (However, using an alternative serialization would not be less work for us, so Jan would likely allow us.)
  * It would likely be simpler and more convenient to just serialize objects through the builtin java serialization.
  * Depending on how complex rooms, items, etc. get, the resulting YAML might get pretty messy.



---


The problem I see with default java serialization is that it requires you to be able to create those files with Java itself. We talked about that being a bad idea during the meeting today. By keeping Room/Item descriptions in Yaml, we -- and importantly _a user_ -- could edit them by hand. It would be an instant, albeit crude MUD editor.


---


I agree that that is an advantage to using Yaml. I am personally partial to the idea of keeping all our data stored in plain text so that it's relatively easy to add bits to the world, edit descriptions, etc. I think that we should make sure that it works fairly easily and ask Jan about whether it's okay.