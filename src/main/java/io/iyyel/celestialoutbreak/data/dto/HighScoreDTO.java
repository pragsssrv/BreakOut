package io.iyyel.celestialoutbreak.data.dto;

import java.io.Serializable;

public final class HighScoreDTO implements Serializable {

    private final String player;
    private final String levelName;
    private final long score;
    private final long time;

    public HighScoreDTO(String player, String levelName, long score, long time) {
        this.player = player;
        this.levelName = levelName;
        this.score = score;
        this.time = time;
    }

    public String getPlayer() {
        return player;
    }

    public String getLevelName() {
        return levelName;
    }

    public long getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }

}