package progRsxDs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;




public class Client {

	//client
	private DatagramSocket sc;
	private InetSocketAddress sia;

	//client mode serveur
	private Socket client;
	private ServerSocket ss;
	private BufferedReader in;
	private int idClient;
	private int portClient;

	Client() throws IOException {
		sc = new DatagramSocket();
		idClient = new Random().nextInt();

		sia = new InetSocketAddress("127.0.0.1",9876);

		System.setProperty("line separator","\r\n");
		//a terminer
		ss = new ServerSocket(0);
	}

	void go() throws IOException {

		this.portClient = ss.getLocalPort();
	
		//mise en buffer
		
		byte [] monBuffer = new byte[7];
		
		//toujours 0
		monBuffer[0] = 0;
		
		//numéro de port
		monBuffer[1] = (byte) (portClient >>> 8);
		monBuffer[2] = (byte) (portClient);
		
		//idClient
		monBuffer[3] = (byte) (idClient >>> 24);
		monBuffer[4] = (byte) (idClient >>> 16);
		monBuffer[5] = (byte) (idClient >>> 8);
		monBuffer[6] = (byte) idClient;
		
		DatagramPacket out = new DatagramPacket(monBuffer,monBuffer.length ,sia);
		sc.send(out);


		//recoit en tcp


		client = ss.accept();

		in = new BufferedReader(new InputStreamReader(client.getInputStream()));

		String line = in.readLine();

		while(line != null) {
			System.out.println(line);
			line = in.readLine();
		}

		// fermeture des flux
		ss.close();
		sc.close();	
	}


	public static void main(String[] args) throws IOException {

		new Client().go();
	}
}
