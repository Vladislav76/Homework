package Message;

public class GameSettingsMessage extends Message {
	public int width;
	public int height;
	public int mode;
	public int[] countOfShips;
	
	public GameSettingsMessage(Object sender, int width, int height, int mode, int[] countOfShips) {
		super(sender);
		this.width = width;
		this.height = height;
		this.mode = mode;
		this.countOfShips = countOfShips;
	}
}