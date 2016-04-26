import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Bufferconcurrent {
	
	public String identifiant;
	public int port_entite ;
	public String ip_entiter ;
	public int port_udp_svt1;
	private String ip_svt1;
	public byte data_recoi[];
	public byte data_envoi[];
	public DatagramSocket client;
	public DatagramPacket paquer_envoi;
	public InetSocketAddress inetsocketAddress;
	
	public DatagramSocket serveur_recoi;
	public DatagramSocket serveur_envoi;
	public DatagramPacket paquer_recoi;
	private final int BUFFER_SIZE = 512;
	
	
	
	private boolean change_connexion = false;
	
	private HashMap<String, Boolean> les_messages_recu = new HashMap<String,Boolean>() ;
	private HashMap<String , String> les_messages_envoyer_entiter  = new HashMap<String,String>() ;
	private HashMap<String , Boolean> les_messages_envoyer_anneau =new HashMap<String,Boolean>();;
	
	
	public Bufferconcurrent(String id , int port_entite,String ip_entite,int port_svt,String ip_svt) {
		this.identifiant = id;
		this.port_entite=port_entite;
		this.ip_entiter = ip_entite ;
		this.port_udp_svt1 = port_svt ;
		this.ip_svt1 = ip_svt ;
		try {
			
			/*                 reception des données             */
			data_recoi = new byte[BUFFER_SIZE];
			this.serveur_recoi  = new DatagramSocket(this.port_entite);
			this.paquer_recoi   =  new DatagramPacket(data_recoi, BUFFER_SIZE);
			
			System.out.println("port entite = "+this.port_entite);
			
			
			/*                 envoi des données                                       */
			this.data_envoi = new byte[BUFFER_SIZE];
			this.inetsocketAddress = new  InetSocketAddress(ip_svt, port_udp_svt1);
			this.client = new DatagramSocket();
			this.paquer_envoi = new DatagramPacket(data_envoi,BUFFER_SIZE,inetsocketAddress);

			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	

	public void set_change_connexion(int port , String adress){
		this.inetsocketAddress  = new InetSocketAddress(adress, port);
		this.paquer_envoi.setSocketAddress(inetsocketAddress);
	}

	public void put_msg_envoyer_anneau(String msg){
		this.les_messages_envoyer_anneau.put(msg, true);
	}
	
	
	public Object get_msg_envoyer_anneau(String msg){
		return this.les_messages_envoyer_anneau.get(msg);
	}
	
	
	
	public void put_msg_envoyer_entiter(String idm,String protocol){
		this.les_messages_envoyer_entiter.put(idm,protocol);
	}
	
	
	public Object get_msg_envoyer_entiter(String msg){
		return this.les_messages_envoyer_entiter.get(msg);
	}
	
	public void put_msg_recu(String msg){
		this.les_messages_recu.put(msg, true);
	}
	
	public Object get_msg_recu(String msg){
		return this.les_messages_recu.get(msg);
	}
	
	public void change_connexion(){
		this.change_connexion =true ;
	}
	
	public void set_port_suivant(int port_svt){
		this.port_udp_svt1 = port_svt ;
	}
	public void set_ip_suivant(String ip_svt){
		this.ip_svt1 = ip_svt;
	}
	
		
	

}
