package Host;

import Common.PlayerHost;

public class DummyPlayerHost extends PlayerHost {
    @Override
    protected void networkUpdate() {
        // nothing
    }

    @Override
    public void faceDeath() {
    }
}
