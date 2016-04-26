import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Entité_bis {

	static String identifiant = "0";
	static int port_ecou_udp = 9998; // inferieur a 9999
	static String adr_ip_machine_suivant;
	static int port_ecou_udp_mach_suivant = 0;
	static String add_ipv4_multi_diff1 = "254.255.255.255";
	static String add_ipv4_multi_diff2 = null;
	static int port_multi_diff1 = 9980; // inferieur a 9999
	static int port_multi_diff2 = -1 ;
	static int port_tcp;
	static int port_tcp_serveur_entite = 4242;
	

	public static void main(String[] args) {

		String beginning = "";
		String addr_tcp = "";
		String ip_diff_dupl="";
		int port_diff_dupl = 0; 
		boolean is_duplicate = false ;
		boolean debut_anneau = true;

		if (args.length == 1) {
			beginning = "beginning";
		}
		if (args.length >= 5) {
			try {
				identifiant = args[4];
				port_tcp_serveur_entite = Integer.parseInt(args[3]);
				port_ecou_udp = Integer.parseInt(args[2]);
				addr_tcp = args[1];
				port_tcp = Integer.parseInt(args[0]);
				debut_anneau = false;
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("erreurs d'argument ");
			}
		}
		if (args.length == 6) {
			is_duplicate =true;
			add_ipv4_multi_diff2="253.253.253.253";
			port_multi_diff2 = 9000;
		}
				
		try {

			if (!beginning.equals("beginning")) {
//				System.out.println("debut");
//				System.out.println("port_tcp a communiquer   	: " + port_tcp);
//				System.out.println("address tcp   				: " + addr_tcp);
//				System.out.println("port_ecou_udp 				: " + port_ecou_udp);
//				System.out.println("port_tcp_serveur_entite 	: " + port_tcp_serveur_entite);
//				System.out.println("identifaint  		    	: " + identifiant);
				
				

				Client_tcp client_tcp = new Client_tcp(port_tcp, addr_tcp);
				String msg_recu = client_tcp.readligne_client();
				
				if((msg_recu.split("\\s")[0]).equals("NOTC")){
					System.out.println("l entité refuse toute demande car elle est dupliquer");
					System.exit(0);
				}

				String protocole_msg = msg_recu.split("\\s")[0];

				if (!protocole_msg.equals("WELC")  ) {
						System.out.println("erreur de protocole de msg");
						System.exit(0);
				}
				
			
				
				if (protocole_msg.equals("WELC")) {
					adr_ip_machine_suivant = msg_recu.split("\\s")[1];
					port_ecou_udp_mach_suivant = Integer.parseInt(msg_recu.split("\\s")[2]);
					add_ipv4_multi_diff1 = msg_recu.split("\\s")[3];
					port_multi_diff1 = Integer.parseInt(msg_recu.split("\\s")[4]);
					
					String ip_local = Inet4Address.getLocalHost().getHostAddress();
				
					
					if (!is_duplicate) { 
						client_tcp.write_client("NEWC " + ip_local + " "+ port_ecou_udp + "\n");// l ip du client et son port
						msg_recu = client_tcp.readligne_client();

						if (msg_recu.equals("ACKC")) {
							System.out.println("insertion reussite ");
						} else {
							System.out.println("pas d'inssertion ");
						}
					}
					
					if(is_duplicate){
						client_tcp.write_client("DUPL "+ ip_local +" "+ port_ecou_udp +" "+ip_diff_dupl+" "+port_diff_dupl+"\n");
						msg_recu = client_tcp.readligne_client();
						if((msg_recu.split("\\s")[0]).equals("ACKD")){
							System.out.println("duplication reussite");
							port_ecou_udp_mach_suivant = Integer.parseInt(msg_recu.split("\\s")[1]);
						}
							}
						client_tcp.close_br_client();
						client_tcp.close_client();
				}
			}
			

			if (debut_anneau ) {
				port_ecou_udp_mach_suivant = port_ecou_udp;
				adr_ip_machine_suivant = args[0];
				addr_tcp = args[0];

			}
			
			
			Serveur_tcp serveur_tcp ;
			Thread thread_tcp ;
			
			if(!is_duplicate){
				serveur_tcp = new Serveur_tcp(identifiant,port_tcp_serveur_entite,addr_tcp,adr_ip_machine_suivant, port_ecou_udp_mach_suivant,add_ipv4_multi_diff1, port_multi_diff1,port_ecou_udp);
			 thread_tcp= new Thread(serveur_tcp);
				thread_tcp.start();
			}
			if(is_duplicate){
				serveur_tcp = new Serveur_tcp(identifiant,port_tcp_serveur_entite,addr_tcp,adr_ip_machine_suivant, port_ecou_udp_mach_suivant,ip_diff_dupl, port_diff_dupl,port_ecou_udp);
				thread_tcp = new Thread(serveur_tcp);
				thread_tcp.start();
				serveur_tcp.set_duplicate(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
