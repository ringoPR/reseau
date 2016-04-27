
public class TEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long debut = System.currentTimeMillis();
		
		for (int i = 0; i < 2750108; i++) {
			System.out.println(i);
		}
		long fin = System.currentTimeMillis();
		System.out.println(fin-debut);

	}

}
