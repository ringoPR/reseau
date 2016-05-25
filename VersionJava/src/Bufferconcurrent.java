import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Bufferconcurrent {
	
	public boolean is_duplicate = false ;
	public String identifiant;
	public int port_entite ;
	public String ip_entiter ;
	
	
	public int port_udp_svt_1;
	public String ip_svt1;
	public byte data_recoi_1[];
	public byte data_envoi_1[];
	public DatagramSocket serveur_recoi_1;
	public DatagramSocket serveur_envoi_1;
	
	
	public int port_udp_svt_2;
	public String ip_svt2;
	public byte data_recoi_2[];
	public byte data_envoi_2[];

	public DatagramSocket serveur_envoi_2;
	
	
	
	public DatagramPacket paquer_envoi_1;
	public DatagramPacket paquer_recoi_1;
	public InetSocketAddress inetsocketAddress_1;
	
	public DatagramPacket paquer_envoi_2;
	public DatagramPacket paquer_recoi_2;
	public InetSocketAddress inetsocketAddress_2;
	

	private final int BUFFER_SIZE = 512;
	private final int SEZE_IDM = 8;
	
	public DatagramSocket diffusion;
	
	
	public String ip_m_d_1;
	public int port_d_1;
	
	public String ip_m_d_2;
	public int port_d_2;
	
	
	
	
	private HashMap<String , String> les_messages_envoyer_entiter  = new HashMap<String,String>() ;
	private HashMap<String , Boolean> les_messages_envoyer_anneau =new HashMap<String,Boolean>();
	private HashMap<String , Boolean> les_messages_envoyer_MBRE =new HashMap<String,Boolean>();
	private HashMap<String , Boolean> les_messages_envoyer_WHOS =new HashMap<String,Boolean>();
	
	public boolean deconnecter = false ;
	
	/*   pour le msg TEST    */
	public boolean recu_test = false ; 
	public boolean envoi_test = false ; 
	public boolean envoi_down = false ;
	public boolean down_recu  = false ;
	public long startTime  = 0; 
	public boolean is_deconnecte_anneau1 = false ;
	public boolean is_deconnecte_anneau2 = false ;
	public boolean envoi_REQ = false ;
	public boolean envoi_ROK = false ;
	public boolean envoi_SEN = false ;
	public static String name_fichier ;

	public final long  TIME_MAX = 5000; 

	/*msg GBYE*/
	public String msg_gbye ;
	
	public Bufferconcurrent(String id , int port_entite,String ip_entite,int port_svt,String ip_svt) {
		this.identifiant = id;
		this.port_entite=port_entite;
		this.ip_entiter = ip_entite ;
		this.port_udp_svt_1 = port_svt ;
		this.ip_svt1 = ip_svt ;
		try {
			/*                 reception des données   anneau 1          */
			
			this.data_recoi_1 = new byte[BUFFER_SIZE];
			this.serveur_recoi_1  = new DatagramSocket(this.port_entite);
			this.paquer_recoi_1   =  new DatagramPacket(this.data_recoi_1, this.BUFFER_SIZE);
			
			
			
			
			/*                 envoi des données      anneau 1                                 */
			this.data_envoi_1 = new byte[BUFFER_SIZE];
			this.inetsocketAddress_1 = new  InetSocketAddress(this.ip_svt1, port_udp_svt_1);
			this.serveur_envoi_1 = new DatagramSocket();
			this.paquer_envoi_1 = new DatagramPacket(data_envoi_1,BUFFER_SIZE,inetsocketAddress_1);
			
			
		
			
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("le port "+this.port_d_1+" est deja utiliser");
				System.exit(0);
			}
		
	}
	

	public void set_change_connexion_1(int port , String adress){ // changement de connexion 
		this.ip_svt1 = adress ;
		this.port_udp_svt_1 = port ;
		this.inetsocketAddress_1  = new InetSocketAddress(adress, port);
		this.paquer_envoi_1.setSocketAddress(inetsocketAddress_1);
	}
	
	

	public void put_msg_envoyer_anneau(String msg){
		this.les_messages_envoyer_anneau.put(msg, true);
	}
	
	
	public Object get_msg_envoyer_anneau(String msg){
		return this.les_messages_envoyer_anneau.get(msg);
	}
	
	
	public void put_msg_envoyer_memb(String msg){
		this.les_messages_envoyer_MBRE.put(msg, true);
	}
	
	
	public Object get_msg_envoyer_memb(String msg){
		return this.les_messages_envoyer_MBRE.get(msg);
	}
	

	public void put_msg_envoyer_whos(String msg){
		this.les_messages_envoyer_WHOS.put(msg, true);
	}
	
	
	public Object get_msg_envoyer_whos(String msg){
		return this.les_messages_envoyer_WHOS.get(msg);
	}
	
	
	public void put_msg_envoyer_entiter(String idm,String protocol){
		this.les_messages_envoyer_entiter.put(idm,protocol);
	}
	
	
	public Object get_msg_envoyer_entiter(String msg){
		return this.les_messages_envoyer_entiter.get(msg);
	}
	

	
	
	public void set_port_suivant(int port_svt){
		this.port_udp_svt_1 = port_svt ;
	}
	public void set_ip_suivant(String ip_svt){
		this.ip_svt1 = ip_svt;
	}
	
	
	public void start_envoi_test(long time ){
		this.envoi_test = true ;
		this.recu_test = false ;
		this.startTime = time;
	}
	
	
	
	
	public void creation_connexion_anneau2(int port_svt_2 , String ip_svt_2 ){
		this.port_udp_svt_2 = port_svt_2 ;
		this.ip_svt2 = ip_svt_2 ;
		try{
			
			/*  reception des donne */
			this.data_recoi_2 = new byte[BUFFER_SIZE];
//			this.serveur_recoi_2  = new DatagramSocket(this.port_entite);
			this.paquer_recoi_2   =  new DatagramPacket(this.data_recoi_2, this.BUFFER_SIZE);
			
			this.data_envoi_2 = new byte[BUFFER_SIZE];
			this.inetsocketAddress_2 = new  InetSocketAddress(this.ip_svt2, this.port_udp_svt_2);
			this.serveur_envoi_2 = new DatagramSocket();
			this.paquer_envoi_2 = new DatagramPacket(this.data_envoi_2,this.BUFFER_SIZE,this.inetsocketAddress_2);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void close(){
	
		System.out.println("dans close");
//		this.deconnecter = true ;
		if(!is_duplicate){
			this.serveur_recoi_1.close();
			this.serveur_envoi_1.close();
			
		}else{
			if(msg_gbye.split("\\s")[4].equals(String_mani.codageIp(this.ip_svt1))&& msg_gbye.split("\\s")[5].equals(String_mani.codagePort(this.port_udp_svt_1))){
//				this.serveur_recoi_1.close();
				this.serveur_envoi_1.close();
				is_deconnecte_anneau1 = true ;
			}else if(msg_gbye.split("\\s")[4].equals(String_mani.codageIp(this.ip_svt2))&& msg_gbye.split("\\s")[5].equals(String_mani.codagePort(this.port_udp_svt_2))){
				this.serveur_envoi_2.close();
				is_deconnecte_anneau2 = true ;
				
			}else{
				System.out.println("a suprimer");
			}
			
		}
	}
	
	public void set_envoi_down(boolean bool){
		this.envoi_down = bool ;
	}
	
	public boolean get_envoi_down(){
		return envoi_down ;
	}
	
public boolean verification_envoi_recu(String msg){
		
		boolean envoi = false ; 
//			System.out.println(msg);
			if (taille_idm(msg.split("\\s")[1])) {
				if (taille_msg(msg)) {
					
					if(verification_codage(msg)){// je suis la
					envoi = true ;
				
					}
				} else {
					System.out.println("la taille du message depasse "+ this.BUFFER_SIZE + " octets");
				}
			} else {
				System.out.println(" taille de l identifiant != "+ this.SEZE_IDM+" octets");
			}
		
		return envoi ; 
	}

public boolean verification_envoi(String msg){
	
	boolean envoi = false ; 
	if (this.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null) {
		
		envoi = verification_envoi_recu(msg);
		
		
	} else {
		System.out.println("vous avez deja envoyer un msg avec l identifiant "+ msg.split("\\s")[1]);
	}
	return envoi ; 
}

public boolean verification_codage(String msg){
	if (msg.split("\\s")[0].equals("WHOS")) {
		
		return taille_idm(msg.split("\\s")[1]) ;
		
	} else if (msg.split("\\s")[0].equals("TEST")) {
		
		return (taille_idm(msg.split("\\s")[1]) &&  ip_veri(msg.split("\\s")[2]) && port_veri(msg.split("\\s")[3]));
		
	}else if(msg.split("\\s")[0].equals("GBYE")){
		
		return (taille_idm(msg.split("\\s")[1]) && ip_veri(msg.split("\\s")[2]) && port_veri(msg.split("\\s")[3]) && ip_veri(msg.split("\\s")[4]) && port_veri(msg.split("\\s")[5]));
		
	}else if(msg.split("\\s")[0].equals("APPL")){
		return taille_idm(msg.split("\\s")[1]) && taille_idm(msg.split("\\s")[2]) && taille_msg(msg) ;
	}

	return false ;
}

	public boolean verification_test_ip_port(String msg ){
		
		if(msg.split("\\s")[2].equals(this.ip_m_d_1) && Integer.parseInt(msg.split("\\s")[3]) == this.port_d_1){
			return true ;
		}
		if(is_duplicate){
			if(msg.split("\\s")[2].equals(this.ip_m_d_2) && Integer.parseInt(msg.split("\\s")[3]) == this.port_d_2){
				return true ;
			}
		}
		System.out.println("l'ip : "+msg.split("\\s")[2]+" ou le port : "+msg.split("\\s")[3]+" n existe pas ");
		return false ;
		
	}
	
	private boolean taille_msg(String msg) {
		byte msg_taille[] = msg.getBytes();
		return (msg_taille.length < this.BUFFER_SIZE);
	}

	private boolean taille_idm(String idm) {
		byte idm_taille[] = idm.getBytes();
		return (idm_taille.length == this.SEZE_IDM);
	}


	
	public void set_ip_m_d_2(String ip_m_d_2){
		this.ip_m_d_2 = ip_m_d_2 ; 
	}
	public void set_port_m_d_2(int port_m_d_2){
		this.port_d_2 = port_m_d_2 ;
	}
	public void set_ip_m_d_1(String ip_m_d_1){
		this.ip_m_d_1 = ip_m_d_1 ; 
	}
	public void set_port_m_d_1(int port_m_d_1){
		this.port_d_1 = port_m_d_1 ;
	}
	
	public boolean ip_veri(String ip){
		String mor[] = ip.split("\\.");
		if(mor.length != 4){
			return false ;
		}
		for (int i = 0; i < 4; i++) {
			if(mor[1].length() != 3){
				return false ;
			}
		}
		return true ;
	}
	public boolean port_veri(String port){
		return port.length() == 4 ;
	}
	

}
