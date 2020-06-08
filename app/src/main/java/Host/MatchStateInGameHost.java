package Host;

import com.example.Client.PlayerClient;
import com.example.Client.PlayerState;

import java.util.Collection;

import Common.CollisionState;
import Common.Damageable;
import Common.GameObject;
import Common.GameOverState;
import Common.GameState;
import Common.MatchStateType;
import Common.OutputBitStream;
import Common.Player;
import Common.PlayerHost;
import Common.ReadOnlyList;
import Common.Util;

class MatchStateInGameHost implements GameState {
    private GameStateMatchHost _match;
    private int _remainingTimeMS = 180000;
    private int _prevRemainingSeconds = _remainingTimeMS / 1000;

    public MatchStateInGameHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
    }

    @Override
    public void start() {
        letTheHungerGamesBegin();
        BattleFieldHost battleField = (BattleFieldHost) Util.findGOByName(_match, "Korimart전장");
        battleField.setDPS(20000);
    }

    @Override
    public void update(long ms) {
        _remainingTimeMS -= ms;
        sendRemainingTime();
        sendGameOver();
    }

    private void sendGameOver() {
        GameOverState state = GameOverState.ZOMBIESWIN;
        if (_remainingTimeMS < 0)
            state = GameOverState.HUMANSWIN;
        else {
            ReadOnlyList<Player> players = _match.getPlayers();
            for (Player p : players){
                if (p.getProperty().getPlayerState() == PlayerState.NORMAL){
                    state = GameOverState.GOING;
                    break;
                }
            }
        }

        OutputBitStream stream = CoreHost.get().getNetworkManager().getPacketToSend();
        boolean gameOver = state == GameOverState.HUMANSWIN || state == GameOverState.ZOMBIESWIN;
        Util.sendHas(stream, gameOver);
        if (gameOver){
            stream.write(state.ordinal(), 2);
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
            _match.switchState(MatchStateType.GAMEOVER);
        }
    }

    private void sendRemainingTime() {
        boolean shouldSend = _prevRemainingSeconds > _remainingTimeMS / 1000;
        OutputBitStream stream = CoreHost.get().getNetworkManager().getPacketToSend();
        Util.sendHas(stream, shouldSend);
        if (shouldSend){
            _prevRemainingSeconds = _remainingTimeMS / 1000;
            stream.write(_prevRemainingSeconds, 10);
            CoreHost.get().getNetworkManager().shouldSendThisFrame();
        }
    }

    private void letTheHungerGamesBegin(){
        ReadOnlyList<Player> players = _match.getPlayers();
        for (Player p : players){
            ((PlayerHost) p).setDamageApplier(new TrueDamageApplier());
            ((PlayerHost) p).setDamageCalculator(new SimpleDamageCalculator());
        }
    }
}
