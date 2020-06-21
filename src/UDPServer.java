import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class UDPServer extends DataServer {

	public static void main(String args[]) throws Exception {


			System.out.println("Aguardando Arquivo a ser enviado...");
			DataServer.iniciarSocket();
			gerenciarDadosRecebidos();
			DataServer.fecharSocket();

//			System.out.println("Deseja enviar mais arquivos? 1 - Sim / 2 - Não");
//			Scanner scanner = new Scanner(System.in);
//			int op = scanner.nextInt();
//			if (op == 2) {
			System.out.println("Envio de arquivo encerrado!");
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
			Path path = Paths.get("arquivo" + arqBytes.length + ".txt");
			Files.write(path, arqBytes);
			System.out.println("Arquivo Recebido e salvo com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao salvar arquivo!");
		}
	}

	/**
	 * Recebe os pacotes enviados pelo cliente e guarda os dados do pacote em uma
	 * lista de array de bytes.
	 */
	private static ArrayList<byte[]> dadosParaLista() {
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
