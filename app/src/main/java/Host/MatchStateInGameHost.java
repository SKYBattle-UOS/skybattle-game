package Host;

import Common.GameState;
import Common.Player;
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
            p.getProperty().setInvincibility(false);
            p.getProperty().setCantAttack(false);
            _match.getWorldSetterHost().generateUpdateInstruction(
                    p.getGameObject().getNetworkId(),
                    PlayerProperty.invincibilityFlag | PlayerProperty.cantAttackFlag
            );
        }
    }
}
