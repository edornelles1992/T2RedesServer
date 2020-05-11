
public class Partida extends Data implements Parametros {

	String dificuldade;
	Data data = new Data();

	/**
	 * Inicia o fluxo para começar a partida com o cliente conectado nesse socket.
	 * Inicializar o socket, aguarda e recebe a dificuldade selecionada e executa o fluxo da partida.
	 * Ao final da partida fecha o socket.
	 */
	public void iniciarPartida(Integer porta) {
		data.iniciarSocket(porta);
		dificuldade = receberDificuldade();
		enviarPontuacaoFinal(enviarPerguntas(dificuldade));
		data.fecharSocket();
	}

	/**
	 * Aguarda o recebimento do nivel de dificuldade escolhido pelo cliente e
	 * retorna o valor recebido.
	 * 
	 * @return nivel de dificuldade
	 */
	public String receberDificuldade() {
		return data.receberDados();
	}

	/**
	 * Recebe a dificuldade selecionada e gerencia o envio das perguntas e
	 * recebimento das respostas do servidor com base nos dados recebidos do cliente
	 * 
	 * @param dificuldade
	 * @return total de pontos na partida
	 */
	private int enviarPerguntas(String dificuldade) {
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
	 * 
	 * @param perguntas, i
	 * @return resposta da pergunta
	 */
	private String enviarPergunta(Pergunta[] perguntas, int i) {
		data.enviarDados(perguntas[i].getQuestao() + DELIMITADOR + perguntas[i].getOpcaoA() + DELIMITADOR
				+ perguntas[i].getOpcaoB() + DELIMITADOR + perguntas[i].getOpcaoC());
		return data.receberDados(); // recebe resposta
	}

	/**
	 * Recebe a pergunta e a resposta para validar se está correta, envia uma
	 * mensagem para o cliente avisando se ele acertou ou não E soma os pontos em
	 * caso de acerto ou erro.
	 * 
	 * @param resposta , pontos, perguntas
	 * @return pontos atualizados
	 */
	private int validarResposta(Pergunta pergunta, String resposta, int pontos) {
		if (pergunta.getOpcaoCorreta().equalsIgnoreCase(resposta)) {
			data.enviarDados("Certa Resposta!");
			pontos += RESPOSTA_CERTA;
		} else {
			data.enviarDados("Resposta Incorreta!");
			pontos += RESPOSTA_ERRADA;
		}
		return pontos;
	}

	/**
	 * Ao final do fluxo de perguntas envia para o cliente o total final de pontos
	 * somados.
	 * 
	 * @param totalPontos
	 */
	private void enviarPontuacaoFinal(int totalPontos) {
		data.enviarDados("" + totalPontos);
	}
}
