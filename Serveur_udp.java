

import java.net.InetSocketAddress;



public class Serveur_udp implements Runnable {


	private String adr_entiter ;
	private String ip_svt ;
	
	private byte data_recoi[];
	private byte data_envoi[];

	private Bufferconcurrent bufferConcurrent ;
	private boolean change_connexion = false ;


	public Serveur_udp(String identifiant,String adr_entite, int port_udp ,int port_svt,String add_svt,Bufferconcurrent bufferConcurrent) {

			this.bufferConcurrent = bufferConcurrent;
		
			

	}

	@Override
	public void run() {
		
		while (true) {		
			try {
				
				
				this.bufferConcurrent.serveur_recoi.receive(this.bufferConcurrent.paquer_recoi);
				System.out.println(this.bufferConcurrent.paquer_recoi.getData());
				String msg_recu = new String(this.bufferConcurrent.paquer_recoi.getData(),0,this.bufferConcurrent.paquer_recoi.getLength());
				System.out.println("msg recu  = "+msg_recu);
				
				if(change_connexion){
					this.bufferConcurrent.inetsocketAddress = new InetSocketAddress(ip_svt, this.bufferConcurrent.port_udp_svt1);
					this.bufferConcurrent.paquer_envoi.setSocketAddress(this.bufferConcurrent.inetsocketAddress);
					this.change_connexion = false ;
				}
				
				if(msg_recu.split("\\s")[0].equals("WHOS")){
					msg_WHOS(msg_recu);
				}
				if(msg_recu.split("\\s")[0].equals("TEST")){
					msg_TEST(msg_recu);
				}
				if(msg_recu.split("\\s")[0].equals("MEMB")){
					msg_MEMB(msg_recu);
				}
			
				if(msg_recu.split("\\s")[0].equals("GBYE")){
					msg_GBYE(msg_recu);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
	
	private void msg_GBYE(String msg_recu){
		if(msg_recu.split("\\s")[2].equals(this.ip_svt) && (Integer.parseInt(msg_recu.split("\\s")[3])== this.bufferConcurrent.port_udp_svt1) ){
			this.ip_svt = msg_recu.split("\\s")[4];
			this.bufferConcurrent.port_udp_svt1 = Integer.parseInt(msg_recu.split("\\s")[5]);
			this.change_connexion = true ;
			String msgAenvoyer = "EYBG "+msg_recu.split("\\s")[1];
			envoi_entite_suivant(msgAenvoyer);
			
		}else{
			envoi_entite_suivant(msg_recu);
			this.bufferConcurrent.put_msg_envoyer_anneau(msg_recu.split("\\s")[1]);
		}
	}
	
	
	private void msg_WHOS(String msg){
		try{
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null){
				envoi_entite_suivant(msg);
				return ;
			}
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null){
				String chaine = "MEMB "+msg.split("\\s")[1]+" "+this.bufferConcurrent.identifiant+" "+this.bufferConcurrent.port_entite+" "+this.adr_entiter;
				this.data_envoi = chaine.getBytes();
				this.bufferConcurrent.paquer_envoi.setData(data_envoi);
				this.bufferConcurrent.serveur_envoi.send(this.bufferConcurrent.paquer_envoi);
				this.bufferConcurrent.put_msg_envoyer_anneau(msg.split("\\s")[1]);
				this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1], msg.split("\\s")[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	
	
	
	private void msg_TEST(String msg){
		try{
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null){
//				envoi_entite_suivant(msg);
				return ;
			}
			envoi_entite_suivant(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void msg_MEMB(String msg){
		if(this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) !=null){
			return ;
		}
		envoi_entite_suivant(msg);
		this.bufferConcurrent.put_msg_envoyer_anneau(msg.split("\\s")[1]);
	}
	

	
	private void envoi_entite_suivant(String msg){
		try{
			this.data_envoi = msg.getBytes();
			this.bufferConcurrent.paquer_envoi.setData(data_envoi);
			this.bufferConcurrent.serveur_envoi.send(this.bufferConcurrent.paquer_envoi);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	

	
}
