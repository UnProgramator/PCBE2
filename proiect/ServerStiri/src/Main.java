
import publiser.Publisher;
import services.DomainService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import client.Cititor;

public class Main {

	public static void main(String[] args) throws Exception {
		DomainService ds = new DomainService(new String[][] {
			{"IT", "Procesoare"},
			{"IT", "Floting point"},
			{"IT", "Concurency"},
			{"IT", "PAD"},
			{"Sport", "Fotbal"},
			{"Sport", "Baschet"},
			{"Biologie", "Celuloza"}
		});
		
		
		Cititor c1 = new Cititor(new String[] {"IT%Minea SA"}, ds);
		Cititor c2 = new Cititor(new String[] {"IT", "Fotbal"}, ds);
//		Cititor c3 = new Cititor(new String[] {"Biologie", "Celuloza", "Economie"}, ds);

		
		Publisher p = new Publisher("IEEE", ds);
		Publisher p1 = new Publisher("Minea SA", ds);
		Publisher p2 = new Publisher("Poli", ds);
		
		p.start();
		p1.start();
		p2.start();
		
		
		
		p.join();
		p1.join();
		p2.join();
	}

}
