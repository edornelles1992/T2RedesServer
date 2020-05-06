
public class Perguntas {

	public static final Pergunta[] normal = new Pergunta[3]; 
	public static final Pergunta[] dificil = new Pergunta[3]; 
	
	static {
		montarPerguntasNormal();
		montarPerguntasDificil();
	}

	private static void montarPerguntasNormal() {
		normal[0] = new Pergunta("qual é a resposta pergunta 1? ","bola","casa","chapeu","bola");
		normal[1] = new Pergunta("qual é resposta pergunta 2?","bola","casa","chapeu","casa");
		normal[2] = new Pergunta("qual é resposta pergunta 3?","bola","casa","chapeu","chapeu");
	}

	private static void montarPerguntasDificil() {
		dificil[0] = new Pergunta("qual é a resposta pergunta 1?","leite","uva","chapeu","leite");
		dificil[1] = new Pergunta("qual é a resposta pergunta 2?","leite","uva","chapeu","uva");
		dificil[2] = new Pergunta("qual é a resposta pergunta 3?","leite","uva","chapeu","chapeu");
	}
}
