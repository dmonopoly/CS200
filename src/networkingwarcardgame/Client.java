package networkingwarcardgame;

public interface Client {
	void connect();
	void sendToServer(String msg);
	void listenToServer();
	void processMessage(String msg);
}
