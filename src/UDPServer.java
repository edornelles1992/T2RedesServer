public class UDPServer extends Data implements Parametros {

	public static void main(String args[]) throws Exception {

		while (true) {

			String dificuldade = Data.receberDados();
//			System.out.println("Nivel de dificuldade selecionado: " + dificuldade);
			enviarPerguntas(dificuldade);
//			// declara o pacote a ser recebido
//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//
//			// recebe o pacote do cliente
//			serverSocket.receive(receivePacket);
//
//			String data = new String(receivePacket.getData());
//			System.out.println("dado recebido: " + data);
		}
	}
	
	private static void enviarPerguntas(String dificuldade) {
		Pergunta[] perguntas = dificuldade.equals(NORMAL) ? Perguntas.getNormal() : Perguntas.getDificil();
		for (int i = 0; i < 3; i++) {
			Data.enviarDados(perguntas[i].getQuestao() + DELIMITADOR + perguntas[i].getOpcaoA() + DELIMITADOR + perguntas[i].getOpcaoB() + DELIMITADOR + perguntas[i].getOpcaoC());
			String resposta = Data.receberDados(); //recebe resposta
			if (perguntas[i].getOpcaoCorreta().equalsIgnoreCase(resposta)) {
				Data.enviarDados("Certa Resposta!");
			} else {
				Data.enviarDados("Resposta Errada");
			}
		}
	}
}
