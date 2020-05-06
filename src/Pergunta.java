
public class Pergunta {

	private String questao;
	private String opcaoA;
	private String opcaoB;
	private String opcaoC;
	private String opcaoCorreta;

	public Pergunta(String questao, String opcaoA, String opcaoB, String opcaoC, String opcaoCorreta) {
		super();
		this.questao = questao;
		this.opcaoA = opcaoA;
		this.opcaoB = opcaoB;
		this.opcaoC = opcaoC;
		this.opcaoCorreta = opcaoCorreta;
	}

	public Pergunta() {
		super();
	}

	public String getQuestao() {
		return questao;
	}

	public void setQuestao(String questao) {
		this.questao = questao;
	}

	public String getOpcaoA() {
		return opcaoA;
	}

	public void setOpcaoA(String opcaoA) {
		this.opcaoA = opcaoA;
	}

	public String getOpcaoB() {
		return opcaoB;
	}

	public void setOpcaoB(String opcaoB) {
		this.opcaoB = opcaoB;
	}

	public String getOpcaoC() {
		return opcaoC;
	}

	public void setOpcaoC(String opcaoC) {
		this.opcaoC = opcaoC;
	}

	public String getOpcaoCorreta() {
		return opcaoCorreta;
	}

	public void setOpcaoCorreta(String opcaoCorreta) {
		this.opcaoCorreta = opcaoCorreta;
	}
}
