package com.kickrank.common.enums;

public enum MatchFormat {
    FIVE_V_FIVE(5),
    SEVEN_V_SEVEN(7),
    ELEVEN_V_ELEVEN(11);

    private final int playersPerTeam;

    MatchFormat(int playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public int playersPerTeam() {
        return playersPerTeam;
    }

    public int capacity() {
        return playersPerTeam * 2;
    }
}
