import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


public class Securite {

	public Securite(){
		
	}
	
	public Key create_key_public(String type_key){
		
		
		KeyPair keyPair = key_paire();
		if(type_key.equals("public")){
			return keyPair.getPublic() ;
		}else if(type_key.equals("private")){
			return keyPair.getPrivate();
		}
		return null ;
	}
	
	private KeyPair key_paire(){
		KeyPair keyPair = null ;
		try{
			KeyPairGenerator keygenerate = KeyPairGenerator.getInstance("rsa");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keygenerate.initialize(1024, random);
			keyPair = keygenerate.generateKeyPair();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return keyPair ;
	}
	
	public  void ecire(String namefile, Object obj) {
		try {
			FileOutputStream file = new FileOutputStream(namefile);// je dois modifier
			ObjectOutputStream objout = new ObjectOutputStream(file);
			objout.writeObject(obj);
			objout.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	public  Object readObject(String filename) {

		Object obj = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(filename);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			obj = objectInputStream.readObject();
			objectInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return obj;

	}

}
