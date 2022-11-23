

# Tema POO  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Tests

1. test01_game_start - 3p
2. test02_place_card - 4p
3. test03_place_card_invalid - 4p
4. test04_use_env_card - 4p
5. test05_use_env_card_invalid - 4p
6. test06_attack_card - 4p
7. test07_attack_card_invalid - 4p
8. test08_use_card_ability - 4p
9. test09_use_card_ability_invalid -4p
10. test10_attack_hero - 4p
11. test11_attack_hero_invalid - 4p
12. test12_use_hero_ability_1 - 4p
13. test13_use_hero_ability_2 - 4p
14. test14_use_hero_ability_1_invalid - 4p
15. test15_use_hero_ability_2_invalid - 4p
16. test16_multiple_games_valid - 5p
17. test17_multiple_games_invalid - 6p
18. test18_big_game - 10p


<div align="center"><img src="https://tenor.com/view/homework-time-gif-24854817.gif" width="500px"></div>

## Implementation

### Main
In the main class, created the players instances with their decks and started each game from the input,
furthermore here i used a utility class to call each command from the server.

### CommandUtility
This is the utility class where created for each command a private method which is called by
static method commandAction in a switch case, where i transmit the necessary parameters for each command.
Every method for commands is implementation for the command and we use Json & Jackson for creating the json
which is the output for our server.

### Game
Using Singleton pattern to use it where needed such as in commandUtility.
There we keep tracking about the game (players, rounds and table)

### Player
Each player is having nr of wins, nr of played games and their decks, playing deck , his hand and
which hero is playing. Here we hame methods when the player need to draw a card, start a game.

### Hand
The hand is used by Player, and here we have nr of cards from the hand, which we add from the playing deck.

### Deck
This class is used for tracking the playing deck for the player.
Is created just with environments and minions cards.

### Round
The round is tracking the turns and which is the active player.

### Table
This is the playground for the players, where we have 4 rows, first 2 is for the player two 
3 and 4 is for the player one. in each row we have just minions and the maximum capacity is 5 minions per row.
In this class we have methods where can help us and is specialized in this class
here we can verify if in a row exist a tank, or we can take all frozen minions or from the table.

### CardInput
This class from the skel we used for extends the minion, env and hero class, because we need the 
same fields, i added just a copy constructor and cardType, to know which card is it.

### Environment
Implemented the environment card with all the effects, where we call them when we 
use the card effect for the environment card.

### Hero
Implemented the hero card with all the abilities, where we call them when we need to use the hero ability
for the hero card.
And here we tack the health of the hero, where we know who won.

### Minion
Implemented the minion card with all the abilities, where we call them when we need to use the minion ability
for the minion card.
This card can attack a card and hero, and he has fought and frozen state, to know 
if he can make actions.
Furthermore, he cas a field which is the tank, to know if the enemy player can attack others cards (minion/hero).
