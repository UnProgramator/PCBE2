
import client.ClientStub;
import publiser.Publisher;
import services.DomainService;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		DomainService domS = new DomainService(
				new String[][] {
						{"IT", "Procesoare"},
						{"IT", "AI"},
						{"Anatomie", "CoVid-19"},
						{"Economie","Inflatie"},
						{"Sport","Fotbal"},
						{"Sport","Basket"},
						{"Sport","Handbal"}
				}
			);
		
		
		Publisher s = new Publisher("IEEE", domS);
		s.start();
		ClientStub c = new ClientStub();
		c.start();
		s.join();
		c.join();
	}

}
