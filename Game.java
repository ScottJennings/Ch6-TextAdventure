/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author Scott Jennings
 * @version 2015.04.05
 * 
 * This version has 16 rooms and 6 new features
 * The new features are:
 * 1. up/down direction
 * 2. look command
 * 3. photo command (text confirmation)
 * 4. back command
 * 5. general item support (item object and printing in room description)
 * 6. support for multiple items in a room
 * 
 * Summary:
 * The world explored in this adventure is a natural science museum based on the D.C. Smithsonian.
 * It has 16 rooms on 3 floors and allows up/down movement between floors.  In addition to compass
 * direction moves, it supports a back move, a look command (replay room description), and a photo
 * command to take a photo of the room.  The rooms can contain contain zero or more items.  Making
 * the item an independent object will allow future versions to have a player carry items between rooms.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room priorRoom = null;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room rotunda, mammals, oceans, theatre, geology, astronomy, planetarium, mezzanine, galleries, mummies, gems, dinosaurs, research, auditorium, gifts, cafe;
      
        // create the rooms
        rotunda = new Room("inside the Smithsonian rotunda");
        mammals = new Room("in the mammals exhibit");
        oceans = new Room("in the oceans exhibit");
        theatre = new Room("in the theatre exhibit");
        geology = new Room("in the geology exhibit");
        astronomy = new Room("in the astronomy exhibit");
        planetarium = new Room("in the planetarium room");
        mezzanine = new Room("in the mezzanine room");
        galleries = new Room("in the galleries room");
        mummies = new Room("in the mummies room");
        gems = new Room("in the gems room");
        dinosaurs = new Room("in the dinosaurs room");
        research = new Room("in the research room");
        auditorium = new Room("in the auditorium");
        gifts = new Room("in the giftship");
        cafe = new Room("in the cafe");
        
        // initialise room exits on first floor
        rotunda.setExit("north", oceans);
        rotunda.setExit("west", mammals);
        rotunda.setExit("east", astronomy);
        rotunda.setExit("up", mezzanine);
        rotunda.setExit("down", auditorium);
        
        mammals.setExit("north", geology);
        mammals.setExit("east", rotunda);

        oceans.setExit("west", geology);
        oceans.setExit("east", theatre);
        oceans.setExit("south", rotunda);
        
        theatre.setExit("west", oceans);
        
        geology.setExit("south", mammals);
        geology.setExit("east", oceans);
        
        astronomy.setExit("west", rotunda);
        astronomy.setExit("east", planetarium);
        
        planetarium.setExit("west", astronomy);
        
        // initialize room exits on the second floor
        mezzanine.setExit("north", galleries);
        mezzanine.setExit("west", mummies);
        mezzanine.setExit("east", gems);
        mezzanine.setExit("down", rotunda);
        
        galleries.setExit("south", mezzanine);
        galleries.setExit("east", research);
        
        mummies.setExit("north", dinosaurs);
        mummies.setExit("east", mezzanine);
        
        gems.setExit("west", mezzanine);
        
        dinosaurs.setExit("south", mummies);
        
        research.setExit("west", galleries);
        
        // initialize room exits on the ground floor
        auditorium.setExit("north", gifts);
        auditorium.setExit("up", rotunda);
        
        gifts.setExit("west", cafe);
        
        cafe.setExit("east", gifts);
        cafe.setExit("south", auditorium);
        
        //create items
        Item datingTool = new Item("equipment to date artifacts",2);
        Item dino = new Item("dinosaur skeleton",5000);
        Item mammoth = new Item("woolly mammoth",2600);
        Item hopeDiamond = new Item("the Hope diamond",3);
        Item kingTutMask = new Item("King Tut's mask",75);
        Item giantSquid = new Item("giant squid", 800);
        Item meteorite = new Item("meteorite", 1);
        Item mummyCase = new Item("mummy case", 45);
        Item infoDesk = new Item("information desk", 200);
        
        //place items in rooms
        research.placeItem(datingTool);
        dinosaurs.placeItem(dino);
        rotunda.placeItem(mammoth);
        gems.placeItem(hopeDiamond);
        mummies.placeItem(kingTutMask);
        oceans.placeItem(giantSquid);
        geology.placeItem(meteorite);
        mummies.placeItem(mummyCase);
        rotunda.placeItem(infoDesk);
        
        currentRoom = rotunda;  // start game in the rotunda
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK: // LAB 6
                look();
                break;
            
            case PHOTO: // LAB 6
                printPhoto();
                break;
            
            case BACK: // LAB 6
                goBack();
                break; 
                
            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the museum.");  //change for LAB 6 for museum world
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /**
     * Prints that the user that a photo has been taken.  For LAB 6.
     */
    private void printPhoto()
    {
        System.out.println("You have taken a photo of the room");
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            priorRoom = currentRoom;  // LAB 6
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /**
     * Takes the user back by one room.  For LAB 6.
     */
    private void goBack()
    {
        if (priorRoom == null)
        {
            System.out.println("You have only just arrived and have no rooms to go back to.");
        }
        else{
            currentRoom = priorRoom;
            System.out.println("You have gone back." + "\n" + currentRoom.getLongDescription());
        }
    }
    
    /**
     * "Look" was entered.  For LAB 6.
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Starts the game outside of BlueJ
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        game.play();
    }
}
