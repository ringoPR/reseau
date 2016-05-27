import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class Entité_bis {

	static String identifiant = "0";
	static int p_ecoute_udp = 9998; // inferieur a 9999 par defaut debut de l'anneau
	static String ip_entité = "";

	static String ip_m_svt;
	static int p_svt = 0;
	static String ip_m_d_1 = "225.006.002.004";
	static int p_m_d_1 = 9980; // inferieur a 9999

	static String ip_m_d_2 = "225.006.002.003";
	static int p_m_d_2 = 9979 ;

	static int port_tcp_a_communiquer;
	static int port_tcp_serveur_entite = 4242;


	public static void main(String[] args) {
		String_mani string_mani = new String_mani();
		String beginning = "";

		boolean is_duplicate = false ;
		boolean debut_anneau = true;

		if (args.length == 0) {
			beginning = "beginning";
		}

		if (args.length >= 5) {
			try {
				identifiant = args[4];
				port_tcp_serveur_entite = Integer.parseInt(args[3]);
				p_ecoute_udp = Integer.parseInt(args[2]);
				ip_entité = args[1];
				port_tcp_a_communiquer = Integer.parseInt(args[0]);
				debut_anneau = false;

			} catch (NumberFormatException e) {
				e.printStackTrace();
				mes_erreur() ;
				System.exit(0);
			}
		}
		if (args.length == 6) {
			is_duplicate =true;
			if(!args[5].equals("dup")){
				mes_erreur() ;
				System.exit(0);
			}
		}
		if(args.length  == 8){
			is_duplicate =true;
			if(!args[5].equals("dup")){
				mes_erreur() ;
				System.exit(0);
			}
			try {
				ip_m_d_2 =string_mani.codageIp(args[6]);
				p_m_d_2 = Integer.parseInt(string_mani.codagePort(Integer.parseInt(args[7])))  ;
			}catch(NumberFormatException e){
				e.printStackTrace();
				mes_erreur() ;
				System.exit(0);
			}
		}


		try {

			if (!beginning.equals("beginning")) {



				Client_tcp client_tcp = new Client_tcp(port_tcp_a_communiquer, ip_entité);
				String msg_recu = client_tcp.readligne_client();
				System.out.println("msg recu : "+msg_recu);
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
					ip_m_svt  = string_mani.codageIp(msg_recu.split("\\s")[1]);
					p_svt = Integer.parseInt(string_mani.codagePort(Integer.parseInt(msg_recu.split("\\s")[2])));
					ip_m_d_1 = string_mani.codageIp(msg_recu.split("\\s")[3]);
					p_m_d_1 = Integer.parseInt(string_mani.codagePort(Integer.parseInt(msg_recu.split("\\s")[4]))) ;

//					String ip_local = Inet4Address.getLocalHost().getHostAddress();
					String ip_local = get_Ip_local() ;

					if (!is_duplicate) {
						client_tcp.write_client("NEWC " + String_mani.codageIp(ip_local) + " "+ String_mani.codagePort(p_ecoute_udp) + "\n");// l ip du client et son port
						msg_recu = client_tcp.readligne_client();

						System.out.println("msg recu : "+msg_recu);
						if (msg_recu.equals("ACKC")) {
							System.out.println("insertion reussite ");
						} else {
							System.out.println("pas d'inssertion ");
						}
					}

					if(is_duplicate){

						client_tcp.write_client("DUPL "+String_mani.codageIp(ip_local) +" "+String_mani.codagePort(p_ecoute_udp) +" "+String_mani.codageIp(ip_m_d_2)+" "+String_mani.codagePort(p_m_d_2)+"\n");
						msg_recu = client_tcp.readligne_client();
						System.out.println("msg_recu : "+msg_recu);
						if((msg_recu.split("\\s")[0]).equals("ACKD")){
							System.out.println("duplication reussite");
							p_svt =  Integer.parseInt(string_mani.codagePort(Integer.parseInt(msg_recu.split("\\s")[1]))) ;
							ip_m_svt=string_mani.codageIp(args[1]);
						}
							}
						client_tcp.close_br_client();
						client_tcp.close_client();
				}
			}


			if (debut_anneau ) { // si par defaut
				p_svt = Integer.parseInt(string_mani.codagePort(p_ecoute_udp));
				ip_m_svt = string_mani.codageIp(get_Ip_local());
				ip_entité = string_mani.codageIp(get_Ip_local());

			}

			ip_entité = string_mani.codageIp(get_Ip_local()) ;
			System.out.println("mon adresse ip locale : "+ip_entité+" mon port TCP "+port_tcp_serveur_entite);
			Serveur_tcp serveur_tcp ;
			Thread thread_tcp ;


			if(!is_duplicate){
				serveur_tcp = new Serveur_tcp(identifiant,port_tcp_serveur_entite,ip_entité,ip_m_svt, p_svt,ip_m_d_1, p_m_d_1,p_ecoute_udp);
			 thread_tcp= new Thread(serveur_tcp);
				thread_tcp.start();
			}
			if(is_duplicate){
				serveur_tcp = new Serveur_tcp(identifiant,port_tcp_serveur_entite,ip_entité,ip_m_svt, p_svt,ip_m_d_2, p_m_d_2,p_ecoute_udp);
				thread_tcp = new Thread(serveur_tcp);
				thread_tcp.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get_Ip_local(){
		Enumeration e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
		String res ="";
		while(e.hasMoreElements()){
		    NetworkInterface n = (NetworkInterface) e.nextElement();
		    Enumeration ee = n.getInetAddresses();

		    while (ee.hasMoreElements()){
		        InetAddress i = (InetAddress) ee.nextElement();

		       if(i.isSiteLocalAddress()){
		    	 		res =i.getHostAddress() ;
		       }
		    }

		}
		return res;
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null ;
	}
	static void mes_erreur(){

		System.out.println("erreur d'argument");
		System.out.println("exemple   : java Entité_bis 4242 127.0.0.1 9995 4248 3 [[dup] [ip_m_d_2] [p_m_d_2]]");
		System.out.println("java      : commande a executer le programme");
		System.out.println("4242      : port tcp de la machine a communiquer ");
		System.out.println("127.0.0.1 : address_tcp de la machine a communiquer");
		System.out.println("9995      : son propre port udp d'écoute");
		System.out.println("4248      : port_tcp_serveur_entite");
		System.out.println("3         : identifiant de l entite");
		System.out.println("dup       : pour dire qu elle va dupliquer  ");
		System.out.println("ip_m_d_2  : Adresse ip de la diffusion du deuxieme anneau  ");
		System.out.println("p_m_d_2   : port de la diffusion du deuxieme anneau ");

	}



}
