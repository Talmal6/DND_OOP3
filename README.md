# Console Based Dungeons & Dragons Game
Welcome to the **Dungeon and Dragons Console Game** repository!  
This is a console-based, single player, multi-level version of a dungeons and dragons board game, with Game of Thrones theme.  
You are trapped in a dungeon, surrounded by enemies. Your goal is to fight your way through them and get
to the next level of the dungeon. Once you complete all the levels, you win the game.


## Project Goals
* Practice different OOP design patterns (Observer, Factory, Strategy, etc.)
* Practice dynamicly designing the project with high modularity
* Practice proper Separation of Concerns inside the project, mainly seperating the Frontend (CLI) from the Backend
* Experience working around different language limitations, while avoiding bad coding habits (instanceOf, casting, etc.)
* Finding effecient solutions to different design problems
* Practice proper unit testings and debugging

## Features
* 7 different playable characters
* 4 different classes
* 13 different enemy types including 3 bosses and 3 traps
* Console-based UI
* Modular and dynamic design, for easy future changes and additions

## Table of Contetns
* [How To Run](#how-to-run)
* [The Game Board](#the-game-board)
* [Game Flow](#game-flow)
* [Comnat](#combat)
* [The Player](#the-player)
  - [Classes](#classes)
  - [Characters](#characters)
* [Enemies](#enemies)
  - [Monsters](#monsters)
  - [Traps](#traps)
  - [Bosses](#bosses)
* [CLI](#cli)
  - [Controls](#controls)
* [About](#about)

## How To Run
The program takes a path of directory as command-line argument. The directory contains files represent
the game boards. Each file is named `level<i>.txt` where `<i>` is the number of the level (For example: `level1.txt`, `level2.txt` etc.)
For convinience, there is a [levels folder](src/main/java/dnd/levels_dir) in the source file, we recommend using it.

**To Run The Game:**
1. Compile the game
2. Run the game
```
java -jar jarfilename.jar <levels_dir>
```

## The Game Board
The game board consists of a [player](#the-player), [enemies](#enemies) of different types, walls, and empty areas that both players and enemies can walk on.
Here is a list of symbols and what they represent:
* `@` - The [player](#the-player).
* `.` - An empty space. The player and enemies can move to this space.
* `#` - A wall. The player and enemies cannot move to this place.
* All other symbols represent different types of [enemies](#enemies).

## Game Flow
In the first screen, the player chooses their character from the [list of characters](#characters) (by typing in the relevant number).
After that the game starts with the first level. Each level consists of several rounds. A round, also called Game
Tick, is defined as follows:
- The player performs a single action.
- Each enemy performs a single action.  
The level ends once the enemies are all dead. In this case, the next level will be loaded up. The game ends once the player finished all levels, or if the player dies.

## Combat
When the player attempts to step on a location that has an enemy, or when an enemy attempts to step on
the player’s location, they engage in melee combat.
The attacker is always the unit that performed the step. The other unit will attempt to defend itself. The
combat goes as follows:
1. The attacker rolls an amount between 0 and its attack damage.
2. The defender rolls an amount between 0 and its defense points.
3. If (attack roll − defense roll) > 0, the defender will be damaged, losing health equal to that amount.
4. The defender may die as a result of this attack if his health goes to or below 0.
* If an enemy is killed by the player:
  - The player gains the experience value of the enemy.
  - The enemy is removed from the game.
  - The player takes the position of the enemy.
* If the player is killed by an enemy, the player’s location is marked with `X` and the game ends.

## The Player
The player may choose one of 7 different [characters](#characters) to play. Each charaters belongs to one of 4 [classes](#classes), and has different starting stats.

### Classes
Each class has a special ability. Player may cast a special ability to improve its situation at the cost of resources.

* #### Warrior
  **Avenger’s Shield:** randomly hits one enemy withing range < 3 for an amount equals to 10% of the warrior’s max health and heals the     warrior for amount equals to 10×defense (but will not exceed the total amount of health pool).

* #### Mage
  **Blizzard:** randomly hit enemies within range for an amount equals to the mage’s spell power at the cost of mana.

* #### Rogue
  **Fan of Knives:** hits everyone around the rogue for an amount equals to the rogue’s attack points at the cost of energy.

* #### Hunter
  **Shoot:** hits the closest enemy for an amount equals to the hunter’s attack points at the cost of an arrow.

### Characters
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/92e07e60-ebfe-4656-8a28-2a5b9a1236b0)
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/00815701-5a09-424d-b31b-ff7e0867d78e)

## Enemies
The player may encounter enemies while traveling around the world. The enemies are divided into three types.

### Monsters
Monsters can move 1 step in any direction (if it's empty), and may chase the player if the player is within its vision range.  
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/d5e1e2bc-9212-41fb-877c-efa13e754709)

### Traps
A trap can’t move (unlike monsters), but updates its state (visibility) on each turn. After `visibility time` game ticks, the trap will become invisible. The trap becomes visible again after `invisibility time` game ticks elapsed. The trap may attack the player if range < 2.  
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/ae0ab067-c028-4a91-8134-f58097146aa7)

### Bosses
Some Monsters are Bosses. Bosses behave mostly like monsters, except they can also use special abilities. 
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/f8b71f39-8403-4d0f-855b-7dcd4c994338)

## CLI
The CLI (Command Line Interface) is reponsible for everything that is outputed to the screen.
The CLI displays the game state after each round. That is:
* Player’s stats (name, health, attack damage, defense points, level, experience, and class-specific properties).
* Enemy stats (when in comnat).
* Whole board.
* Notifications (Damage rolls, Level up, Special ability casts, etc.).

### Controls
The player may perform any of the following actions, using the assigned input:  
![image](https://github.com/Talmal6/DND_OOP3/assets/118106721/f95f2b6e-fbf1-45c3-bdde-17baecea2b7f)

## About
This game was coded in Java by **Or Malky & Tal Malma** as final project for 'Principles of OOP' (OOSD) course @ BGU 23
