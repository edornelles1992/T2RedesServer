import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UDPServer extends DataServer {
	
	public static void main(String args[]) throws Exception {
		while (true) {
			DataServer.iniciarSocket();
			gerenciarDadosRecebidos();
			DataServer.fecharSocket();		
		}
	}

	private static void gerenciarDadosRecebidos() {

		ArrayList<byte[]> arqBytesArray = dadosParaLista();
		byte[] arqBytes = ListaParaBytes(arqBytesArray);
		salvarArquivo(arqBytes);
	}

	/**
	 * Salva arquivo no servidor
	 */
	private static void salvarArquivo(byte[] arqBytes) {
		try {
			Path path = Paths.get("arquivoRecebidoUDP" + arqBytes.length + ".txt");
			Files.write(path, arqBytes);
			System.out.println("Arquivo Recebido e salvo com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar arquivo!");
		}
	}

	/**
	 * Recebe os pacotes enviados pelo cliente e guarda os dados do pacote
	 * em uma lista de array de bytes.
	 */
	private static ArrayList<byte[]> dadosParaLista() {
		ArrayList<byte[]> arqBytesList = new ArrayList<>();
		Pacote pacote;
		while (true) {
			pacote = DataServer.receberDados();
			if (pacote.ultimo == ultimo_pacote) {
				arqBytesList.add(pacote.dados);
				enviarAck(pacote);
				break;
			} else {
				arqBytesList.add(pacote.dados);
				enviarAck(pacote);
			}
		}
		return arqBytesList;
	}

	private static void enviarAck(Pacote pacote) {
		System.out.println("SEQ: " + pacote.seq);
		Pacote pacoteResposta = new Pacote();
		pacoteResposta.ack = pacote.seq + 1;
		DataServer.enviarDados(pacoteResposta);
	}
	
	private static byte[] ListaParaBytes(ArrayList<byte[]> arqBytesArray) {
		byte[] arqBytes = new byte[calculaTamanhoArquivo(arqBytesArray)];
		int pos = 0;
		for (byte[] parteBytes : arqBytesArray) {
			pos = gravaNoArqBytes(arqBytes, parteBytes, pos);
		}
		return arqBytes;
	}

	/**
	 * grava no byte array que esta sendo montado e retorna a posição final para
	 * os próximos arrays de bytes serem concatenados.
	 */
	private static int gravaNoArqBytes(byte[] arqBytes, byte[] parteBytes, int pos) {
		int novaPos = pos;
		for (int i = 0 ; i < parteBytes.length ; i++) {
			arqBytes[novaPos] = parteBytes[i];
			novaPos++;
		}
		return novaPos;
	}

	/**
	 * Soma o tamanho de todas as partes do arquivo
	 */
	private static int calculaTamanhoArquivo(ArrayList<byte[]> arqBytesArray) {
		int cont = 0;
		for (byte[] arqParte: arqBytesArray) {
			cont+= arqParte.length;
		}
		return cont;
	}
}
