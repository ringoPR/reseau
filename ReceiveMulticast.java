
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class ReceiveMulticast implements Runnable{

	public int port_diff ;
	public String ip_diff ;
	
	public MulticastSocket recoi ;
	public byte[]data ;
	DatagramPacket paquet ;
	
	
	public ReceiveMulticast(int port_diff , String ip_diff){
		this.port_diff = port_diff ;
		this.ip_diff = ip_diff ;
		this.data = new byte[512];
		try {
			this.paquet = new DatagramPacket(data, data.length);
			this.recoi = new MulticastSocket(this.port_diff);
			this.recoi.joinGroup(InetAddress.getByName(this.ip_diff));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			System.out.println("dans le receive multicast");
			try{
				this.recoi.receive(this.paquet);
				String msg_recu = new String(this.paquet.getData(), 0, this.paquet.getLength());
				System.out.println("j'ai recu "+msg_recu);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}



}
