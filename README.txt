This is an alpha-stage (yet fully functionall) mod to add Miners (much like Buildcraft Mining well and Quarry,
or IC2 Miner). There are two main differences though:
   * This mod's miners run on vanillla fuel (well, they will, as soon as I code it)
   * These are intelligent miners.(more on that bellow)

Currently there are two miners:
   * the eXpandable Mine Miner, that will go through the blocks building a mine using the pattern described by NZPhonix (http://nzphoenixma.blogspot.com/2011/04/phoenix-x-mine.html). It detects and mine ores it finds along the way.
   * The Chunk Miner works like the Buildcraft quarry, in the sense that it will strip an area of all blocks from the point it is placed to bedrock. It just happens that the area is 16x16

As they are, these are quite OP, crossing the border of "cheating", thus the alpha-stage designation.

Why i'm doing this? Because i find programming and meta-gaming quite fun, and because I want miners without all the other features that  come with Buildcraft or IC2


DISCLAIMER: The Drill block uses textures based on the Redstone Lamp, and the XMiner uses textures from the furnace. They will be changed at some point before release
---

To compile this project for the first time, you need to follow the typical steps for a Forge installation.
Type the following from the command prompt in your working directory

gradlew setupDecompWorkspace --refresh-dependencies

and then either
gradlew eclipse

or

gradlew idea

to build the jar, use

gradlew build
