import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UDPServer extends Data {
	
	public static void main(String args[]) throws Exception {
		while (true) {
			Data.iniciarSocket();
			gerenciarDadosRecebidos();
			Data.fecharSocket();		
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
			pacote = Data.receberDados();
			if (pacote.ultimo == ultimo_pacote) {
				arqBytesList.add(pacote.dados);
				break;
			} else {
				arqBytesList.add(pacote.dados);
			}
		}
		return arqBytesList;
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
