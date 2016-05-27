
import java.net.InetSocketAddress;

import java.util.Scanner;

public class Client_udp implements Runnable {




	private String ip_m_d_1;
	private int port_d_1;

	private String ip_m_d_2;
	private int port_d_2;

	private boolean change_connexion = false;
	private Bufferconcurrent bufferConcurrent;



	public Client_udp(int port_multi_diff1,String adress_multi_diff1,int port_multi_diff2,String adress_multi_diff2,Bufferconcurrent buffconcurent) {
		this.port_d_1 = port_multi_diff1 ;
		this.ip_m_d_1 = adress_multi_diff1;

		this.port_d_2 = port_multi_diff2 ;
		this.ip_m_d_2 = adress_multi_diff2;


		this.bufferConcurrent = buffconcurent;

	}

	@Override
	public void run() {


		Scanner sc = new Scanner(System.in);
		String msgdenvoi = "";

		while (true) {



			try {

				if(this.bufferConcurrent.envoi_test){
					while(!this.bufferConcurrent.recu_test){
						System.out.print("");
						if((System.currentTimeMillis()-this.bufferConcurrent.startTime) > this.bufferConcurrent.TIME_MAX){
							this.bufferConcurrent.set_envoi_down(true) ;
							this.bufferConcurrent.envoi_test = false ;
							this.bufferConcurrent.recu_test = true ;
							EnvoiMulticast envoiMulticast = new EnvoiMulticast(Integer.parseInt(msgdenvoi.split("\\s")[3]),msgdenvoi.split("\\s")[2]);
							(new Thread(envoiMulticast)).start() ;
							if(!this.bufferConcurrent.is_duplicate){ // si elle n'est pas dupliquer
								break ;
							}
						}
					}
				}

				msgdenvoi = sc.nextLine();


				if(this.bufferConcurrent.deconnecter){
					break ;
				}
				if(msgdenvoi.toLowerCase().equals("info")){
					this.info();
					continue;
				}

				if(msgdenvoi.toLowerCase().equals("clear")){
					clear();
					continue ;
				}
				if(msgdenvoi.toLowerCase().equals("info protocole")){
					info_protocole();
					continue;
				}

				if(msgdenvoi.split("\\s").length >= 2){
					msgdenvoi = String_mani.code_id(msgdenvoi);
					if (msgdenvoi.split("\\s")[0].equals("WMSG")) {
						envoi_WHOSMSG(msgdenvoi);
					} else if (msgdenvoi.split("\\s")[0].equals("TEST")) {
						envoi_TEST(msgdenvoi);
					}else if (msgdenvoi.split("\\s")[0].equals("WHOS")) {
							envoi_whos(msgdenvoi);
					}else if(msgdenvoi.split("\\s")[0].equals("GBYE")){
						envoi_GBYE(msgdenvoi);
					}else if(msgdenvoi.split("\\s")[0].equals("rien")){
						envoi_rien(msgdenvoi);
					}else if(msgdenvoi.split("\\s")[0].equals("APPL")){
						if(msgdenvoi.split("\\s")[2].equals("DIFF####")){
							envoi_DIFF_msg(msgdenvoi) ;
						}else if(msgdenvoi.split("\\s")[2].equals("TRANS###")){
							if(msgdenvoi.split("\\s")[3].equals("REQ")){
								envoi_fichier(msgdenvoi);
							}else{
								System.out.println("erreur de protocole de msg");
							}
						}else{
							System.out.println("erreur de protocole de msg");
						}
					} else {
						System.out.println("erreur de protocole de msg ");
					}
				}else{
					System.out.println("erreur de msg : [protocol idm] ");
				}


				if (change_connexion) {
					this.bufferConcurrent.inetsocketAddress_1 = new InetSocketAddress(this.bufferConcurrent.ip_svt1,this.bufferConcurrent.port_udp_svt_1);
					this.bufferConcurrent.paquer_envoi_1.setSocketAddress(this.bufferConcurrent.inetsocketAddress_1);
					this.change_connexion = false;
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sc.close();
	}

	private void envoi_whos(String msg){
		if(this.bufferConcurrent.verification_envoi(msg)){
			this.bufferConcurrent.envoi_WHOS = true ;
			envoi_entite_suivant(msg , 0);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1], "WHOS");
		}
	}


	private void envoi_fichier(String msg){

		Scanner sc = new Scanner(System.in);
		System.out.println("quel fichier : ");
		String file = sc.nextLine();
		this.bufferConcurrent.name_fichier = file ;
		int size_file = file.length();
		msg=msg+" "+size_file+" "+file ;

		if(this.bufferConcurrent.verification_envoi(msg)){
			this.bufferConcurrent.envoi_REQ = true ;
			envoi_entite_suivant(msg , 0);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
		}
	}

	private void envoi_WHOSMSG(String msg) {

		if(this.bufferConcurrent.verification_envoi(msg)){
			envoi_entite_suivant(msg , 0);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
			this.bufferConcurrent.put_msg_envoyer_whosmsg(msg.split("\\s")[1]);
		}
	}

	private void envoi_TEST(String msg) {

		if(msg.split("\\s").length == 4){
			if(this.bufferConcurrent.verification_envoi(msg) && this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) == null ){
				if(this.bufferConcurrent.verification_test_ip_port(msg)){
					this.bufferConcurrent.start_envoi_test(System.currentTimeMillis());
					envoi_entite_suivant(msg , 0);
					this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
				}
			}
		if(this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) != null){
			System.out.println("quelqu'un a deja envoyer le msg avec l'identifiant "+msg.split("\\s")[1]);
		}
		}else{
			System.out.println("err protocole :  [TEST␣idm␣ip-diff␣port-diff]");
		}
	}


	private void envoi_GBYE(String msg){
		String envoi="" ;
		int anneau = 0 ;
		if(!this.bufferConcurrent.is_duplicate){
			envoi = msg+" " +String_mani.codageIp(this.bufferConcurrent.ip_entiter)+" "
                           +String_mani.codagePort(this.bufferConcurrent.port_entite)+" "
		               	   +String_mani.codageIp(this.bufferConcurrent.ip_svt1)+" "
		               	   +String_mani.codagePort(this.bufferConcurrent.port_udp_svt_1);
		}else{
			this.affichage_GBYE();
			Scanner sc1  = new Scanner(System.in);
			System.out.println("entrer l'ip :");
			String ip   = sc1.nextLine();

			System.out.println("entrer le port :");
			String port = sc1.nextLine();
			System.out.println("a quelle anneau : ");
			anneau = sc1.nextInt();
			envoi = msg+" "+String_mani.codageIp(this.bufferConcurrent.ip_entiter)+" "
						   +String_mani.codagePort(this.bufferConcurrent.port_entite)+" "
					       +ip+" "
						   +port;

			this.bufferConcurrent.msg_gbye = envoi ;

		}
		if(this.bufferConcurrent.verification_envoi(envoi)){
			envoi_entite_suivant(envoi,anneau);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
		}else{
			System.out.println("erreur de codage du message ");
		}
	}

	private void envoi_rien(String msg) {
		if(this.bufferConcurrent.verification_envoi(msg)){
			envoi_entite_suivant(msg , 0);
			this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
		}
	}

	private void envoi_DIFF_msg(String msg ){
		if(msg.split("\\s").length == 3){

			Scanner sc2 = new Scanner(System.in);
			System.out.println("Write the msg :");
			String message = sc2.nextLine();
			int taille = message.getBytes().length ;
			msg += " "+String_mani.TroisOctet(taille)+" "+message ;
			if(this.bufferConcurrent.verification_envoi(msg)){
				envoi_entite_suivant(msg , 0);
				this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1],msg.split("\\s")[0]);
			}else{
				System.out.println("erreur de codage du message");
			}
			}else{
				System.out.println("erreur de codage du message");
			}
	}



	private void envoi_entite_suivant(String msg ,int anneau ) {
		try {

			if(anneau == 1){
			if(!this.bufferConcurrent.is_deconnecte_anneau1){
				this.bufferConcurrent.data_envoi_1 = msg.getBytes();
				this.bufferConcurrent.paquer_envoi_1.setData(this.bufferConcurrent.data_envoi_1);
				this.bufferConcurrent.serveur_envoi_1.send(this.bufferConcurrent.paquer_envoi_1);
			}
			}else if(anneau == 2){
			if(this.bufferConcurrent.is_duplicate){
				if(!this.bufferConcurrent.is_deconnecte_anneau2){
					this.bufferConcurrent.data_envoi_2 = msg.getBytes();
					this.bufferConcurrent.paquer_envoi_2.setData(this.bufferConcurrent.data_envoi_2);
					this.bufferConcurrent.serveur_envoi_2.send(this.bufferConcurrent.paquer_envoi_2);
				}
			}
			}else{
				if(!this.bufferConcurrent.is_deconnecte_anneau1){
					this.bufferConcurrent.data_envoi_1 = msg.getBytes();
					this.bufferConcurrent.paquer_envoi_1.setData(this.bufferConcurrent.data_envoi_1);
					this.bufferConcurrent.serveur_envoi_1.send(this.bufferConcurrent.paquer_envoi_1);
				}

				if(this.bufferConcurrent.is_duplicate){
					if(!this.bufferConcurrent.is_deconnecte_anneau2){
						this.bufferConcurrent.data_envoi_2 = msg.getBytes();
						this.bufferConcurrent.paquer_envoi_2.setData(this.bufferConcurrent.data_envoi_2);
						this.bufferConcurrent.serveur_envoi_2.send(this.bufferConcurrent.paquer_envoi_2);
					}
			}
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void set_ip_m_d_2(String ip_m_d_2){
		this.ip_m_d_2 = ip_m_d_2 ;
	}
	public void set_port_m_d_2(int port_m_d_2){
		this.port_d_2 = port_m_d_2 ;
	}

	private void affichage_GBYE(){
		System.out.println("\nquelle anneau :");
		System.out.println("anneau 1 :");
		System.out.println("ip machine suivant 1   : "+this.bufferConcurrent.ip_svt1);
		System.out.println("port machine suivant 1 : "+this.bufferConcurrent.port_udp_svt_1);
		System.out.println("anneau 2 :");
		System.out.println("ip machine suivant 2   : "+this.bufferConcurrent.ip_svt2);
		System.out.println("port machine suivant 2 : "+this.bufferConcurrent.port_udp_svt_2 +"\n");
	}

	private void clear(){
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	private void info(){

		System.out.println("isduplicate    : "+this.bufferConcurrent.is_duplicate);
		System.out.println("ip machine     : "+this.bufferConcurrent.ip_entiter);
		System.out.println("port machine   : "+this.bufferConcurrent.port_entite);
		System.out.println("ip_diff_1      : "+this.bufferConcurrent.ip_m_d_1);
		System.out.println("port_diff1     : "+this.bufferConcurrent.port_d_1);
		System.out.println("ip_diff_2      : "+this.bufferConcurrent.ip_m_d_2);
		System.out.println("port_diff2     : "+this.bufferConcurrent.port_d_2);
		System.out.println("port_udp_svt_1 : "+this.bufferConcurrent.port_udp_svt_1);
		System.out.println("ip_udp_svt_1   : "+this.bufferConcurrent.ip_svt1);
		System.out.println("port_udp_svt_2 : "+this.bufferConcurrent.port_udp_svt_2);
		System.out.println("ip_udp_svt_2   : "+this.bufferConcurrent.ip_svt2 + 	"\n");

	}
	private void info_protocole(){

		System.out.println("\n[TEST␣idm␣ip-diff␣port-diff] ");
		System.out.println("[APPL␣idm␣id-app␣message-app] ");
		System.out.println("[WHOS␣idm]");
		System.out.println("[GBYE␣idm␣ip␣port␣ip-succ␣port-succ]");
		System.out.println("[APPL␣idm␣DIFF####␣size-mess␣mess]");
		System.out.println("[APPL␣idm␣TRANS###␣REQ␣size-nom␣nom-fichier]");
		System.out.println("[APPL␣idm␣TRANS###␣ROK␣id-trans␣size-nom␣nom-fichier␣nummess]");
		System.out.println("[APPL␣idm␣TRANS###␣SEN␣id-trans␣no-mess␣size-content␣content]");
		System.out.println("[WMSG␣idm]\n");

	}
}
