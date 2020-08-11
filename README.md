# Shulker's Super Simple Structure System

Do you have cool builds? Want to share them with friends and have them generate randomly in their. or your world? This mod aims to make creating structures simple without requireing any knowledge regaurding programming languages.

The text below is taken from the description on my curseforge page for the mod. It includes a basic getting started tutorial.

Information about each structure such as its name, how far to sink it into the ground, rarity, and valid biomes can be set through a json file. Currently the mod only supports single piece structures (one nbt file), but I plan to make more possible.

 

Tutorials coming soon; basic getting started info below.

 

# Read before installing this mod
Mojang introduced a bug in 1.16 which affects all mods which add structures. If a structure is removed or renamed (things you can do in this mod if you're not careful when changing a structure's json file), it is possible to corrupt your worlds upon loading. Please back up your worlds before opening them with any mods which add structures in 1.16. It is inadvisable to use this mod on a long-standing world, but if you do be sure to take regular backups. See MC-194811 on Mojang's bug tracker for more details.

 


# Getting Started
 

1. Make a new minecraft instance for this mod

This isn't strictly necessary, but is highly recommended. If you don't, you risk accidentally opening a world you didn't mean to and be forced to keep your current structures or risk corrupting the world. The details on how to do this will vary depending on which launcher you use, but using the default launcher you will want to go to Installations on the top and press the new button. Make sure the game version is forge for 1.16.1, and where it says game directory, make a new folder under your minecraft install (%appdata%/.minecraft/<profileNameHere>). If you run into any issues there are many in-depth articles online regarding how to do this.

2. Set up the folders for your structures

In the files section of this page, below the download for the mod jar there should be an additional files section containing a zip folder with examples. You can also download it off of my github using this link. Download the zip folder, uncompress it, and put it in the base directory of the profile which you will be using (%appdata%/.minecraft by default, but if you did the first step it will be (%appdata%/.minecraft/<profileNameHere>). If you don't want to download the zip folder, the directory layout is below. An example json file is included at the bottom of the page.

Minecraft Game Directory

............|_shulkerssupersimplestructuresystem

......................|_-structurejsons

......................|_-structureNBTs

All of your json files describing your structures will go in the structurejsons folder, and all of your NBT files made in-game containing the block data for your structures will go in the structureNBTs folder.

 

3. Make your structures

There's too much for me to include here and I don't have the time at the moment, but if you download the examples folder it contains a few structure json files which are heavily commented and should make it clear how you need to describe your structures. It also contains a set of nbt files I've made so you can try making your own json files and get them spawning in your world if you want to practice. When you want to make your own structures, there are some great tutorials on youtube which describe it better than I could here in text.

 

4. Share your creations with your friends! (or keep them to yourself)

If you want to share your structures with your friends, all you have to do is zip up the shulkerssupersimplestructuressystem folder in your Minecraft Game Directory and have them unzip it and put it in their directory! They will also need this mod installed of course.

 

Tips and tricks

The jsons describing the structures are loaded when you launch minecraft, but the nbt files aren't opened until you open your world. If you're making changes to the nbt files you shouldn't have to completely close Minecraft, just go back to the title screen
 

Example json file:

Sorry for the poor formatting, all the tabs and newlines got removed when I pasted it here

 

{{

"comment":"#### You can remove any lines starting with the word comment. JSON doesn't support comments so this is how we have to do them.",   

"comment2":"#### Note the format of the name of this file. Minecraft will throw errors if you have capital letters, spaces, or most special characters in the file name",   

"comment3":"#### The valid characters for file names include lowercase a-z, 0-9, the underscore character (_), and the minus character (-)",   

"comment4":"#### Make sure to close this file in any editors before launching minecraft. Some editors won't let go of the file and cause it to fail to parse'",


"comment_name": "//// You can name your structure whatever you want and this is what will show up using /locate, but once you have set this don't change it.",

"comment_name2":"//// There's a nasty bug in 1.16.1 where removing or renaiming structures can corrupt worlds",

"comment_name3":"//// Also note that this name can only contain lowercase letters and no special characters or numbers",

"structureName":"dirthutexample",


"comment_seed": "//// It is very important that you set this to a large, unique number for each structure you add. Otherwise your structures will generate on top of each other",

"comment_seed2": "//// This number must be under int_max which is 2147483647",

"seed":1235437262,


"comment_biomes": "//// The biomes it can spawn in. These use the registry keys for the biomes. If the f3 biome name doesn't work then check the wiki, it may be different",

"biomes":[ "ocean", "plains", "desert", "extreme_hills", "forest", "taiga", "swampland", "river", "hell", "sky", "frozen_ocean", "frozen_river", "ice_flats", "ice_mountains", "mushroom_island", "mushroom_island_shore", "beaches", "desert_hills", "forest_hills", "taiga_hills", "smaller_extreme_hills", "jungle", "jungle_hills", "jungle_edge", "deep_ocean", "stone_beach", "cold_beach", "birch_forest", "birch_forest_hills", "roofed_forest", "taiga_cold", "taiga_cold_hills", "redwood_taiga", "redwood_taiga_hills", "extreme_hills_with_trees", "savanna", "savanna_rock", "mesa", "mesa_rock", "mesa_clear_rock", "mutated_plains", "mutated_desert", "mutated_extreme_hills", "mutated_forest", "mutated_taiga", "mutated_swampland", "mutated_ice_flats", "mutated_jungle", "mutated_jungle_edge", "mutated_birch_forest", "mutated_birch_forest_hills", "mutated_roofed_forest", "mutated_taiga_cold", "mutated_redwood_taiga", "mutated_redwood_taiga_hills", "mutated_extreme_hills_with_trees", "mutated_savanna", "mutated_savanna_rock", "mutated_mesa", "mutated_mesa_rock", "mutated_mesa_clear_rock" ],


"comment_spawnRate":"//// How far apart, in chunks, your structure will try to generate. Structures with fewer valid biomes will be rarer with the same spawn rate set",

"spawnRate":12,


"comment_yOffset":"//// How many y levels down to shift your structure from having the bottom at ground level. A negative number will make it go up instead",

"blockLevelsBelowGround":2,

 

"comment_filename":"//// The name of the nbt file with your structure. If you don't include the file extention in the name it will crash the game",

"fileName":"dangerous_dirt_hut.nbt"}

 
