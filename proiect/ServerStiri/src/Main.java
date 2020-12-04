
import client.ClientStub;
import publiser.PublisherStub;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		PublisherStub s = new PublisherStub();
		s.start();
		ClientStub c = new ClientStub();
		c.start();
		s.join();
		c.join();
	}

}
