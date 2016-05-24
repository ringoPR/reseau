import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.Socket;

public class Client_tcp {

	private int port;
	private String address;
	private BufferedReader br_client;
	private PrintWriter pr_client;
	private Socket client;

	public Client_tcp(int port, String address) {
		this.port = port;
		this.address = address;
		connect_client();
		creat_buff_client();
		creat_PrintWriter_client();
	}

	public void connect_client() {
		try {
			this.client = new Socket(this.address, this.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void creat_buff_client() {
		try {
			this.br_client = new BufferedReader(new InputStreamReader(
					this.client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void creat_PrintWriter_client() {
		try {
			this.pr_client = new PrintWriter(new OutputStreamWriter(
					client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void creat_PrintWriter_serveur() {
		try {
			this.pr_client = new PrintWriter(new OutputStreamWriter(
					this.client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readligne_client() throws Exception {

		if (client != null && this.br_client != null) {
			String recu = this.br_client.readLine();
			return recu;
		} else {
			throw new Exception(
					"le client n'est pas initialser ou bien sont buff");
		}
	}

	public void write_client(String msg) throws Exception {
		if (msg != null) {
			this.pr_client.write(msg);
			this.pr_client.flush();
		} else {
			throw new Exception("pas de msg");
		}
	}

	public void close_br_client() {

		try {
			this.br_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close_client() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
