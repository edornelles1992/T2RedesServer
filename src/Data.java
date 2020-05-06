import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class Data {

	private static DatagramSocket serverSocket;
	private static InetAddress enderecoCliente;
	private final static Integer portaServidor = 50000;
	private static Integer portaCliente = -1;

	static {
		try {
			serverSocket = new DatagramSocket(portaServidor);
		} catch (SocketException e) {
			System.out.println("Erro ao iniciar socket servidor!");
			e.printStackTrace();
		}
	}

	protected static void enviarDados(String dados) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(dados.getBytes(), dados.length(), enderecoCliente,
					portaCliente);
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			// TODO: Tratativa para erro de envio/conexão
			e.printStackTrace();
		}
	}

	protected static String receberDados() {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveDatagram);
			getClientInfos(receiveDatagram);
			return new String(receiveDatagram.getData());
		} catch (IOException e) {
			// TODO: Tratativa para erro de envio/conexão
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Captura IP e Porta do cliente que enviou o pacote para utilizar na resposta.
	 * 
	 * @param receiveDatagram
	 */
	private static void getClientInfos(DatagramPacket receiveDatagram) {
		enderecoCliente = receiveDatagram.getAddress();
		portaCliente = receiveDatagram.getPort();
	}

	public static InetAddress getEnderecoCliente() {
		return enderecoCliente;
	}

	public static void setEnderecoCliente(InetAddress enderecoCliente) {
		Data.enderecoCliente = enderecoCliente;
	}

	public static Integer getPortaCliente() {
		return portaCliente;
	}

	public static void setPortaCliente(Integer portaCliente) {
		Data.portaCliente = portaCliente;
	}

}
