public class UDPServer extends Data {

	public static void main(String args[]) throws Exception {

		while (true) {

			String teste = Data.receberDados();
			System.out.println("Nivel de dificuldade selecionado: " + teste);
			
			Data.enviarDados(Perguntas.normal[0].getQuestao());
			Data.enviarDados(Perguntas.normal[0].getOpcaoA());
			Data.enviarDados(Perguntas.normal[0].getOpcaoB());
			Data.enviarDados(Perguntas.normal[0].getOpcaoC());
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
}
