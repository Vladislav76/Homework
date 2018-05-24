//package core.gameState;

public interface GameStateManager {

    public void setState(int state);
    public void sendMessage(Message message, int addrState);
    public void update();
    public void render();

}
