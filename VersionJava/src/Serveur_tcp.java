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


	public Serveur_tcp(String identifiant,int port_tcp_serveur_entite,String adr_tcp_entite, String ip_suivant,int port_udp_suivant, String add_ip_multi, int port_multi,int port_ecoute_udp) {
		this.identifiant = String_mani.codageIdMsg(identifiant) ;
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
			bufferconcurrent.set_ip_m_d_1(this.ip_m_d_1);
			bufferconcurrent.set_port_m_d_1(this.port_m_d_1);

			Serveur_udp serveur_udp = new Serveur_udp(bufferconcurrent) ;
			Thread threaserveur_udp = new Thread(serveur_udp);
			threaserveur_udp.start();


			serverSocket = new ServerSocket(this.port_tcp);


			client_udp = new Client_udp(this.port_m_d_1,this.ip_m_d_1,this.port_m_d_2,this.ip_m_d_2,bufferconcurrent);
			Thread threadClient_udp = new Thread(client_udp);
			threadClient_udp.start();


			ReceiveMulticast receiveMulticast = new ReceiveMulticast(this.port_m_d_1, this.ip_m_d_1,bufferconcurrent);
			Thread threadReceivMulicast = new Thread(receiveMulticast);
			threadReceivMulicast.start();

			while (true) {
				while(true){

				String msg_recu = "";
				client = serverSocket.accept();

				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));


				if(isduplicate){
					pw.write("NOTC\n");
					pw.flush();
					client.close();
					break ;
				}

				pw.write("WELC " + String_mani.codageIp(this.ip_svt1) + " "
						+ String_mani.codagePort(this.port_udp_svt1) + " "
						+ String_mani.codageIp(this.ip_m_d_1) + " "
						+ String_mani.codagePort(this.port_m_d_1)
						+ "\n");
				pw.flush();

				msg_recu = br.readLine();
				System.out.println("msg_recu :"+msg_recu);

				this.msg_protocole = msg_recu.split("\\s")[0];

				if (msg_protocole.equals("NEWC")) {

						pw.write("ACKC\n");
						pw.flush();
						InetSocketAddress infoClient = (InetSocketAddress)client.getRemoteSocketAddress();
						this.ip_svt1 =infoClient.getAddress().getHostAddress() ;
						this.port_udp_svt1 = Integer.parseInt(msg_recu.split("\\s")[2]);

						if(deja_client_udp){
							bufferconcurrent.set_ip_suivant(ip_svt1);
							bufferconcurrent.set_port_suivant(port_udp_svt1);
							bufferconcurrent.set_change_connexion_1(port_udp_svt1, ip_svt1);
						}
				}

				if(!deja_client_udp){
					bufferconcurrent.set_ip_suivant(this.ip_svt1);
					bufferconcurrent.set_port_suivant(this.port_udp_svt1);
					bufferconcurrent.set_change_connexion_1(this.port_udp_svt1, this.ip_svt1);
					deja_client_udp = true ;
				}


				if(msg_protocole.equals("DUPL")){ // la duplication
					isduplicate = true ;
					pw.write("ACKD "+String_mani.codagePort(port_udp)+"\n");
					pw.flush();
					InetSocketAddress infoClient = (InetSocketAddress)client.getRemoteSocketAddress();
					this.ip_svt2 =infoClient.getAddress().getHostAddress() ;
					this.port_udp_svt2 = Integer.parseInt(msg_recu.split("\\s")[2]);
					this.ip_m_d_2 = msg_recu.split("\\s")[3] ;
					this.port_m_d_2 = Integer.parseInt(msg_recu.split("\\s")[4]);
					bufferconcurrent.is_duplicate = true ;
					bufferconcurrent.creation_connexion_anneau2(this.port_udp_svt2,this.ip_svt2 );
					bufferconcurrent.set_ip_m_d_2(this.ip_m_d_2);
					bufferconcurrent.set_port_m_d_2(this.port_m_d_2);
					client_udp.set_ip_m_d_2(ip_m_d_2);
					client_udp.set_port_m_d_2(port_m_d_2);

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
