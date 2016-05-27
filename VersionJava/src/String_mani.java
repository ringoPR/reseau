public class String_mani {



	public String_mani() {

	}
	public static String code_id (String msg){
		String tout [] =msg.split("\\s") ;
		String tout_bis="";
		tout[1] = String_mani.codageIdMsg(tout[1]);
		for (int i = 0; i < tout.length; i++) {
			if(i != tout.length - 1 ){
			tout_bis+=tout[i]+" " ;
			}else{
				tout_bis+=tout[i] ;
			}
		}
		return tout_bis ;
	}

	public static  String codageIp(String ip){
		String array[] =ip.split("\\.");
		String ip_bis ="";

		for (int i = 0; i < array.length; i++) {
			int taille = array[i].length();
			for (int j = 0; j < 3-taille; j++) {
				array[i] = "0"+array[i];
			}
		}
		for (int i = 0; i < array.length; i++) {
			if(i != array.length -1)
				ip_bis += array[i]+"." ;
			else
				ip_bis+= array[i] ;
		}
		return ip_bis ;
	}

	public static  String codagePort(int port){
		String port_bis = ""+port ;
		int size = port_bis.length();

		for (int i = 0; i < 4-size; i++) {
			port_bis = "0"+port_bis;
		}
		return port_bis ;
	}

	public static  String codageIdMsg(String id){
		int size  = id.length();
		String id_bis = id ;
		for (int i = 0; i < 8-size; i++) {
			id_bis = "0"+id_bis ;
		}
		return id_bis ;
	}

	public static String  TroisOctet(int size_content){
		String tmp = ""+size_content;
		int size = tmp.length();
		for (int i = 0; i < 3-size; i++) {
			tmp = "0"+tmp ;
		}
		return tmp ;
	}
}
