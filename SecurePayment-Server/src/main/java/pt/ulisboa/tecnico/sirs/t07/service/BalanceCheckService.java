/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.service;

import java.util.Vector;

import pt.ulisboa.tecnico.sirs.t07.data.AccountData;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidIbanException;

/**
 * @author Jo�o
 *
 */
public class BalanceCheckService extends OperationService{

	private String IBAN;
	public Vector<Integer> balance = null;
	
	public BalanceCheckService(String iban) {
		super(iban);
		this.IBAN = iban;
	}

	@Override
	void dispatch() throws ErrorMessageException {
		AccountData Account = new AccountData();
		Vector<Integer> balance = Account.getBalanceFromIBAN(this.IBAN);
		if(balance.isEmpty())
			throw new InvalidIbanException(this.IBAN);
		this.balance = balance;
		return;
	}

	@Override
	public String result() {
		return "B"+this.balance.firstElement()+"$";
	}

}
