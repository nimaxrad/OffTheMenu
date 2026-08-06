package app;
import audio.Music;
import audio.SoundEffects;

import core.GamePanel;

/**
 * @Author Nimarad
 * @author Ken Tran
 * Handles state changes in game (eg. from play to win)
 */

public class StateTransitionHandler {

    private final GamePanel gp;
    private final Music music;
    private final SoundEffects sound;

    public StateTransitionHandler(GamePanel gp, Music music, SoundEffects sound) {
        this.gp = gp;
        this.music = music;
        this.sound = sound;
    }

    public void handle(StateManager.gameState state, StateManager.gameState prevState) {
        switch (state) {
            case WIN:
                handleWin();
                break;

            case LOSE:
                handleLose();
                break;

            case PLAY:
                handlePlay(prevState);
                break;

            case PAUSE:
                // nothing special
                break;

            case TITLE:
            default:
                handleTitle();
                break;
        }

        gp.uiM.resetSelectorPosition();
    }

    // STATE HANDLERS

    private void handleWin() {
        music.stop();
        sound.play(3);

        int score = calculateScore();
        gp.player.score = score;
    }

    private void handleLose() {
        music.stop();
        sound.play(4);

        boolean showHighScore = gp.player.score > 0;
        gp.uiM.showNewHighScore(showHighScore);
    }

    private void handlePlay(StateManager.gameState prevState) {
        if (prevState != StateManager.gameState.PAUSE) {
            resetLevelForNewRun();
        }
        music.stop();
        music.play(0);
    }

    private void handleTitle() {
        gp.uiM.resetPlayScreen();
        gp.player.setDefaultValues();
        music.play(0);
    }

    // HELPERS

    private int calculateScore() {
        return gp.player.score;
    }

    private void resetLevelForNewRun() {
        gp.getEngine().setElapsedTicks(0);
        gp.uiM.resetPlayScreen();
        gp.player.setDefaultValues();

        if (gp.level != null) {
            gp.level.spawnEntities(gp.getEngine().getMap(), gp);
        }
    }
}
