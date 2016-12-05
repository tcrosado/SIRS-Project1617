/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Jo�o
 *
 */
public class AccountMatrixData extends AbstractData {

	public boolean VerifyMatrixValue(String iban, String row, int column, int value) throws SQLException{

		boolean result = false;

		java.sql.PreparedStatement returnedValue = conn.prepareStatement("SELECT value FROM accountmatrix WHERE iban = ? AND row = ? AND column = ?");

		returnedValue.setString(1, iban);
		returnedValue.setString(2, row);
		returnedValue.setInt(3, column);

		ResultSet rs = returnedValue.executeQuery();

		while(rs.next()){
			if(rs.getInt("value") == value)
				result = true;
		}

		return result;	
	}

	public String getRowRequest(String iban) throws SQLException{

		String result;
		ArrayList<String> queryResult = new ArrayList<String>();

		java.sql.PreparedStatement returnedValue = conn.prepareStatement("SELECT row FROM accountmatrix WHERE iban = ?");
		
		returnedValue.setString(1, iban);
		
		ResultSet rs = returnedValue.executeQuery();

		while(rs.next()){
			queryResult.add(rs.getString("row"));
		}
		
		Random r = new Random();
		int index = r.nextInt(queryResult.size() - 1);
		
		result = queryResult.get(index);
		
		return result;

	}
	
	public int getColumnRequest(String iban) throws SQLException{

		int result;
		ArrayList<Integer> queryResult = new ArrayList<Integer>();

		java.sql.PreparedStatement returnedValue = conn.prepareStatement("SELECT `column` FROM accountmatrix WHERE iban = ?");
		
		returnedValue.setString(1, iban);
		
		ResultSet rs = returnedValue.executeQuery();

		while(rs.next()){
			queryResult.add(rs.getInt("column"));
		}
		
		Random r = new Random();
		int index = r.nextInt(queryResult.size() - 1);
		
		result = queryResult.get(index);
		
		return result;

	}

	public int getRandomDigitPosition(){
		Random r = new Random();
		return r.nextInt(3)+1;
	}

	private int getNthDigit(int nth,int number){
		Double digit;
		digit = Math.pow(10,nth-1);
		digit = (number/digit) % 10;
		return digit.intValue();
	}
}
