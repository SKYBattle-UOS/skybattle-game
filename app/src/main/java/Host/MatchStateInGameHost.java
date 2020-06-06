package Host;

import com.example.Client.PlayerState;

import Common.GameState;
import Common.Player;
import Common.PlayerHost;
import Common.PlayerProperty;
import Common.ReadOnlyList;

class MatchStateInGameHost implements GameState {
    private GameStateMatchHost _match;

    public MatchStateInGameHost(GameStateMatchHost gameStateMatchHost) {
        _match = gameStateMatchHost;
    }

    @Override
    public void start() {
        letTheHungerGamesBegin();
    }

    @Override
    public void update(long ms) {
        // TODO
    }

    private void letTheHungerGamesBegin(){
        ReadOnlyList<Player> players = _match.getPlayers();
        for (Player p : players){
            ((PlayerHost) p).setDamageApplier(new TrueDamageApplier());
            ((PlayerHost) p).setDamageCalculator(new SimpleDamageCalculator());
            p.getProperty().setPlayerState(PlayerState.NORMAL);
            _match.getWorldSetterHost().generateUpdateInstruction(
                    p.getGameObject().getNetworkId(), PlayerProperty.playerStateFlag
            );
        }
    }
}
