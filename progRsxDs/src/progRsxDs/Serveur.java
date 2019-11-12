package progRsxDs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Serveur {

	private DatagramSocket dgSocket;
	private BufferedReader in;
	private PrintWriter out;
	private List<String> mesCitations;
	public static int indice = 0;
	
	//clientTcp
	private Socket sc;


	
	
	public Serveur(int pSrv) throws IOException {
		dgSocket = new DatagramSocket(pSrv);
		this.mesCitations = this.lectureFichier();
		
		System.setProperty("line separator","\r\n");
	}

	void go() throws IOException {
		

		while (true) {
			
			DatagramPacket dgPacket = new DatagramPacket(new byte[7], 7);
			String str;
			
			dgSocket.receive(dgPacket);
			
			//On recupere le port et l'idClient
			int portClient = this.getPort(dgPacket.getData());
			int idClient = this.getId(dgPacket.getData());
			
			str = this.mesCitations.get(indice);
			if(indice == this.mesCitations.size())
				indice = 0;
			else
				indice++;
			
			sc = new Socket("localhost",portClient);
			out = new PrintWriter(sc.getOutputStream(),true);
			out.println(str);		
			
		}
	}
	
	// récupération de 4 octet pour en faire un int
	private int  getId(byte[] data) {
	    return (data[3] << 24) + ((data[4] & 0xff) << 16) + ((data[5] & 0xff) << 8) + (data[6] & 0xff);
	}
	
	private int getPort(byte[] data) {
		return ((data[1] & 0xff) << 8) + (data[2] & 0xff);
	}

	
	private List<String> lectureFichier() {

		List<String> res = new ArrayList<>();
		String ligneValide = "";
		String ligne = "";
		try {

			in = new BufferedReader(new FileReader("celebre"));
			
			ligne = in.readLine();

			while(ligne != null) {

				if(!ligne.contains("%")) 
					ligneValide += ligne+"\n";
				else {
					res.add(ligneValide);
					ligneValide = "";
				}			
				
				ligne = in.readLine();
			}
			
			in.close();

		}catch(FileNotFoundException fnfe) {
			System.out.println(fnfe);
		}catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}

		return res;
	}



	public static void main(String[] args) throws IOException {
		final int DEFAULT_PORT = 9876;
		Serveur monServeur = new Serveur( args.length == 0 ? DEFAULT_PORT : Integer.parseInt(args[0]) );
		monServeur.go();
	
	}
}
