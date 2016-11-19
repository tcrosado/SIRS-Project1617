/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

/**
 * @author Jo�o
 *
 */
public class Account {

	String IBAN;
	Float balance;
	
	public Account(String iBAN, Float s) {
		super();
		IBAN = iBAN;
		this.balance = s;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}
	
	public String toString(){
		return "IBAN - " + this.IBAN + " | " + "Saldo - " + this.balance;
	}
	
}
