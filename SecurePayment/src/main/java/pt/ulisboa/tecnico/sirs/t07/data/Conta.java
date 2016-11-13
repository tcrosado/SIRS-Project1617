/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

/**
 * @author João
 *
 */
public class Conta {

	String IBAN;
	Float saldo;
	
	public Conta(String iBAN, Float s) {
		super();
		IBAN = iBAN;
		this.saldo = s;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public Float getSaldo() {
		return saldo;
	}

	public void setSaldo(Float saldo) {
		this.saldo = saldo;
	}
	
	public String toString(){
		return "IBAN - " + this.IBAN + " | " + "Saldo - " + this.saldo; 
	}
	
}
