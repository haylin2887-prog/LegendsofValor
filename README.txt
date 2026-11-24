LEGENDS OF VALOR
Feifei Qin, Ivan Li


1. FILES
--------
Design Document.txt            design choices, patterns, and evaluation
DesignDoc.txt                  detailed design notes
Legends of Valor.pdf           project spec for reference

src/valor/
  Main.java                    starts the game: loads data, picks heroes, builds the world
  Game.java                    turn loop, hero commands, monster moves, win/lose
  Rules.java                   balance numbers and random rolls

src/valor/battle/
  Combat.java                  resolves attacks and spells, notifies observers
  BattleObserver.java          interface for anything that listens to game events
  BattleObserverAdapter.java   empty version of every event method
  BattleLogger.java            observer that prints events
  BattleStats.java             observer that counts events for the final summary

src/valor/Characters/
  Entity.java                  parent of heroes and monsters
  Hero.java                    hero stats, inventory, leveling; Warrior, Sorcerer, Paladin
  Monster.java                 monster stats; Dragon, Exoskeleton, Spirit
  Party.java                   the player's team of 3 heroes
  GameData.java                loads all heroes, monsters, and items

src/valor/data/
  DataFileReader.java          reads a data file into rows
  RecordParser.java            interface that turns one row into an object
  HeroParser.java              row -> hero
  MonsterParser.java           row -> monster
  WeaponParser.java            row -> weapon
  ArmorParser.java             row -> armor
  PotionParser.java            row -> potion
  SpellParser.java             row -> spell
  HeroFactory.java             interface that creates the right kind of hero
  MonsterFactory.java          interface that creates the right kind of monster
  ItemCatalog.java             every item in the game

src/valor/item/
  Item.java                    parent of all items
  Equipment.java               parent of weapons and armor (repairable)
  Weapon.java                  adds attack damage
  Armor.java                   reduces damage taken
  Potion.java                  raises one or more stats
  PotionStat.java              stats a potion or tile can raise
  Spell.java                   single-use magic attack
  SpellType.java               ICE, FIRE, LIGHTNING
  SpellEffect.java             interface for a spell's side effect
  IceEffect.java               lowers monster damage
  FireEffect.java              lowers monster defense
  LightningEffect.java         lowers monster dodge chance
  Durability.java              uses left on an item

src/valor/market/
  Market.java                  buying, selling, repairing

src/valor/ui/
  Console.java                 reads and checks all player input
  SetupMenu.java               welcome message and hero selection
  HeroMenu.java                target, spell, potion, and equipment menus
  MarketMenu.java              market menus
  WorldView.java               draws the board

src/valor/world/
  World.java                   the board, positions, and movement rules
  GenerateWorld.java           creates a random valid board
  Lane.java                    top, mid, and bot lanes
  Position.java                a row and column on the board
  Directions.java              UP, DOWN, LEFT, RIGHT
  Tile.java                    parent of all tiles
  NexusTile.java               home row of the heroes or monsters
  PlainTile.java               no effect
  BushTile.java                raises dexterity
  CaveTile.java                raises agility
  KoulouTile.java              raises strength
  ObstacleTile.java            blocks movement until removed
  InaccessibleTile.java        wall between lanes

Data/
  Warriors.txt, Sorcerers.txt, Paladins.txt              heroes
  Dragons.txt, Exoskeletons.txt, Spirits.txt             monsters
  Weaponry.txt, Armory.txt, Potions.txt                  items
  FireSpells.txt, IceSpells.txt, LightningSpells.txt     spells


2. HOW TO RUN
-------------
Create the output folder (skip if "out" already exists):
  mkdir out

