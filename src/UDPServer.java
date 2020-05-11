public class UDPServer {
	
	public static void main(String args[]) {
			iniciarSlots();
	}
	
	/**
	 * Ao subir a aplicação inicia duas threads que aguardam pelo
	 * client para iniciar a partida. Permite 2 clientes jogarem
	 * simultaneamente. OBS: cada um na sua partida.
	 */
	public static void iniciarSlots(){
		
		Thread slot1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		        while(true)
		        {
		        	Partida partida = new Partida();
					partida.iniciarPartida(50000);
		        }
		    }
		});
		
		Thread slot2 = new Thread(new Runnable() {

		    @Override
		    public void run() {
		        while(true)
		        {
		        	Partida partida = new Partida();
					partida.iniciarPartida(40000);
		        }
		    }
		});
		
		slot1.start();
		slot2.start();
	}


}
