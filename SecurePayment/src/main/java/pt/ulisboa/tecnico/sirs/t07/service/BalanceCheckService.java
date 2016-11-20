/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.service;

import java.util.Vector;

import pt.ulisboa.tecnico.sirs.t07.data.AccountData;

/**
 * @author João
 *
 */
public class BalanceCheckService extends OperationService{

	private String IBAN;
	public Vector<Float> balance = null;
	
	public BalanceCheckService(String iban) {
		super(iban);
		this.IBAN = iban;
	}

	@Override
	void dispatch() {
		AccountData Account = new AccountData();
		this.balance = Account.getBalanceFromIBAN(this.IBAN);
		return;
	}

}
