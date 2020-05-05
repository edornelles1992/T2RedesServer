import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UDPServer {

	public static void main(String args[]) throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(50000);

		byte[] receiveData = new byte[1024];
		while (true) {

			// declara o pacote a ser recebido
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

			// recebe o pacote do cliente
			serverSocket.receive(receivePacket);

			String data = new String(receivePacket.getData());
			System.out.println("dado recebido: " + data);

		}
	}
}
