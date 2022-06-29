package it.polito.tdp.crimes.db;

public class Arco implements Comparable<Arco>{

	private String offense_type_id_1;
	private String offense_type_id_2;
	int peso;
	
	public Arco(String offense_type_id_1, String offense_type_id_2, int peso) {
		super();
		this.offense_type_id_1 = offense_type_id_1;
		this.offense_type_id_2 = offense_type_id_2;
		this.peso = peso;
	}
	
	
	
	@Override
	public String toString() {
		return offense_type_id_1 + "  -  " + offense_type_id_2 + "  :  " + peso + "\n";
	}



	public String getOffense_type_id_1() {
		return offense_type_id_1;
	}
	public void setOffense_type_id_1(String offense_type_id_1) {
		this.offense_type_id_1 = offense_type_id_1;
	}
	public String getOffense_type_id_2() {
		return offense_type_id_2;
	}
	public void setOffense_type_id_2(String offense_type_id_2) {
		this.offense_type_id_2 = offense_type_id_2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}



	@Override
	public int compareTo(Arco o) {
		return this.peso-o.getPeso();
	}

}
