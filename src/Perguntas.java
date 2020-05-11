
public class Perguntas {

	public static final Pergunta[] normal = new Pergunta[3];
	public static final Pergunta[] dificil = new Pergunta[3];

	static {
		montarPerguntasNormal();
		montarPerguntasDificil();
	}

	private static void montarPerguntasNormal() {
		normal[0] = new Pergunta("Qual protocolo é utilizado para comunicação de correio eletrônico?", "IMAP", "POP",
				"SMTP", "SMTP");
		normal[1] = new Pergunta(
				"Fornece endereços IP temporários e gerencia de forma central e automática a atribuição das configurações TCP/IP das máquinas de uma rede. Qual protocolo possui essas funções?",
				"DHCP", "NAT", "IMAP", "DHCP");
		normal[2] = new Pergunta("Qual das alternativas representa uma característica de um Socket UDP?",
				"Controla o estado de conexão", "Garante ordem de entrega", "Não confiável", "Não confiável");
	}

	private static void montarPerguntasDificil() {
		dificil[0] = new Pergunta("Qual das alternativas representa uma característica de um Socket TCP?",
				"Não garante entrega", "Controle de congestionamento e fluxo", "Não tem estado de conexão",
				"Controle de congestionamento e fluxo");
		dificil[1] = new Pergunta("Uma máscara Máscara: 255.255.0.0, representa um endereço IP de qual classe?",
				"Classe A", "Classe B", "Classe C", "Classe B");
		dificil[2] = new Pergunta("Normalmente o primeiro endereço de uma subrede é destinado ao:",
				"Endereço de Broadcast", "Endereço de Máscara", "Endereço de Rede", "Endereço de Rede");
	}

	public static Pergunta[] getNormal() {
		return normal;
	}

	public static Pergunta[] getDificil() {
		return dificil;
	}

}
