# Prova finale di Ingegneria del Software -- AA 2021-2022

## AM40 Group
### Camilla Casiraghi, Carlo Chiodaroli, Manuel Colombo

Implementation of Eriantys board game

## Implementation

|              __What__              | __Status__ |
|:----------------------------------:|:----------:|
|          Simplified Rules          |     🟢     |
| Complete Rules (8 character cards) |     🟢     |
|               Socket               |     🟢     |
|                CLI                 |     🟢     |
|                GUI                 |     🟢     |
|   __Additional Functionalities__   |     2      |
|         12 Character cards         |     🟢     |
|           4 Player Play            |     🔴     |
|           Multiple Plays           |     🔴     |
|            Persistence             |     🔴     |
|             Resilience             |     🟢     |

*🟢 Done; 🔴 Not Done*

## Actual test coverage
|   __Package__    | __Class %__ | __Method %__ | __Line %__ |
|:----------------:|:-----------:|:------------:|:----------:|
|      model       |     100     |      89      |     86     |
| controller.inner |     100     |      76      |     73     |

## Run Instructions

###Run the jar file

To run the Cli Client
<pre>java -jar AM40.jar --c</pre>

To run the Gui Client
<pre>java -jar AM40.jar --g</pre>

To run the Server
<pre>java -jar AM40.jar --s</pre>

To run the Server on a specific port
<pre>java -jar AM40.jar --s --port [port number] </pre>

### Server commands

The server prints on screen only the log of the sent and received messages.

### Cli commands

Once started the prompt will ask for server info:
<pre>
* ip [address]  : to set the server's ip address (default is localhost)
* port [number] : to set the server's listening port (default is 16847)
</pre>
Once connected you are asked to input your name:
<pre>
name [name] : to set the name
nickname [name] : the same as name command
</pre>
If the server accepts the connection the user can finally set play settings or customizations.

The player can set here which wizard he wants to be; he can choose one of the shown options with this command:
<pre>
wizard [KING | FAIRY | MAGICIAN | BAMBOO_GUY]
</pre>
If the user is the first to connect to the server, the server considers him as the "main Player".
This classification is used only for play creation purposes. The main player is the only player 
allowed to set the total number of players and the play's ruleset with theese commands:
<pre>
players [number]        : to set the number oh the game (accepted values are 2 and 3)
expert [true | false]   : to set the normal ruleset (false), or the expert ruleset (true)
</pre>
After all players have set their wizards and the main player has set the play settings, 
the latter one can start the game with the command:
<pre>
start   : starts the game
</pre>
At this point the game is started and is helpful to play it with the game's player handbook.

The commands to do all the possible moves in the game are:
<pre>
assistant [number]                      : in Planning phase, to play an assistant card.
studentmove [color] [from id] [to id]   : in action phase, to move a student of a color from a "from" place to a "to" place.
mnmove [#hops]                          : in action phase, to move mother nature of a certain number of hops
influence                               : in action phase, to calc the influence
choose [cloud id]                       : in action phase, to choose a cloud
</pre>
These commands are for expert ruleset games:
<pre>
character [character]                           : to play a character card. To identify her you need to use the name of the character (i.e. FRIAR)
character [character] [color]                   : the same of ca, but for those cards who need it.
character [character] [island id]               : the same of ca, but for those cards who need it.
studentmove [from color] [to color] [place id]  : is a particular student move that shifts two students one from the "Entrance" (from color) the other from the place (to color).
</pre>
Anyway the user can always use:
<pre>
help : to show the list of possible commands and abbreviations.
</pre>
### Gui commands
Once started it shows the Gui, follow the instructions to start a play.

When playing wait your turn, then you can submit your moves.
The submittable ones are:
<pre>
Play an assistant card  : choose an assistant card, then click on submit.
Move a student          : SM button, Color of student to move, From place, To place, Submit.
Switch two students     : SM button, Color of student from entrance, Color of student from other place, other place, Submit.
Move mother nature      : MNM button, island of arrival, Submit.
Calc influence          : IN button, Submit.
Choose a cloud          : CH button, select the cloud, Submit.
play a character card   : CA button, if needed click on an island or a student, Submit.
</pre>

## Project progress:

|                __Phase__                 | __Status__ |
|:----------------------------------------:|:----------:|
|  Game model's initial UML class diagram  |    Done    |
|           Model Implementation           | Retouching |
|  *Model FA implementation and testing*   |    Done    |
|        Controller implementation         | Retouching |
|   *MCV FA implementation and testing*    | Retouching |
|      Communication protocol design       |    Done    |
|  Communication protocol Implementation   |    Done    |
|          Client Implementation           | Retouching |
|                 CLI view                 | Retouching |
|                 GUI view                 | Retouching |
|||
|                 Javadoc                  |   Making   |
|        Final Application Testing         |   Doing    |
| Delivery of "'deliveries' folder things" |   Making   |
|    __Final delivery__ on 27th of june    |     -      |

*Technical Issues buffer time: 4 days*

*Notes:*
* *Implementation phases include singular class testing*
* *Testing phases are intended for finalizing class interaction testing*
* *Italics phases are "if we have time" phases*

