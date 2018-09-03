//package core.message;

public abstract class Message {
	public Object sender;
	public Object addressee;
	
	public Message(Object sender) {
		this.sender = sender;
	}

	public void setAdressee(Object addressee) {
		this.addressee = addressee;
	}
}
