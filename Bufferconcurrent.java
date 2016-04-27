import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Bufferconcurrent {
	
	public String identifiant;
	public int port_entite ;
	public String ip_entiter ;
	public int port_udp_svt1;
	public String ip_svt1;
	public byte data_recoi[];
	public byte data_envoi[];
	public DatagramSocket serveur_recoi;
	public DatagramSocket serveur_envoi;
	
	public DatagramPacket paquer_envoi;
	public DatagramPacket paquer_recoi;
	public InetSocketAddress inetsocketAddress;
	public boolean deconnecter = false ;
	private final int BUFFER_SIZE = 512;
	
	
	public DatagramSocket diffusion;
	
	
	
	
	
	private HashMap<String, Boolean> les_messages_recu = new HashMap<String,Boolean>() ;
	private HashMap<String , String> les_messages_envoyer_entiter  = new HashMap<String,String>() ;
	private HashMap<String , Boolean> les_messages_envoyer_anneau =new HashMap<String,Boolean>();;
	
	/*   pour le msg TEST    */
	public boolean recu_test = false ; 
	public boolean envoi_test = false ; 
	public boolean envoi_down = false ;
	public long startTime  = 0; 
	public long endTime = 0 ; 
	public final long  TIME_MAX = 5000; 
	int cpt = 0 ;
	
	public Bufferconcurrent(String id , int port_entite,String ip_entite,int port_svt,String ip_svt) {
		this.identifiant = id;
		this.port_entite=port_entite;
		this.ip_entiter = ip_entite ;
		this.port_udp_svt1 = port_svt ;
		this.ip_svt1 = ip_svt ;
		try {
			/*                 reception des données             */
			
			this.data_recoi = new byte[BUFFER_SIZE];
			this.serveur_recoi  = new DatagramSocket(this.port_entite);
			this.paquer_recoi   =  new DatagramPacket(this.data_recoi, this.BUFFER_SIZE);
			
			System.out.println("port entite = "+this.port_entite);
			
			
			/*                 envoi des données                                       */
			this.data_envoi = new byte[BUFFER_SIZE];
			this.inetsocketAddress = new  InetSocketAddress(ip_svt, port_udp_svt1);
			this.serveur_envoi = new DatagramSocket();
			this.paquer_envoi = new DatagramPacket(data_envoi,BUFFER_SIZE,inetsocketAddress);
			
			
		
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	

	public void set_change_connexion(int port , String adress){ // changement de connexion 
		this.ip_svt1 = adress ;
		this.port_udp_svt1 = port ;
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
	
	
	
	public void set_port_suivant(int port_svt){
		this.port_udp_svt1 = port_svt ;
	}
	public void set_ip_suivant(String ip_svt){
		this.ip_svt1 = ip_svt;
	}
	
	
	public void start_envoi_test(long time ){
		this.envoi_test = true ;
		this.recu_test = false ;
		this.startTime = time;
	}
	
/*	public void initialisation_temps(){
		this.envoi_test = false ;
		recu_test = false ; 
		this.startTime = 0 ;
		this.endTime = 0 ;
	}*/
	
	public void close(){
		this.deconnecter = true ; 
		this.serveur_recoi.close();
		this.serveur_envoi.close();
	}
		
	

}
