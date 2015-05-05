package tp.pr5.control;

import tp.pr5.logic.*;
import tp.pr5.views.window.MainWindow;

import java.util.Scanner;


/**
 * Class which controls the execution of the game in a windows mode.
 *
 * @author : Alvaro Bermejo
 * @author : Francisco Lozano
 * @version : 10/03/2015
 * @since : Assignment 4
 */

public class WindowController extends Controller {
	
    //Attributes
	private Game game;
	private Scanner in;
	private GameTypeFactory currentGame;
	private Player black;
	private Player white;
	private Thread autoThread;
	/**
	 * Class constructor.
	 * 
	 * @param factory The gameType factory to be used.
	 * @param g The game which is to be played.
	 */
	public WindowController(GameTypeFactory factory, Game g) {
        this.game = g;
	    this.in = new Scanner(System.in);
	    this.currentGame = factory;
	    this.black = currentGame.createHumanPlayerAtConsole(in);
		this.white = currentGame.createHumanPlayerAtConsole(in);
		autoThread = new Thread() { //TODO : Change this whole thing to a worker/executor or try the old way
			public void run() {
				while (!game.isFinished() && !autoThread.isInterrupted()) {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!autoThread.isInterrupted() && game.getTurn().getMode() == PlayerType.AUTO) {
						randomMove();
					}
				}
			}
		};
	}
	
	/**
	 * 	Creates the window view. It is assumed that this method is called just once. If it is called again, the behaviour is undefined.
	 */
	@Override
	public void run(){
 		new MainWindow(game,this);
		automaticMove();
	}
	
	//TODO: Implement move with human/automatic player
	public void makeMove(int col, int row, Counter turn) {
		Move move = currentGame.createMove(col, row, turn);
  		try {
			game.executeMove(move);
		} catch (InvalidMove e) {}
		automaticMove();
	}
	
	/**
	 * Undo the last move of the currently played game.
	 */
	public void undo() {
		game.undo();
		automaticMove();
	}
	
	/**
	 * Reset the current game.
	 */
	public void reset() {
		stopAutoPlayer();
		game.reset(currentGame.createRules());
		automaticMove();
	}
	
	/**
	 * Change to a new game.
	 * 
	 * @param gameType The game to be played.
	 * @param dimX The width of the board (necessary only for Gravity).
	 * @param dimY The height of the board (necessary only for Gravity)
	 */
	public void changeGame(GameType gameType, int dimX, int dimY) {
		switch (gameType){
        	case CONNECT4: {
            	currentGame = new Connect4Factory();
			} break;
			case COMPLICA: {
            	currentGame = new ComplicaFactory();
			} break;
			case GRAVITY: {
            	currentGame = new GravityFactory(dimX,dimY);
			} break;
			case REVERSI: {
				currentGame = new ReversiFactory();
			}
		}
		this.game.reset(currentGame.createRules());
		automaticMove();
	}
	
	/**
	 * Make a random move.
	 */
	public void randomMove() {
		Player random = currentGame.createRandomPlayer();
		Move move = game.getMove(random);
        try {
              game.executeMove(move);  								
        } catch (InvalidMove e) {}
		automaticMove();
	}
	
	/**
	 * Quit the application.
	 */
	public void requestQuit() {
            System.exit(0);
	}

	public void setPlayerMode(Counter player, PlayerType selected) {
		player.setMode(selected);
		automaticMove();
	}

	public PlayerType getPlayerMode() {
		return game.getTurn().getMode();
	}

	private void stopAutoPlayer() {
		if (autoThread != null) {
			autoThread.interrupt();
			try {
				autoThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void automaticMove() {
		if (game.getTurn().getMode() == PlayerType.HUMAN)
			return;
		else if (!autoThread.isAlive())
			autoThread.start();

	}
}