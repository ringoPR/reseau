
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client_udp implements Runnable {

	
	private final int BUFFER_SIZE = 512;
	private final int SEZE_IDM = 8;

	private int port_udp_svt1;
	private String ip_svt1;
	private String ip_m_d_1;
	private int port_d_1;
	private boolean change_connexion = false;
	private Bufferconcurrent bufferConcurrent;
	
	

	public Client_udp(int port_multi_diff,String adress_multi_diff,Bufferconcurrent buffconcurent) {
		this.port_d_1 = port_multi_diff ;
		this.ip_m_d_1 = adress_multi_diff;
		this.bufferConcurrent = buffconcurent;
	}

	@Override
	public void run() {
	
	
		Scanner sc = new Scanner(System.in);
		String msgdenvoi = "";

		while (true) {
			try {

				msgdenvoi = sc.nextLine();

				if(this.bufferConcurrent.deconnecter){
					break ;
				}
				
				if (msgdenvoi.split("\\s")[0].equals("WHOS")) {
					envoi_WHOS(msgdenvoi);
				} else if (msgdenvoi.split("\\s")[0].equals("TEST")) {
					envoi_TEST(msgdenvoi);
				}else if(msgdenvoi.split("\\s")[0].equals("GBYE")){
					envoi_GBYE(msgdenvoi);
				} else {
					System.out.println("erreur de protocole de msg ");
				}

				if (change_connexion) {
					this.bufferConcurrent.inetsocketAddress = new InetSocketAddress(this.bufferConcurrent.ip_svt1,this.bufferConcurrent.port_udp_svt1);
					this.bufferConcurrent.paquer_envoi.setSocketAddress(this.bufferConcurrent.inetsocketAddress);
					this.change_connexion = false;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sc.close();
	}

	private void envoi_WHOS(String msg) {

		if (this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null) {
			if (taille_idm(msg.split("\\s")[1])) {
				if (taille_msg(msg)) {
					envoi_entite_suivant(msg);
					this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
					
				} else {
					System.out.println("la taille du message depasse "+ this.BUFFER_SIZE + " octet");
				}
			} else {
				System.out.println("l identifiant depasse la taille de "+ this.SEZE_IDM);
			}
		} else {
			System.out.println("vous avez deja envoyer un msg avec l identifiant "+ msg.split("\\s")[1]);
		}
	}

	private void envoi_TEST(String msg) {

		if (this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null) {
			if (taille_idm(msg.split("\\s")[1])) {
				if (taille_msg(msg)) {
					envoi_entite_suivant(msg);
					this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
				} else {
					System.out.println("la taille du message depasse "+ this.BUFFER_SIZE + " octet");
				}
			} else {
				System.out.println("l identifiant depasse la taille de "+ this.SEZE_IDM);
			}
		} else {
			System.out.println("vous avez deja envoyer un msg avec l identifiant "+ msg.split("\\s")[1]);
		}

	}
	private void envoi_GBYE(String msg){
		
		if (this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null) {
			if (taille_idm(msg.split("\\s")[1])) {
				String envoi = msg+" "+this.bufferConcurrent.ip_entiter+" "+this.bufferConcurrent.port_entite+" "+this.bufferConcurrent.ip_svt1+" "+this.bufferConcurrent.port_udp_svt1;
				if (taille_msg(envoi)) {
					envoi_entite_suivant(envoi);
					this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
				} else {
					System.out.println("la taille du message depasse "+ this.BUFFER_SIZE + " octet");
				}
			} else {
				System.out.println("l identifiant depasse la taille de "+ this.SEZE_IDM);
			}
		} else {
			System.out.println("vous avez deja envoyer un msg avec l identifiant "+ msg.split("\\s")[1]);
		}

	}

	

	
	private void envoi_entite_suivant(String msg) {
		try {

			this.bufferConcurrent.data_envoi = msg.getBytes();
			this.bufferConcurrent.paquer_envoi.setData(this.bufferConcurrent.data_envoi);
			this.bufferConcurrent.serveur_envoi.send(this.bufferConcurrent.paquer_envoi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean taille_msg(String msg) {
		byte msg_taille[] = msg.getBytes();
		return (msg_taille.length < this.BUFFER_SIZE);
	}

	private boolean taille_idm(String idm) {
		byte idm_taille[] = idm.getBytes();
		return (idm_taille.length < this.SEZE_IDM);
	}

	

	
	

	


}

