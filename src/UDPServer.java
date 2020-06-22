import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Classe que contem os métodos de gerenciamento da comunicação com o CLIENTE.
 */
public class UDPServer extends DataServer {

	public static String arquivoHashRecebido = null;

	/**
	 * Aplicação inicia abrindo o socket e navegando para o fluxo de recebimento de arquivo.
	 * Finaliza fechando o socket.
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("Aguardando Arquivo a ser enviado...");
		DataServer.iniciarSocket();
		gerenciarDadosRecebidos();
		DataServer.fecharSocket();
	}

	/**
	 * Método principal que gerencia os passos necessários para salvar o arquivo recebido:
	 * 1 - Recebe os pacotes para montar o arquivo.
	 * 2 - Cria o array de bytes com os pacotes recebidos
	 * 3 - Cria o has do md5sum com base no byte array recebido.
	 * 4 - Salva o arquivo no servidor e informa o Hashcode.
	 */
	private static void gerenciarDadosRecebidos() {
		ArrayList<byte[]> arqBytesArray = recebePacotesEMontaArquivo();
		byte[] arquivo = ListaParaBytes(arqBytesArray);
		arquivoHashRecebido = md5sum(Arrays.toString(arquivo));
		salvarArquivo(arquivo);
		System.out.println("md5Sum: " + arquivoHashRecebido);
		System.out.println("Envio de arquivo encerrado!");
	}

	/**
	 * Salva arquivo no servidor.
	 */
	private static void salvarArquivo(byte[] arqBytes) {
		try {
			Path path = Paths.get("arquivo" + arqBytes.length);
			Files.write(path, arqBytes);
			System.out.println("Arquivo Recebido e salvo com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar arquivo!");
		}
	}

	/**
	 * Recebe os pacotes enviados pelo cliente e guarda os dados do pacote em uma
	 * lista de array de bytes. O método fica em looping até receber o ultimo pacote 
	 * do cliente. Por fim ordena os pacotes, remove possiveis duplicados e remonta
	 * e junta as partes do arquivo em um array de bytes.
	 */
	private static ArrayList<byte[]> recebePacotesEMontaArquivo() {
		ArrayList<byte[]> arqBytesList = new ArrayList<>();
		List<Pacote> pacotes = new ArrayList<>();
		Pacote pacote;
		do {
			pacote = DataServer.receberDados();
			pacotes.add(pacote);
			enviarAck(pacote);
		} while (pacote.ultimo != 1);

		pacotes.sort(Comparator.comparingDouble(Pacote::getSeq));
		removeDuplicados(pacotes);
		// monta o array de array de bytes do arquivo
		for (Pacote p : pacotes)
			arqBytesList.add(p.dados);

		return arqBytesList;
	}

	/**
	 * Remove possiveis pacotes que possam vir duplicados do cliente durante a
	 * comunicação cliente/servidor.
	 */
	public static void removeDuplicados(List<Pacote> pacotes) {
		for (int i = 0; i < pacotes.size(); i++) {
			int cont = 0;
			for (Pacote pacote : pacotes) {
				if (pacote.getSeq() == i) {
					cont++;
				}
			}
			if (cont > 1) {
				pacotes.remove(i);
				i--;
			}
		}
		System.out.println("removido pacotes duplicados...");
	}

	/**
	 * Método que envia o ack de confirmação de recebimento de pacote
	 * para o cliente.
	 */
	private static void enviarAck(Pacote pacote) {
		System.out.println("SEQ: " + pacote.seq);
		Pacote pacoteResposta = new Pacote();
		pacoteResposta.ack = pacote.seq + 1;
		DataServer.enviarDados(pacoteResposta);
	}

	/**
	 * Converte a lista de array de bytes para um array de bytes único para
	 * montar o arquivo corretamente..
	 */
	private static byte[] ListaParaBytes(ArrayList<byte[]> arqBytesArray) {
		byte[] arqBytes = new byte[calculaTamanhoArquivo(arqBytesArray)];
		int pos = 0;
		for (byte[] parteBytes : arqBytesArray) {
			pos = gravaNoArqBytes(arqBytes, parteBytes, pos);
		}
		return arqBytes;
	}

	/**
	 * grava no byte array que esta sendo montado e retorna a posição final para os
	 * próximos arrays de bytes serem concatenados.
	 */
	private static int gravaNoArqBytes(byte[] arqBytes, byte[] parteBytes, int pos) {
		int novaPos = pos;
		for (int i = 0; i < parteBytes.length; i++) {
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
		for (byte[] arqParte : arqBytesArray) {
			cont += arqParte.length;
		}
		return cont;
	}
}
