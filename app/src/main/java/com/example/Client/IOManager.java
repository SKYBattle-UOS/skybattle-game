package com.example.Client;

enum CharacterType {
    GOOD,
    BAD,
    UGLY
}

public class IOManager {
    private PacketManager _packetManager;

    // TODO
    private long _elapsed;

    public IOManager(PacketManager packetManager) {
        _packetManager = packetManager;
    }

    public void update(long ms){
        // TODO
        _elapsed += ms;
    }

    public void selectCharacter(CharacterType type){
        // TODO
    }

    public void confirmPlayerInit(){
        // TODO
    }

    public void confirmCharacterInit(){
        // TODO
    }

    public boolean didHostPressStart(){
        return _elapsed > 5000;
    }

    public boolean isEverybodyInitializedForAssemble(){
        return _elapsed > 10000;
    }

    public boolean isAssembleComplete() {
        return _elapsed > 15000;
    }

    public boolean isCharacterSelectComplete(){
        return _elapsed > 20000;
    }
}
