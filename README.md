# vindinium-client

![Build status](https://travis-ci.org/Protoalliance/vindinium-client.svg?branch=master)

This is a fork of Brian Stempin's Java starter for Vindinium.  We have kept a majority of Mr. Stempin's code intact and have essentially walled off our own section of the project while saving what we needed from his implementation.  There are some changes in our logging structure as well as the ability to run multiple runs of the project and specify the map for training runs.  The rest of our work is located in the proto package.

## Prerequisites

You'll need maven if you want to run this project in its current configuration.  You can get it [here](http://maven.apache.org/download.cgi).  If you're running in OSX the easiest way is going to be using homebrew:

`brew install maven`

In ubuntu it's as simple as:

`sudo apt-get install maven`

Alternatively using [intellij](https://www.jetbrains.com/idea/) is another very simple way to work with this software since it includes some maven functionality.

### Building using Maven:

To build:

    mvn compile

To create an uber JAR:

    mvn package
    
## Usage

Since there are a large number of bots available at this point in our system we won't generate the exec statements for all of the possible bots, but essentially all of these commands are of the form below.  If running on the command line you'll want to prepend mvn.

Command for running from Maven for bloodthirstbot on predefined map 1:

    exec:java -Dexec.mainClass=com.protoalliance.vindiniumclient.Main "-Dexec.args=YOURKEY TRAINING m1 proto com.protoalliance.vindiniumclient.bot.proto.bloodthirstbot.BloodthirstBot"

### Arguments

A valid maven command will be of this form:

`mvn exec:java -Dexec.mainClass=com.protoalliance.vindiniumclient.Main "-Dexec.args=<YOURKEY> <GAMEURL> <MAP> <BOT_TYPE> <QUALIFIED_BOT_NAME>"`

There are 4 required arguments:

* Your key
* The game URL
* The map (optional)
* The bot type
* The fully qualified class name of the bot that will play

The arguments are space-delimited.

##### Key
The key is specified by the Vindinium website when a user name is registered.

##### Game Url
Instead of specifying a game URL, a user can say `TRAINING` or `COMPETITION`, which will connect to Vindinium's training and competition arena, respectively.  The only time a user will not use one of these two arguments is when they are connecting to a different server, such as a local development server.

##### Map (optional)
If specified, the game will be run on a predefined map. Predefined maps include `m1`, `m2`, `m3`, `m4`, `m5`, and `m6` and you can see what they look like [here](https://github.com/ornicar/vindinium/blob/master/app/Maps.scala#L15).  Additionally `m*` is a potential value for this parameter.  It runs the game on all six predefined maps when utilized with the TRAINING mode.

##### Bot Type
Bot type should always be proto for running ProtoAllaince bots.

##### Fully Qualified Bot Class Name
The Main class will reflectively instantiate a `Bot` to play the game.  In order to locate the bot, the fully qualified name is needed, in the general form of `com.packagename.packagenamingpackage.SomeBot`.


### Authors
This fork was created by students at North Carolina State University as part of a Game AI project. The original Vindinium client was created by Brian Stempin. You can visit [his personal website](http://brianstempin.com) or his [GitHub](http://github.com/bstempi).
