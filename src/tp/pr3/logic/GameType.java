package tp.pr3.logic;

import tp.pr3.control.ComplicaFactory;
import tp.pr3.control.Connect4Factory;
import tp.pr3.control.GravityFactory;

/**
 * Represents the type of the game the user is playing to and contains its rules
 * The enumeration is used to identify the kind of game (Connect 4 or Complica) and get the rules attached to it.
 *
 * @author: Alvaro Bermejo
 * @author: Francisco Lozano
 * @version: 08/01/2015
 * @since: Assignment 2
 */

public enum GameType {
	CONNECT4(new Connect4Factory().createRules()), COMPLICA(new ComplicaFactory().createRules()), GRAVITY(new GravityFactory().createRules());

	//Attributes
	private final GameRules gameRules;

	//Constructor

	/**
	 * Constructor of the enumerated type
	 *
	 * @param gameRules The rules of the game
	 */
	GameType(GameRules gameRules) {
		this.gameRules = gameRules;
	}

	//Methods

	/**
	 * Accessor method which returns the rules of the game depending on the enumerated type
	 *
	 * @return Rules attached to the game type
	 */
	public GameRules getGameRules() {
		return gameRules;
	}
}


