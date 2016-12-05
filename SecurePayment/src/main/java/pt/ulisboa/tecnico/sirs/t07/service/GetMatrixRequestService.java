/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.service;

import java.sql.SQLException;

import pt.ulisboa.tecnico.sirs.t07.data.AccountMatrixData;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;

/**
 * @author Jo�o
 *
 */
public class GetMatrixRequestService extends OperationService{
	
	private String iban;
	private String row;
	private int column;
	private String result;
	
	
	public GetMatrixRequestService(String iban) {
		super();
		this.iban = iban;
	}

	@Override
	void dispatch() throws ErrorMessageException {
		
		AccountMatrixData matrix = new AccountMatrixData();
		
		try {
			this.row = matrix.getRowRequest(iban);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			this.column = matrix.getColumnRequest(iban);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.result = this.row + "-" + this.column + "-" + matrix.getRandomDigitPosition();
		
	}

	@Override
	public String result() {
		return this.result;
	}

}
