/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

/**
 * @author João
 *
 */
public class AccountMatrix {
	
	private String iban;
	private String row;
	private int column;
	private int value;

	public AccountMatrix(String nib, String r, int c, int v) {
		this.iban = nib;
		this.row = r;
		this.column = c;
		this.value = v;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
