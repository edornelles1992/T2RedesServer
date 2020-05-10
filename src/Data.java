import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Classe contendo os métodos de manipulação do socket dos
 * datagrams no lado do SERVIDOR
 */
public abstract class Data {

	private static DatagramSocket serverSocket;
	private static InetAddress enderecoCliente;
	private final static Integer portaServidor = 50000;
	private static Integer portaCliente = -1;
	private static Integer timeout = 1500;

	/**
	 * Inicia o socket ao subir o servidor setando um valor
	 * fixo de timeout
	 */
	static {
		try {
			serverSocket = new DatagramSocket(portaServidor);
			serverSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			System.out.println("Erro ao iniciar socket servidor!");
			e.printStackTrace();
		}
	}

	/**
	 * Recebe os dados a ser enviados e envia pelo socket já
	 * pré configurado. Caso ocorra algum erro no envio a exceção
	 * é capturada e fica tenta enviar novamente até obter sucesso.
	 * @param dados
	 */
	protected static void enviarDados(String dados) {
		try {
			DatagramPacket sendPacket = new DatagramPacket(dados.getBytes(), dados.getBytes().length, enderecoCliente,
					portaCliente);
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Houve um problema na comunicação com o cliente...");
			System.out.println("Tentando enviar os dados novamente...");
			enviarDados(dados);
		}
	}

	/**
	 * Aguarda o cliente retornar os dados solicitados e retorna
	 * os dados em caso de sucesso. Caso o tempo de espera dos dados demore
	 * mais que o timeout configurado ele avisa em tela para o usuário que está
	 * com problema para receber os dados e fica em loop até conseguir receber os dados.
	 * @return dado recebido
	 */
	protected static String receberDados() {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveDatagram);
			getClientInfos(receiveDatagram);
			return new String(receiveDatagram.getData()).trim();
		} catch (IOException e) {
			System.out.println("Houve um problema na comunicação com o cliente...");
			System.out.println("Tentando restabelecer comunicação...");
			return receberDados();
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
}
