package io.iyyel.celestialoutbreak.ui.screen.play;

import io.iyyel.celestialoutbreak.controller.GameController;
import io.iyyel.celestialoutbreak.controller.GameController.State;
import io.iyyel.celestialoutbreak.data.dao.HighScoreDAO;
import io.iyyel.celestialoutbreak.data.dao.PlayerDAO;
import io.iyyel.celestialoutbreak.data.dao.interfaces.IHighScoreDAO;
import io.iyyel.celestialoutbreak.data.dao.interfaces.IPlayerDAO;
import io.iyyel.celestialoutbreak.data.dto.HighScoreDTO;
import io.iyyel.celestialoutbreak.handler.LevelHandler;
import io.iyyel.celestialoutbreak.handler.LogHandler;
import io.iyyel.celestialoutbreak.level.Level;
import io.iyyel.celestialoutbreak.ui.screen.AbstractScreen;

import java.awt.*;

public final class PostLevelScreen extends AbstractScreen {

    private final LevelHandler levelHandler = LevelHandler.getInstance();
    private final IHighScoreDAO highScoreDAO = HighScoreDAO.getInstance();
    private final LogHandler logHandler = LogHandler.getInstance();
    private final IPlayerDAO playerDAO = PlayerDAO.getInstance();
    private HighScoreDTO highScoreDTO;

    private boolean isFirstRender = true;
    private boolean hasWon = false;
    private boolean isHighScore = false;

    public PostLevelScreen(GameController gameController) {
        super(gameController);
    }

    @Override
    public void update() {
        decInputTimer();
        if (inputHandler.isOKPressed() && isInputAvailable()) {
            resetInputTimer();
            isFirstRender = true;
            levelHandler.getActiveLevel().stopSound();
            levelHandler.resetActiveLevel();
            gameController.switchState(State.SELECT_LEVEL);
        }
    }

    @Override
    public void render(Graphics2D g) {
        Level activeLevel = levelHandler.getActiveLevel();

        if (isFirstRender) {
            isFirstRender = false;
            isHighScore = false;
            activeLevel.getLevelTimer().stopTimer();
            hasWon = levelHandler.getActiveLevel().isWon();

            try {
                highScoreDAO.loadHighScoreList();
            } catch (IHighScoreDAO.HighScoreDAOException e) {
                e.printStackTrace();
            }

            if (hasWon) {
                try {
                    highScoreDTO = new HighScoreDTO(playerDAO.getSelectedPlayer(), activeLevel.getName(),
                            levelHandler.getCurrentScore(),    activeLevel.getLevelTimer().getSecondsElapsed());
                    isHighScore = highScoreDAO.isHighScore(highScoreDTO);
                    if (isHighScore && !optionsHandler.isGodModeEnabled()) {
                        highScoreDAO.addHighScore(highScoreDTO);
                        highScoreDAO.saveHighScoreList();
                    }
                } catch (IPlayerDAO.PlayerDAOException | IHighScoreDAO.HighScoreDAOException e) {
                    e.printStackTrace();
                    logHandler.log(e.getMessage(), "PostLevelScreen: render", LogHandler.LogLevel.ERROR, false);
                }
            }
        }

        drawTitle(g);
        drawSubtitle(activeLevel.getName(), g);

        if (hasWon) {
            if (isHighScore) {
                drawCenteredText("»»»»»»»» NEW HIGH SCORE! ««««««««", -50, g);
                drawCenteredText("You reached a new high score of " + levelHandler.getCurrentScore() + ".", 50, g);
            } else {
                drawCenteredText("You reached a total score of " + levelHandler.getCurrentScore() + ".", 50, g);
            }
            drawCenteredText("You are victorious! " + levelHandler.getActiveLevel().getName() + " has been defeated.", 0, g);

            if (optionsHandler.isGodModeEnabled()) {
                drawCenteredText("God Mode scores are not recorded.", 150, g);
            }
        } else {
            drawCenteredText("You have lost. " + levelHandler.getActiveLevel().getName() + " shines in grace upon you.", 0, g);
            drawCenteredText("You reached a total score of " + levelHandler.getCurrentScore() + ".", 50, g);
        }
        drawCenteredText("Time: " + textHandler.getTimeString(activeLevel.getLevelTimer().getSecondsElapsed()), 100, g);


        drawInfoPanel(g);
    }

}