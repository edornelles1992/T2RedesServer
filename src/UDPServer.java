import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UDPServer extends Data {
	
	public static void main(String args[]) throws Exception {
		while (true) {
			Data.iniciarSocket();
			Pacote pacote = Data.receberDados();
			Data.fecharSocket();
			try {
				Path path = Paths.get("arquivoRecebidoUDP" + pacote.dados.length + ".txt");
				Files.write(path, pacote.dados);
				System.out.println("Arquivo Recebido e salvo com sucesso!");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Erro ao salvar arquivo!");
			}

		}
	}
}
