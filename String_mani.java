public class String_mani {

	private String msg;
	private int port;
	private int port_dupl;
	private String adresse_dupl;
	private int port_diff;
	private String adresse;
	private String adresse_diff;

	public String_mani(String msg, int nb, boolean isduplicate) {
		if (!isduplicate) {
			this.msg = msg;
			this.port = port();
			this.adresse = adresse();
			if (nb == 4) {
				this.port_diff = port_diff();
				this.adresse_diff = adresse_diff();
			}
		} else {
			this.msg = msg;
			this.port_dupl = port_dupl();
		}
	}

	public void set_msg(String chaine) {
		this.msg = chaine;
	}

	public void set_port(int port) {
		this.port = port;
	}

	public int get_port() {
		return this.port;
	}

	public String get_address() {
		return this.adresse;
	}

	public int get_port_diff() {
		return this.port_diff;
	}

	public String get_address_diff() {
		return this.adresse_diff;
	}

	public String get_protocol_msg() {
		return this.msg.split("\\s")[0];
	}

	private int port() {
		return Integer.parseInt(this.msg.split("\\s")[2]);
	}

	private String adresse() {
		return this.msg.split("\\s")[1];
	}

	private String adresse_diff() {
		return this.msg.split("\\s")[3];
	}

	private int port_diff() {
		return Integer.parseInt(this.msg.split("\\s")[4]);
	}

	private int port_dupl() {
		return Integer.parseInt(this.msg.split("\\s")[1]);
	}

	public String get_address_dupl() {
		return this.adresse_dupl;
	}

	public int get_port_dupl() {
		return this.port_dupl;
	}
}

