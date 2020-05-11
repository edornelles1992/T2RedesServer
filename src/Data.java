import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Classe contendo os métodos de manipulação do socket dos datagrams no lado do
 * SERVIDOR
 */
public class Data {

	private DatagramSocket serverSocket;
	private InetAddress enderecoCliente;
	private Integer portaCliente = -1;
	private Integer timeout = 1500;

	/**
	 * Inicia o socket ao subir o servidor setando um valor fixo de timeout
	 */
	protected void iniciarSocket(Integer portaServidor) {
		try {
			serverSocket = new DatagramSocket(portaServidor);
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
	protected void fecharSocket() {
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
	protected void enviarDados(String dados) {
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
	 * Aguarda o cliente retornar os dados solicitados e retorna os dados em caso de
	 * sucesso. Caso o tempo de espera dos dados demore mais que o timeout
	 * configurado ele fica em loop até conseguir receber os dados.
	 * 
	 * @return dado recebido
	 */
	protected String receberDados() {
		try {
			byte[] receiveData = new byte[1024];
			DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveDatagram);
			getClientInfos(receiveDatagram);
			return new String(receiveDatagram.getData()).trim();
		} catch (IOException e) {
			return receberDados();
		}
	}

	/**
	 * Captura IP e Porta do cliente que enviou o pacote para utilizar na resposta.
	 * Caso já tenha um cliente conectado nesse socket, valida se é ele que esta enviando
	 * os dados ou algum outro cliente. Caso seja outro retorna o erro de slot ocupado para o cliente e 
	 * poem o socket novamente para aguardar os dados do cliente correto.
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
