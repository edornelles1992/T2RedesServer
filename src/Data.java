import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Classe contendo os métodos de manipulação do socket dos datagrams no lado do
 * SERVIDOR
 */
public class Data {

	private static DatagramSocket serverSocket;
	private static InetAddress enderecoCliente;
	private static Integer portaCliente = -1;
	protected final static Integer porta1 = 50000;
	private static Integer timeout = 1500;
	public static Integer dataSize = 512;

	/**
	 * Inicia o socket ao subir o servidor setando um valor fixo de timeout
	 */
	protected static void iniciarSocket() {
		try {
			serverSocket = new DatagramSocket(porta1);
			serverSocket.setSoTimeout(timeout);
		} catch (SocketException e) {
			System.out.println("Erro ao iniciar socket servidor!");
			e.printStackTrace();
		}
	}

	/**
	 * Fechar o socket e limpar endereco do cliente e porta do cliente que estava
	 * conectado.
	 */
	protected static void fecharSocket() {
		serverSocket.close();
		serverSocket = null;
		enderecoCliente = null;
		portaCliente = null;
	}

	/**
	 * Recebe os dados a ser enviados e envia pelo socket já pré configurado. Caso
	 * ocorra algum erro no envio a exceção é capturada e fica tenta enviar
	 * novamente até obter sucesso.
	 * 
	 * @param dados
	 */
	protected static void enviarDados(Pacote pacote) {
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(pacote);
			oo.close();
			byte[] serialized = bStream.toByteArray();
			DatagramPacket sendPacket = new DatagramPacket(serialized, serialized.length);
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Houve um problema na comunicação com o servidor...");
			System.out.println("Tentando restabelecer a conexão...");
			enviarDados(pacote);
		}
	}

	protected static Pacote receberDados() {
		try {
			byte[] receiveData = new byte[dataSize];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveDatagram);
			byte[] recBytes = receiveDatagram.getData();
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(recBytes));
			Pacote pacote = (Pacote) iStream.readObject();
			iStream.close();
			return pacote;
		} catch (IOException e) {
			return receberDados();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Captura IP e Porta do cliente que enviou o pacote para utilizar na resposta.
	 * Caso já tenha um cliente conectado nesse socket, valida se é ele que esta
	 * enviando os dados ou algum outro cliente. Caso seja outro retorna o erro de
	 * slot ocupado para o cliente e poem o socket novamente para aguardar os dados
	 * do cliente correto.
	 * 
	 * @param receiveDatagram
	 */
	private void getClientInfos(DatagramPacket receiveDatagram) throws IOException {
		if (enderecoCliente == null) {
			enderecoCliente = receiveDatagram.getAddress();
			portaCliente = receiveDatagram.getPort();
		} else {
			if (!enderecoCliente.equals(receiveDatagram.getAddress())
					|| !portaCliente.equals(receiveDatagram.getPort())) {
				String error = "ERRO: Slot Ocupado";
				DatagramPacket sendPacket = new DatagramPacket(error.getBytes(), error.getBytes().length,
						receiveDatagram.getAddress(), receiveDatagram.getPort());
				serverSocket.send(sendPacket);
				throw new IOException("Outro cliente tentou conectar nesse slot");
			}
		}
	}
}
