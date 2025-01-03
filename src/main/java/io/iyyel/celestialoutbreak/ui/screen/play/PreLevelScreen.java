package io.iyyel.celestialoutbreak.ui.screen.play;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.controller.GameController.State;
import io.iyyel.celestialoutbreak.handler.LevelHandler;
import io.iyyel.celestialoutbreak.level.Level;
import io.iyyel.celestialoutbreak.ui.screen.AbstractScreen;

import java.awt.*;

public final class PreLevelScreen extends AbstractScreen {

    private final LevelHandler levelHandler = LevelHandler.getInstance();

    public PreLevelScreen(GameController gameController) {
        super(gameController);
    }

    @Override
    public void update() {
        decInputTimer();

        if (inputHandler.isOKPressed() && isInputAvailable()) {
            resetInputTimer();
            soundHandler.stopAllSound();
            menuNavClip.play(false);
            levelHandler.getActiveLevel().playSound();
            levelHandler.getActiveLevel().getLevelTimer().resetTimer();
            levelHandler.getActiveLevel().getLevelTimer().startTimer();
            gameController.switchState(State.PLAY);
        }

    }

    @Override
    public void render(Graphics2D g) {
        Level activeLevel = levelHandler.getActiveLevel();
        drawTitle(g);
        drawSubtitle(activeLevel.getName(), g);
        drawCenteredText(activeLevel.getDesc(), 0, g);
        drawInfoPanel(g);
    }

}