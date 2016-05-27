
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;




public class Serveur_udp implements Runnable {

	private Bufferconcurrent bufferConcurrent ;
	private static byte arrayfile []  ;


	public Serveur_udp(Bufferconcurrent bufferConcurrent) {
			this.bufferConcurrent = bufferConcurrent;
	}

	@Override
	public void run() {

		while (true) {
			try {

				if(this.bufferConcurrent.deconnecter){
					break ;
				}

				this.bufferConcurrent.serveur_recoi_1.receive(this.bufferConcurrent.paquer_recoi_1);
				String msg_recu = new String(this.bufferConcurrent.paquer_recoi_1.getData(),0,this.bufferConcurrent.paquer_recoi_1.getLength());


				System.out.println("msg recu  = "+msg_recu);


				if(msg_recu.split("\\s")[0].equals("WMSG")){
					msg_WHOSMSG(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("TEST")){
					msg_TEST(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("WHOS")){
					msg_WHOS(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("MEMB")){
					msg_MEMB(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("MMSG")){
					msg_MEMBMSG(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("GBYE")){
					msg_GBYE(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("EYBG")){
					msg_EYBG(msg_recu);
				}
				else if(msg_recu.split("\\s")[0].equals("APPL")){
					if(msg_recu.split("\\s")[2].equals("DIFF####")){
						msg_APPL_DIFF(msg_recu);
					}else if(msg_recu.split("\\s")[2].equals("TRANS###") && msg_recu.split("\\s")[3].equals("REQ") ){
						 msg_REQ(msg_recu);
					}else if(msg_recu.split("\\s")[2].equals("TRANS###") && msg_recu.split("\\s")[3].equals("ROK")){
						recu_ROK(msg_recu);
					}else if(msg_recu.split("\\s")[2].equals("TRANS###") && msg_recu.split("\\s")[3].equals("SEN")){
							recu_SEN(msg_recu);
					}else{
						System.out.println("erreur de protocole de msg");
					}
				}else{
					System.out.println("erreur de protocole de msg");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void msg_WHOS(String msg){

		if(this.bufferConcurrent.verification_envoi_recu(msg)){
			if(this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) == null ){
				this.bufferConcurrent.put_msg_envoyer_anneau(msg.split("\\s")[1]);
				envoi_entite_suivant(msg);

				try {
					Thread.sleep(10);
					Random rand = new Random();
					int id = rand.nextInt(100000000) ;
					String chaine = "MEMB "+String_mani.codageIdMsg(String.valueOf(id))+" "+this.bufferConcurrent.identifiant+" "+this.bufferConcurrent.ip_entiter+" "+this.bufferConcurrent.port_entite;
					this.bufferConcurrent.put_msg_envoyer_anneau(chaine.split("\\s")[1]);
					this.bufferConcurrent.put_msg_envoyer_entiter(chaine.split("\\s")[1],"MEMB");
					envoi_entite_suivant(chaine);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("mauvais codage");
		}
	}

	private void msg_WHOSMSG(String msg){


		if(this.bufferConcurrent.verification_envoi_recu(msg)){
		try{


			if(this.bufferConcurrent.get_msg_envoyer_whosmsg(msg.split("\\s")[1]) != null){
				return ;
			}

			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null ){
				envoi_entite_suivant(msg);
				this.bufferConcurrent.put_msg_envoyer_whosmsg(msg.split("\\s")[1]);
				this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1], msg.split("\\s")[0]);
				return ;
			}

			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null){

				Random rand = new Random();
				int id = rand.nextInt(100000000) ;
				String chaine = "MMSG "+String_mani.codageIdMsg(String.valueOf(id))+" "+this.bufferConcurrent.identifiant+" "+this.bufferConcurrent.ip_entiter+" "+this.bufferConcurrent.port_entite;
				this.bufferConcurrent.put_msg_envoyer_whosmsg(msg.split("\\s")[1]);
				envoi_entite_suivant(msg);

				envoi_entite_suivant(chaine);
				this.bufferConcurrent.put_msg_envoyer_memb(msg.split("\\s")[1]);
				this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1], msg.split("\\s")[0]);
			}
		}catch(Exception e){
			e.printStackTrace();
			}
		}
	}



	private void msg_MEMB(String msg){

		if(this.bufferConcurrent.get_msg_envoyer_memb(msg.split("\\s")[1])== null && this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1])==null && this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) == null){
			envoi_entite_suivant(msg);
		}
		this.bufferConcurrent.put_msg_envoyer_memb(msg.split("\\s")[1]);
		this.bufferConcurrent.put_msg_envoyer_entiter(msg.split("\\s")[1], "WHOS");

	}

	private void msg_REQ(String msg){

		if(!this.bufferConcurrent.envoi_REQ){
			if(this.bufferConcurrent.verification_envoi_recu(msg)){
				if(in_shared(msg.split("\\s")[5])){
					envoi_ROK(msg);
				}else{
					envoi_entite_suivant(msg);
				}
		  }
		}else{
			this.bufferConcurrent.envoi_REQ = false ;
		}
	}

	private void envoi_ROK(String msg){

		Random r = new Random();
		int ale = r.nextInt(100000000);
		String msg_rok = "APPL"+" "+ale+" TRANS### ROK "+r.nextInt(100000000)+" "+msg.split("\\s")[4]+" "+msg.split("\\s")[5];
		int nummess = taille_fichier(msg.split("\\s")[5]);
		msg_rok +=" "+nummess ;
		this.bufferConcurrent.envoi_SEN = false ;
		this.bufferConcurrent.envoi_ROK = true ;

		envoi_entite_suivant(msg_rok);

	}

	private void recu_ROK(String msg){
		if(this.bufferConcurrent.envoi_ROK){

			int taille_nummess = Integer.parseInt(msg.split("\\s")[7]);
			Random rand = new Random();
			int id = rand.nextInt(100000000) ;

			String msg_sen="APPL "+String_mani.codageIdMsg(String.valueOf(id))+" TRANS### SEN" ;
			this.bufferConcurrent.envoi_SEN = true ;

			int compte = 0 ;
			for (int i = 0; i < taille_nummess; i++) {
				String morceau = new String (Arrays.copyOfRange(arrayfile, compte, compte+463));
				compte = compte + 463 + 1 ;
				byte morceau_byte[] = morceau.getBytes();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				envoi_entite_suivant(msg_sen+" "+rand.nextInt(100000000)+" "+i+" "+morceau_byte.length+" "+morceau);
			}

		}else{
			envoi_entite_suivant(msg);
		}
	}

	private void recu_SEN(String msg){
		if(!this.bufferConcurrent.envoi_SEN ){
			if(this.bufferConcurrent.envoi_REQ){

				try {
					FileOutputStream fos = new FileOutputStream("/home/kira/workspace/projet_reseau_modifier2/shared/"+this.bufferConcurrent.name_fichier,true);
					byte conten [] = msg.split("\\s",8)[7].getBytes();
						fos.write(conten);
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			envoi_entite_suivant(msg);
		}else{
			this.bufferConcurrent.envoi_ROK = false ;
		}
	}
	private int taille_fichier(String file){
		Path path = Paths.get("/home/kira/workspace/projet_reseau_modifier2/shared/"+file);
		int nummess = -1 ;
		try{
			arrayfile =  Files.readAllBytes(path);
		    nummess = arrayfile.length/463 +1  ;
		}catch(Exception e){
			System.out.println(e.toString());
			return -1 ;
		}
		return nummess ;
	}

	private boolean in_shared(String file){

		File fichiers = new File("/home/kira/workspace/projet_reseau_modifier2/shared/");
		String list[] = fichiers.list();
		for (int i = 0; i < list.length; i++) {
			if(list[i].equals(file)){
				return true ;
			}
		}
		return false ;
	}



	private void msg_EYBG(String msg_recu){
		this.bufferConcurrent.close();
		if(!this.bufferConcurrent.is_duplicate){
			System.exit(0);
		}
	}

	private void msg_GBYE(String msg_recu){
		if(this.bufferConcurrent.verification_envoi_recu(msg_recu) ){

			if(msg_recu.split("\\s")[2].equals(String_mani.codageIp(this.bufferConcurrent.ip_svt1)) &&
				(msg_recu.split("\\s")[3].equals(String_mani.codagePort(this.bufferConcurrent.port_udp_svt_1))) ){
					String msg_envoi ="EYBG "+msg_recu.split("\\s")[1];
					envoi_entite_suivant(msg_envoi);
					this.bufferConcurrent.set_change_connexion_1(Integer.parseInt(msg_recu.split("\\s")[5]), msg_recu.split("\\s")[4]);
			}else{
				if(this.bufferConcurrent.get_msg_envoyer_anneau(msg_recu.split("\\s")[1]) == null){
					envoi_entite_suivant(msg_recu);
					this.bufferConcurrent.put_msg_envoyer_anneau(msg_recu.split("\\s")[1]);
				}
			}
		}

	}






	private void msg_MEMBMSG(String msg){
		if(this.bufferConcurrent.verification_envoi_recu(msg)){
			if(this.bufferConcurrent.get_msg_envoyer_memb(msg.split("\\s")[1]) !=null){
				return ;
			}
				envoi_entite_suivant(msg);
				this.bufferConcurrent.put_msg_envoyer_memb(msg.split("\\s")[1]);
			}
	}




	private void msg_TEST(String msg){
		if(this.bufferConcurrent.verification_envoi_recu(msg)){

			try{
				if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null || this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) != null){
					this.bufferConcurrent.envoi_test = false ;
					this.bufferConcurrent.recu_test = true ;
					return ;
				}

				if(this.bufferConcurrent.verification_test_ip_port(msg)){
					envoi_entite_suivant(msg);
				}

		}catch(Exception e){
			e.printStackTrace();
		}
		}
	}

	private void msg_APPL_DIFF(String msg){

		try{
			if(this.bufferConcurrent.get_msg_envoyer_entiter(msg.split("\\s")[1]) != null || this.bufferConcurrent.get_msg_envoyer_anneau(msg.split("\\s")[1]) != null){
				return ;
			}
			envoi_entite_suivant(msg);
		}catch(Exception e){
			e.printStackTrace();
		}

	}



	private void envoi_entite_suivant(String msg){
		try{

			if(!this.bufferConcurrent.is_deconnecte_anneau1){
				this.bufferConcurrent.data_envoi_1 = msg.getBytes() ;
				this.bufferConcurrent.paquer_envoi_1.setData(bufferConcurrent.data_envoi_1);
				this.bufferConcurrent.serveur_envoi_1.send(this.bufferConcurrent.paquer_envoi_1);
			}

			if(this.bufferConcurrent.is_duplicate){
				if(!this.bufferConcurrent.is_deconnecte_anneau2){
					this.bufferConcurrent.data_envoi_2 = msg.getBytes() ;
					this.bufferConcurrent.paquer_envoi_2.setData(bufferConcurrent.data_envoi_2);
					this.bufferConcurrent.serveur_envoi_2.send(this.bufferConcurrent.paquer_envoi_2);
				}
			}

			this.bufferConcurrent.put_msg_envoyer_anneau(msg.split("\\s")[1]);

		}catch(Exception e){
			e.printStackTrace();
		}
	}






}
