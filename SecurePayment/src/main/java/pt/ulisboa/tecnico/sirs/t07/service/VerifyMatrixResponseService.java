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
public class VerifyMatrixResponseService extends OperationService{
	
	private String iban;
	private String row;
	private int column;
	private int value;
	private int position;
	private String result;
	
	
	
	public VerifyMatrixResponseService(String iban, String row, int column,int position, int value) {
		super();
		this.iban = iban;
		this.row = row;
		this.column = column;
		this.value = value;
		this.position = position;
	}

	@Override
	void dispatch() throws ErrorMessageException {
		AccountMatrixData matrix = new AccountMatrixData();
		
		try {
			Boolean res = matrix.verifyMatrixValue(this.iban, this.row, this.column,this.position, this.value);
			result = "" + res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String result() {
		return this.result;
	}

}
