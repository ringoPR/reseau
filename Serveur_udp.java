


public class Serveur_udp implements Runnable {



	private Bufferconcurrent bufferConcurrent ;
	


	public Serveur_udp(String identifiant,String adr_entite, int port_udp ,int port_svt,String add_svt,Bufferconcurrent bufferConcurrent) {

			this.bufferConcurrent = bufferConcurrent;
	}

	@Override
	public void run() {
		
		while (true) {		
			try {
			

				if(this.bufferConcurrent.deconnecter){
					break ;
				}
				
				
				this.bufferConcurrent.serveur_recoi.receive(this.bufferConcurrent.paquer_recoi);
				String msg_recu = new String(this.bufferConcurrent.paquer_recoi.getData(),0,this.bufferConcurrent.paquer_recoi.getLength());
				System.out.println("msg recu  = "+msg_recu);
				
			
				
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
				if(msg_recu.split("\\s")[0].equals("EYBG")){
					msg_EYBG(msg_recu);
				}
				if(msg_recu.split("\\s")[0].equals("PROTOCOLE")){
					msg_protocole(msg_recu);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
	
	private void msg_EYBG(String msg_recu){
		this.bufferConcurrent.close();
	}
	
	private void msg_GBYE(String msg_recu){
		
		if(msg_recu.split("\\s")[2].equals(this.bufferConcurrent.ip_svt1) && (Integer.parseInt(msg_recu.split("\\s")[3])== this.bufferConcurrent.port_udp_svt1) ){
			String msg_envoi ="EYBG "+msg_recu.split("\\s")[1]; 
			envoi_entite_suivant(msg_envoi);
			this.bufferConcurrent.set_change_connexion(Integer.parseInt(msg_recu.split("\\s")[5]), msg_recu.split("\\s")[4]);
			
			
		}else{
			envoi_entite_suivant(msg_recu);
		}
	}
	
	
	private void msg_WHOS(String msg){
		try{
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null){
				envoi_entite_suivant(msg);
				return ;
			}
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null){
				String chaine = "MEMB "+msg.split("\\s")[1]+" "+this.bufferConcurrent.identifiant+" "+this.bufferConcurrent.ip_entiter+" "+this.bufferConcurrent.port_entite;
				envoi_entite_suivant(chaine);
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
//				System.out.println("je suis dans l entité qui a envoyé le teste ");
				this.bufferConcurrent.envoi_test = false ;
				this.bufferConcurrent.recu_test = true ; 
//				this.bufferConcurrent.initialisation_temps();
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
			this.bufferConcurrent.data_envoi = msg.getBytes() ;
			this.bufferConcurrent.paquer_envoi.setData(bufferConcurrent.data_envoi);
			this.bufferConcurrent.serveur_envoi.send(this.bufferConcurrent.paquer_envoi);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void msg_protocole(String msg){
		try{
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null){
				
				return ;
			}
			envoi_entite_suivant(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	

	
}
