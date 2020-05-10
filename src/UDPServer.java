public class UDPServer extends Data implements Parametros {

	public static void main(String args[]) {
			iniciaServico();
	}
	
	/**
	 * Inicia o looping para começar a interação do jogo com o cliente.
	 */
	public static void iniciaServico(){
		while (true) {
			enviarPontuacaoFinal(enviarPerguntas(receberDificuldade()));
		}
	}
	
	/**
	 * Aguarda o recebimento do nivel de dificuldade escolhido pelo cliente
	 * e retorna o valor recebido.
	 * @return nivel de dificuldade
	 */
	private static String receberDificuldade(){
		return Data.receberDados();
	}

	/**
	 * Recebe a dificuldade selecionada e gerencia o envio das perguntas
	 * e recebimento das respostas do servidor com base nos dados recebidos
	 * do cliente
	 * @param dificuldade
	 * @return total de pontos na partida
	 */
	private static int enviarPerguntas(String dificuldade) {
		Pergunta[] perguntas = dificuldade.equals(NORMAL) ? Perguntas.getNormal() : Perguntas.getDificil();
		int pontos = 0;
		for (int i = 0; i < perguntas.length; i++) {
			String resposta = enviarPergunta(perguntas, i);
			pontos = validarResposta(perguntas[i], resposta, pontos);
		}
		return pontos;
	}

	/**
	 * Enviar uma pergunta e aguarda a resposta do cliente.
	 * @param perguntas, i
	 * @return resposta da pergunta
	 */
	private static String enviarPergunta(Pergunta[] perguntas, int i) {
		Data.enviarDados(perguntas[i].getQuestao() + DELIMITADOR + perguntas[i].getOpcaoA() + DELIMITADOR
				+ perguntas[i].getOpcaoB() + DELIMITADOR + perguntas[i].getOpcaoC());
		return Data.receberDados(); // recebe resposta
	}

	/**
	 * Recebe a pergunta e a resposta para validar se está correta, envia uma
	 * mensagem para o cliente avisando se ele acertou ou não E soma
	 * os pontos em caso de acerto ou erro. 
	 * @param resposta , pontos, perguntas
	 * @return pontos atualizados
	 */
	private static int validarResposta(Pergunta pergunta, String resposta, int pontos) {
		if (pergunta.getOpcaoCorreta().equalsIgnoreCase(resposta)) {
			Data.enviarDados("Certa Resposta!");
			pontos += RESPOSTA_CERTA;
		} else {
			Data.enviarDados("Resposta Incorreta!");
			pontos += RESPOSTA_ERRADA;
		}
		return pontos;
	}

	/**
	 * Ao final do fluxo de perguntas envia para o cliente o total 
	 * final de pontos somados.
	 * @param totalPontos
	 */
	private static void enviarPontuacaoFinal(int totalPontos) {
		Data.enviarDados("" + totalPontos);
	}
}
