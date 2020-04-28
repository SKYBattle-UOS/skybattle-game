package com.example.myapplication;

import android.util.Log;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 집합화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateAssemble implements GameState {
    GameStateMatch parentMatch;

    public MatchStateAssemble(GameStateMatch parentMatch) {
        this.parentMatch = parentMatch;
    }

    @Override
    public void update(int ms) {
        boolean assembled = false;

        // TODO: 플레이어 오브젝트 따로 구분해야 함
        GameObject[] players = parentMatch.getGameObjects();

        // 집합 지점 찾기
        // TODO: 게임 반경 이내에서 계산할 것
        double assembleLat = 0;
        double assembleLon = 0;
        final double range = 30; // in meters

        // 집합하기
        while (!assembled) {
            // 체크
            for (int i = 0; i < players.length; i++) {
                double lat = players[i].getPosition()[0];
                double lon = players[i].getPosition()[1];

                double distance = getDistance(assembleLat, assembleLon, lat, lon);
            }


            assembled = true;
        }
        // TODO: DEBUG EDIT
        Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
    }

    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow( (x1 - x2), 2 ) + Math.pow( (y1 - y2), 2) );
    }
}