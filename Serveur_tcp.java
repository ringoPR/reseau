import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Serveur_tcp implements Runnable {
	private String identifiant ;
	private String msg_protocole;
	private int port_tcp;
	private String adr_entite;
	private String ip_svt1;
	private String ip_svt2;
	private int port_udp_svt1;
	private int port_udp_svt2 ;
	
	private String ip_m_d_1;
	private String ip_m_d_2 = null;
	private int port_m_d_1; // inferieur a 9999
	private int port_m_d_2 = -1 ;  
	private int port_udp;
	
	private boolean isduplicate = false;
	private boolean deja_client_udp = false ;
	private boolean deja_serveur_udp = false ; 
	
	public Serveur_tcp(String identifiant,int port_tcp_serveur_entite,String adr_tcp_entite, String ip_suivant,int port_udp_suivant, String add_ip_multi, int port_multi,int port_ecoute_udp) {
		this.identifiant = identifiant ;
		this.adr_entite= adr_tcp_entite ;
		this.port_tcp = port_tcp_serveur_entite;
		this.ip_svt1 = ip_suivant;
		this.port_udp_svt1 = port_udp_suivant;
		this.ip_m_d_1 = add_ip_multi;
		this.port_m_d_1 = port_multi;
		this.port_udp = port_ecoute_udp;
	}

	public void run() {
		Socket client = null;
		ServerSocket serverSocket = null;
		Client_udp client_udp= null ;
		
		try {
			Bufferconcurrent bufferconcurrent = new Bufferconcurrent(this.identifiant,this.port_udp,this.adr_entite,this.port_udp_svt1,this.ip_svt1) ;
			
			Serveur_udp serveur_udp = new Serveur_udp(identifiant,adr_entite, port_udp ,port_udp_svt1 ,ip_svt1 , bufferconcurrent) ;
			Thread threaserveur_udp = new Thread(serveur_udp);
			threaserveur_udp.start();
			
			
			serverSocket = new ServerSocket(this.port_tcp);
			
			
			client_udp = new Client_udp(this.port_m_d_1,this.ip_m_d_1,bufferconcurrent);
			Thread threadClient_udp = new Thread(client_udp);
			threadClient_udp.start();
			
			EnvoiMulticast envoiMulticast = new EnvoiMulticast(this.port_m_d_1, this.ip_m_d_1, bufferconcurrent);
			Thread thread_multicast = new Thread(envoiMulticast);
			thread_multicast.start();
			
			ReceiveMulticast receiveMulticast = new ReceiveMulticast(this.port_m_d_1, this.ip_m_d_1);
			Thread threadReceivMulicast = new Thread(receiveMulticast);
			threadReceivMulicast.start();
			
			while (true) {
				while(true){

				String msg_recu = "";
				client = serverSocket.accept();
				System.out.println("connexion tcp ");
				

				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
				
				
				if(isduplicate){ // pas de duplication ni d'insertion
					pw.write("NOTC\n");
					pw.flush();
					client.close();
					break ;
				}
				
				pw.write("WELC " + this.ip_svt1 + " "
						+ this.port_udp_svt1 + " "
						+ this.ip_m_d_1 + " " 
						+ this.port_m_d_1
						+ "\n");
				pw.flush();

				msg_recu = br.readLine();
				String_mani string_mani = new String_mani(msg_recu, 2, false);

				this.msg_protocole = string_mani.get_protocol_msg();

				if (msg_protocole.equals("NEWC")) {
					
						pw.write("ACKC\n");
						pw.flush();
						InetSocketAddress infoClient = (InetSocketAddress)client.getRemoteSocketAddress();
						this.ip_svt1 =infoClient.getAddress().getHostAddress() ;
						this.port_udp_svt1 = string_mani.get_port();
						
						if(deja_client_udp){
//							client_udp.set_port_svt1(port_udp_svt1);
							bufferconcurrent.set_ip_suivant(ip_svt1);
							bufferconcurrent.set_port_suivant(port_udp_svt1);
							
						}
				}
				
				if(!deja_client_udp){ 
					bufferconcurrent.set_ip_suivant(this.ip_svt1);
					bufferconcurrent.set_port_suivant(this.port_udp_svt1);
					bufferconcurrent.set_change_connexion(this.port_udp_svt1, this.ip_svt1);
					deja_client_udp = true ;
				}
				
				
				if(msg_protocole.equals("DUPL")){ // la duplication
					isduplicate = true ;
					pw.write("ACKD "+port_udp+"\n");
					pw.flush();
					this.ip_svt2 = msg_recu.split("\\s")[1];
					this.port_udp_svt2 = Integer.parseInt(msg_recu.split("\\s")[2]);
					this.ip_m_d_2 = msg_recu.split("\\s")[3] ; 
					this.port_m_d_2 = Integer.parseInt(msg_recu.split("\\s")[4]);
					
				}
				client.close();
				break ; 
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void set_duplicate(boolean isduplicate) {
		this.isduplicate = isduplicate;
	}
	

}
