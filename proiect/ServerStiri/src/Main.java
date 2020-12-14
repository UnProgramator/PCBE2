
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
		
		
		/* daca nu merge avem un output valid
		 * 
		 * 
		 * 
		 * published to IEEE.IT.PAD msg: PAD|IEEE|2020-51-14 09:51:00|Stire - PAD0 from IEEE|Body - PAD0 from IEEE
published to IEEE.Sport.Baschet msg: Baschet|IEEE|2020-51-14 09:51:00|Stire - Baschet1 from IEEE|Body - Baschet1 from IEEE
published to IEEE.IT.Concurency msg: Concurency|IEEE|2020-51-14 09:51:00|Stire - Concurency2 from IEEE|Body - Concurency2 from IEEE
published to Minea SA.Biologie.Celuloza msg: Celuloza|Minea SA|2020-51-14 09:51:00|Stire - Celuloza0 from Minea SA|Body - Celuloza0 from Minea SA
published to Poli.Sport.Baschet msg: Baschet|Poli|2020-51-14 09:51:00|Stire - Baschet0 from Poli|Body - Baschet0 from Poli
published to Minea SA.Sport.Baschet msg: Baschet|Minea SA|2020-51-14 09:51:00|Stire - Baschet1 from Minea SA|Body - Baschet1 from Minea SA
published to IEEE.Sport.Fotbal msg: Fotbal|IEEE|2020-51-14 09:51:00|Stire - Fotbal3 from IEEE|Body - Fotbal3 from IEEE
published to Minea SA.Biologie.Celuloza msg: Celuloza|Minea SA|2020-51-14 09:51:00|Stire - Celuloza2 from Minea SA|Body - Celuloza2 from Minea SA
published to IEEE.Biologie.Celuloza msg: Celuloza|IEEE|2020-51-14 09:51:00|Stire - Celuloza4 from IEEE|Body - Celuloza4 from IEEE
published to Poli.Biologie.Celuloza msg: Celuloza|Poli|2020-51-14 09:51:00|Stire - Celuloza1 from Poli|Body - Celuloza1 from Poli
published to Minea SA.IT.Concurency msg: Concurency|Minea SA|2020-51-14 09:51:00|Stire - Concurency3 from Minea SA|Body - Concurency3 from Minea SA
published to Minea SA.IT.PAD msg: PAD|Minea SA|2020-51-14 09:51:00|Stire - PAD4 from Minea SA|Body - PAD4 from Minea SA
published to Poli.Sport.Baschet msg: Baschet|Poli|2020-51-14 09:51:00|Stire - Baschet2 from Poli|Body - Baschet2 from Poli
 [x] Cititorul 2 Received 'IEEE.IT.PAD':'PAD|IEEE|2020-51-14 09:51:00|Stire - PAD0 from IEEE|Body - PAD0 from IEEE'
published to Poli.IT.PAD msg: PAD|Poli|2020-51-14 09:51:00|Stire - PAD3 from Poli|Body - PAD3 from Poli
published to Poli.Sport.Fotbal msg: Fotbal|Poli|2020-51-14 09:51:00|Stire - Fotbal4 from Poli|Body - Fotbal4 from Poli
 [x] Cititorul 2 Received 'IEEE.IT.Concurency':'Concurency|IEEE|2020-51-14 09:51:00|Stire - Concurency2 from IEEE|Body - Concurency2 from IEEE'
 [x] Cititorul 2 Received 'IEEE.Sport.Fotbal':'Fotbal|IEEE|2020-51-14 09:51:00|Stire - Fotbal3 from IEEE|Body - Fotbal3 from IEEE'
 [x] Cititorul 1 Received 'Minea SA.IT.Concurency':'Concurency|Minea SA|2020-51-14 09:51:00|Stire - Concurency3 from Minea SA|Body - Concurency3 from Minea SA'
 [x] Cititorul 2 Received 'Minea SA.IT.Concurency':'Concurency|Minea SA|2020-51-14 09:51:00|Stire - Concurency3 from Minea SA|Body - Concurency3 from Minea SA'
 [x] Cititorul 2 Received 'Minea SA.IT.PAD':'PAD|Minea SA|2020-51-14 09:51:00|Stire - PAD4 from Minea SA|Body - PAD4 from Minea SA'
 [x] Cititorul 2 Received 'Poli.IT.PAD':'PAD|Poli|2020-51-14 09:51:00|Stire - PAD3 from Poli|Body - PAD3 from Poli'
 [x] Cititorul 2 Received 'Poli.Sport.Fotbal':'Fotbal|Poli|2020-51-14 09:51:00|Stire - Fotbal4 from Poli|Body - Fotbal4 from Poli'
IEEE{PAD=1}
IEEE{PAD=1, Concurency=1}
IEEE{PAD=1, Concurency=1, Fotbal=1}
Poli{PAD=1}
Poli{PAD=1, Fotbal=1}
Minea SA{Concurency=1}
Minea SA{PAD=1, Concurency=1}
 [x] Cititorul 1 Received 'Minea SA.IT.PAD':'PAD|Minea SA|2020-51-14 09:51:00|Stire - PAD4 from Minea SA|Body - PAD4 from Minea SA'
Minea SA{PAD=1, Concurency=2}
Minea SA{PAD=2, Concurency=2}
		 * 
		 * 
		 * avem si printscreen
		 */
	}

}
