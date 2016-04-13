GuardianBeamAPI
===========
![GuardianBeamAPI Image Credit ArcLumX](http://i.imgur.com/oWfQVvg.png)

An easy solution for manipulating Guardian's beams in Spigot 1.9!

NOTE! 1.9 support is not included in this push. See the 1.8 branch. 1.9 support is coming soon!

What is GuardianBeamAPI?
--------
GuardianBeamAPI is an open source library and plugin licensed under the permissive MIT license that allows you to seamlessly manipulate the beams emitted by Guardians when they target another entity. The project was founded out of my realization that there were no clean libraries for manipulating these beams.
It is recommended that you use GuardianBeamAPI as a dependency. I hope to find a Maven repository to host this API on.

This project is currently under development, and it is not unlikely that the API will change. However, I will try not to change the API, or at least make it backwards compatible.

How do I use it?
--------
First, include GuardianBeamAPI as a dependency in your project.
<Maven Dependency and Repository coming soon>

Next, add to your plugin.yml:
```
depend: [GuardianBeamAPI]
```

Using the library is simple:
```java
Beam beam = new Beam(locationOne, locationTwo);
beam.start();
//Changing the target of the beam is easy:
beam.setStartingPosition(newLocation);
```
And all of the code is documented. Check out the [net.jaxonbrown.guardianBeam.beam package](https://github.com/MeRPG/GuardianBeamAPI/tree/master/src/main/java/net/jaxonbrown/guardianBeam/beam) for more!

If you use my library for a public plugin, while the license does not require it, I respectfully ask you to credit me by linking either here, or the [plugin page](https://www.spigotmc.org/resources/guardianbeamapi.18329/).

Contributing
--------
I encourage anyone with an idea to fork this project and submit a pull request! I want this to be a community driven project, so I'd be glad to accept any PRs that meet reasonable quality standards.

Contributors:
* Jaxon Brown, Author
