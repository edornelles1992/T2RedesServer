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
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Classe contendo os m�todos de manipula��o do socket dos datagrams no lado do
 * SERVIDOR
 */
public class DataServer implements Parametros {

	private static DatagramSocket serverSocket;
	private static InetAddress enderecoCliente;
	private static Integer portaCliente = -1;
	protected final static Integer porta1 = 50000;
	private static Integer timeout = 1500;

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
	 * Recebe os dados a ser enviados e envia pelo socket j� pr� configurado. Caso
	 * ocorra algum erro no envio a exce��o � capturada e fica tenta enviar
	 * novamente at� obter sucesso.
	 * 
	 * @param dados
	 */
	protected static void enviarDados(Pacote pacote) {
		try {
			byte[] serialized = pacoteToByteArray(pacote);
			DatagramPacket sendPacket = new DatagramPacket(serialized, serialized.length, enderecoCliente, portaCliente);
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("Houve um problema na comunica��o com o servidor...");
			System.out.println("Tentando restabelecer a conex�o...");
			enviarDados(pacote);
		}
	}

	/**
	 * Recebe um pacote datagrama e converte ele para o objeto Pacote
	 */
	protected static Pacote receberDados() {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveDatagram);
			byte[] recBytes = receiveDatagram.getData();
			Pacote pacote = byteArrayToPacote(recBytes);
			if (!isValidoCRC(pacote)) {
				System.out.println("Pacote com CRC Inválido, descartado!");
				throw new IOException("CRC Inválido!");
			} 
			getClientInfos(receiveDatagram);
			return pacote;
		} catch (IOException e) {
			return receberDados();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converte o objeto pacote para um byteArray
	 */
	private static byte[] pacoteToByteArray(Pacote pacote) {
		try {
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream);
		oo.writeObject(pacote);
		oo.close();
		return bStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converte de byteArray para o objeto pacote.
	 */
	private static Pacote byteArrayToPacote(byte[] pacote) {
		try {
			ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(pacote));
			Pacote pacoteObj = (Pacote) iStream.readObject();
			iStream.close();
			return pacoteObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Recalcula o CRC e valida se bate com o CRC do client.
	 */
	private static boolean isValidoCRC(Pacote pacote) {
		long crc_recebido = pacote.valor_crc;
		return crc_recebido == calcularCRC32DoPacote(pacote);
	}
	
	protected static long calcularCRC32DoPacote(Pacote pacote) {
		try {
		Checksum checksum = new CRC32();
		// update the current checksum with the specified array of bytes
		checksum.update(pacote.dados, 0, pacote.dados.length);		 
		// get the current checksum value
		long checksumValue = checksum.getValue();
		return checksumValue;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
 	}

	/**
	 * Captura IP e Porta do cliente que enviou o pacote para utilizar na resposta.
	 * Caso j� tenha um cliente conectado nesse socket, valida se � ele que esta
	 * enviando os dados ou algum outro cliente. Caso seja outro retorna o erro de
	 * slot ocupado para o cliente e poem o socket novamente para aguardar os dados
	 * do cliente correto.
	 * 
	 * @param receiveDatagram
	 */
	private static void getClientInfos(DatagramPacket receiveDatagram) throws IOException {

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
