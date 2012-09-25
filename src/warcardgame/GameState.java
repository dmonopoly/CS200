package warcardgame;

// GameState manages the state of the game when War is actually playing - i.e., within Game.java
public enum GameState {
	// "War over" states; used in playWar()
    DRAW, PLAYER_1_GAME_VICTORY, PLAYER_2_GAME_VICTORY, // game over enums 
    PLAYER_1_WAR_VICTORY, PLAYER_2_WAR_VICTORY, // game continues enums
    CONTINUE_WAR,
    
    // Only these are used for the gameState instance variable!
    // for card showing states; used in step() and gameState instance variable
    SHOULD_PLAY_CARDS, SHOULD_CLEAR_CARDS, SHOULD_PLAY_WAR_FACE_DOWN, SHOULD_PLAY_WAR_FACE_UP, SHOULD_TEST_WAR,
    SHOULD_EXIT_GAME
}