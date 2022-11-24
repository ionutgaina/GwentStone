

# GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

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