Compile:
  javac -d out src/valor/*.java src/valor/world/*.java src/valor/ui/*.java src/valor/Characters/*.java src/valor/item/*.java src/valor/data/*.java src/valor/market/*.java src/valor/battle/*.java

Run:
  java -cp out valor.Main

Type Q at any prompt to quit.


3. EXAMPLE PLAYTHROUGH
----------------------
Medium difficulty. H1 heads straight for the monsters' Nexus while H2 and H3
hold their lanes.

==============================================
        Welcome to Legends of Valor!
==============================================
Monsters are marching on your Nexus. Lead a team of
3 heroes, one in each lane, and fight your way north.

HOW TO WIN
  Get any hero into the monsters' Nexus (the top row).
HOW TO LOSE
  Any monster reaches your Nexus (the bottom row).

Type Q at any prompt to quit.


Choose hero 1 of 3:
  1) Gaerdal_Ironhand the Warrior  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  2) Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none
  3) Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  4) Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none
  5) Undefeated_Yoj the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  6) Eunoia_Cyn the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  7) Rillifane_Rallathil the Sorcerer  Lv1  HP 100/100  MP 1300/1300  weapon: none, armor: none
  8) Segojan_Earthcaller the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  9) Reign_Havoc the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  10) Reverie_Ashels the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  11) Kalabar the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  12) Skye_Soar the Sorcerer  Lv1  HP 100/100  MP 1000/1000  weapon: none, armor: none
  13) Parzival the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  14) Sehanine_Moonbow the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  15) Skoraeus_Stonebones the Paladin  Lv1  HP 100/100  MP 250/250  weapon: none, armor: none
  16) Garl_Glittergold the Paladin  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  17) Amaryllis_Astra the Paladin  Lv1  HP 100/100  MP 500/500  weapon: none, armor: none
  18) Caliber_Heist the Paladin  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
> 2
Sehanine_Monnbow joins your team!

Choose hero 2 of 3:
  1) Gaerdal_Ironhand the Warrior  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  2) Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  3) Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none
  4) Undefeated_Yoj the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  5) Eunoia_Cyn the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  6) Rillifane_Rallathil the Sorcerer  Lv1  HP 100/100  MP 1300/1300  weapon: none, armor: none
  7) Segojan_Earthcaller the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  8) Reign_Havoc the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  9) Reverie_Ashels the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  10) Kalabar the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  11) Skye_Soar the Sorcerer  Lv1  HP 100/100  MP 1000/1000  weapon: none, armor: none
  12) Parzival the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  13) Sehanine_Moonbow the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  14) Skoraeus_Stonebones the Paladin  Lv1  HP 100/100  MP 250/250  weapon: none, armor: none
  15) Garl_Glittergold the Paladin  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  16) Amaryllis_Astra the Paladin  Lv1  HP 100/100  MP 500/500  weapon: none, armor: none
  17) Caliber_Heist the Paladin  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
> 2
Muamman_Duathall joins your team!

Choose hero 3 of 3:
  1) Gaerdal_Ironhand the Warrior  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  2) Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none
  3) Undefeated_Yoj the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  4) Eunoia_Cyn the Warrior  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
  5) Rillifane_Rallathil the Sorcerer  Lv1  HP 100/100  MP 1300/1300  weapon: none, armor: none
  6) Segojan_Earthcaller the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  7) Reign_Havoc the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  8) Reverie_Ashels the Sorcerer  Lv1  HP 100/100  MP 900/900  weapon: none, armor: none
  9) Kalabar the Sorcerer  Lv1  HP 100/100  MP 800/800  weapon: none, armor: none
  10) Skye_Soar the Sorcerer  Lv1  HP 100/100  MP 1000/1000  weapon: none, armor: none
  11) Parzival the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  12) Sehanine_Moonbow the Paladin  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none
  13) Skoraeus_Stonebones the Paladin  Lv1  HP 100/100  MP 250/250  weapon: none, armor: none
  14) Garl_Glittergold the Paladin  Lv1  HP 100/100  MP 100/100  weapon: none, armor: none
  15) Amaryllis_Astra the Paladin  Lv1  HP 100/100  MP 500/500  weapon: none, armor: none
  16) Caliber_Heist the Paladin  Lv1  HP 100/100  MP 400/400  weapon: none, armor: none
> 2
Flandal_Steelskin joins your team!

Your team:
  Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  XP 8  Gold 2500  STR 770 DEX 500 AGI 880  weapon: none, armor: none
  Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  XP 6  Gold 2546  STR 990 DEX 750 AGI 550  weapon: none, armor: none
  Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  XP 7  Gold 2500  STR 825 DEX 700 AGI 715  weapon: none, armor: none

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [H1       ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

Difficulty (how often a new wave of monsters spawns):
  1) Easy   - every 8 rounds
  2) Medium - every 6 rounds
  3) Hard   - every 4 rounds, and monsters get terrain bonuses too
> 2

Three monsters appear at the Monsters' Nexus...
  >> A new monster appeared: [M1] Cyrrollalee the Exoskeleton  Lv1  HP 100  DMG 5  DEF 126  DODGE 50%
  >> A new monster appeared: [M2] BunsenBurner the Dragon  Lv1  HP 100  DMG 6  DEF 125  DODGE 50%
  >> A new monster appeared: [M3] BunsenBurner the Dragon  Lv1  HP 100  DMG 6  DEF 125  DODGE 50%

################ ROUND 1 ################

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [H1       ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 7, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 6, col 0 (Plain).

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 6, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 6, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 2 ################

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 6, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Plain
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 5, col 0 (Bush (+10% DEX)).

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600 (+10% DEXTERITY)  weapon: none, armor: none  @ row 5, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600 (+10% DEXTERITY)  weapon: none, armor: none  @ row 5, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 3 ################

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600 (+10% DEXTERITY)  weapon: none, armor: none  @ row 5, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Bush (+10% DEX)
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 4, col 0 (Plain).

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [M3       ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 4 ################

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [M1       ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv1  HP 100/100  MP 600/600  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Plain
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 2
  >> Sehanine_Monnbow (H1) attacked Cyrrollalee (M1) for 148 damage!
  >> Cyrrollalee (M1) was defeated!
     Sehanine_Monnbow earned 2 XP and 500 gold.  LEVEL UP! Now level 2!
     Muamman_Duathall earned 2 XP and 500 gold.
     Flandal_Steelskin earned 2 XP and 500 gold.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 5 ################

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 4, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Plain
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 3, col 0 (Cave (+10% AGI)).

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 3, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 3, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 6 ################

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 3, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Cave (+10% AGI)
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 2, col 0 (Plain).

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 2, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Muamman_Duathall passes the turn.

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 2, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  (Surviving heroes recover 10% HP and MP.)
  >> A new monster appeared: [M4] Exodia the Exoskeleton  Lv2  HP 200  DMG 10  DEF 220  DODGE 50%
  >> A new monster appeared: [M5] Aasterinian the Exoskeleton  Lv2  HP 200  DMG 10  DEF 275  DODGE 50%
  >> A new monster appeared: [M6] Chrysophylax the Dragon  Lv2  HP 200  DMG 11  DEF 500  DODGE 50%

################ ROUND 7 ################

        [         ] [M4       ] [XXXXXXXXX] [         ] [M5       ] [XXXXXXXXX] [         ] [M6       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660  weapon: none, armor: none  @ row 2, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Plain
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 1, col 0 (Cave (+10% AGI)).

        [         ] [M4       ] [XXXXXXXXX] [         ] [M5       ] [XXXXXXXXX] [         ] [M6       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 1, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Muamman_Duathall (H2)'s turn ===
Muamman_Duathall is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 2
  >> BunsenBurner (M2) dodged Muamman_Duathall's attack!

        [         ] [M4       ] [XXXXXXXXX] [         ] [M5       ] [XXXXXXXXX] [         ] [M6       ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [M3       ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [         ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 1, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Flandal_Steelskin (H3)'s turn ===
Flandal_Steelskin is standing on: Nexus
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 9
  Flandal_Steelskin passes the turn.

--- Monsters' turn ---
  >> Muamman_Duathall (H2) dodged BunsenBurner's attack!
  >> Exodia (M4) attacked Sehanine_Monnbow (H1) for 10 damage!
  (Surviving heroes recover 10% HP and MP.)

################ ROUND 8 ################

        [         ] [M4       ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    0   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M5       ] [XXXXXXXXX] [         ] [M6       ]
    1   [--- C ---] [--- C ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- K ---] [--- P ---]
        [H1       ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    2   [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    3   [--- C ---] [--- P ---] [XXXXXXXXX] [--- O ---] [--- B ---] [XXXXXXXXX] [--- K ---] [--- O ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    4   [--- P ---] [--- O ---] [XXXXXXXXX] [--- K ---] [--- B ---] [XXXXXXXXX] [--- P ---] [--- B ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    5   [--- B ---] [--- O ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- P ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [M2       ] [XXXXXXXXX] [M3       ] [         ]
    6   [--- P ---] [--- K ---] [XXXXXXXXX] [--- P ---] [--- P ---] [XXXXXXXXX] [--- C ---] [--- P ---]
        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]

        [         ] [         ] [XXXXXXXXX] [         ] [         ] [XXXXXXXXX] [         ] [         ]
    7   [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---] [XXXXXXXXX] [--- N ---] [--- N ---]
        [         ] [         ] [XXXXXXXXX] [H2       ] [         ] [XXXXXXXXX] [H3       ] [         ]

                  Top                                 Mid                                 Bot

  [H1] Sehanine_Monnbow the Warrior  Lv2  HP 200/200  MP 660/660 (+10% AGILITY)  weapon: none, armor: none  @ row 1, col 0
  [H2] Muamman_Duathall the Warrior  Lv1  HP 100/100  MP 300/300  weapon: none, armor: none  @ row 7, col 3
  [H3] Flandal_Steelskin the Warrior  Lv1  HP 100/100  MP 200/200  weapon: none, armor: none  @ row 7, col 6

=== Sehanine_Monnbow (H1)'s turn ===
Sehanine_Monnbow is standing on: Cave (+10% AGI)
   1) Move  2) Attack  3) Cast spell  4) Use potion  5) Equip  6) Teleport  7) Recall  8) Remove obstacle  9) Pass turn
   i) Info  b) Show board  m) Market (at the Heroes' Nexus)  (the letters don't use your turn)
> 1
  Move W(up) A(left) S(down) D(right), 0 to cancel: w
  Sehanine_Monnbow moved to row 0, col 0 (Nexus).

*****************************************************
*  HEROES VICTORY! Sehanine_Monnbow (H1) reached the Monsters' Nexus!
*****************************************************
Game summary: 8 round(s), 148 damage dealt, 10 damage taken, 1/6 monster(s) defeated, 0 hero faint(s), 2 dodge(s).
Thanks for playing Legends of Valor!
