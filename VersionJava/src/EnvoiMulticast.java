import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;


public class EnvoiMulticast implements Runnable {
		

	public int port_dif;
	public String ip_diff;
	
	public DatagramSocket envoi;
	public DatagramPacket paquer;
	public InetSocketAddress inetSocketAddress;
	public byte[] data;
	private final String msg = "DOWN";
	
	
	
	
	public  EnvoiMulticast(int port_diff,String ip_diff ){
			
			this.port_dif = port_diff ;
			this.ip_diff = ip_diff ;
			this.inetSocketAddress = new InetSocketAddress(this.ip_diff, this.port_dif);
			this.data = new byte[512] ; 
			try {
				this.envoi = new DatagramSocket() ;
				this.paquer = new DatagramPacket(data, data.length,inetSocketAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void run() {

				try{
					
					this.data = this.msg.getBytes();
					this.paquer.setData(data);
					this.envoi.send(this.paquer);

				}catch(Exception e){
					e.printStackTrace() ;
				}
	}

}
