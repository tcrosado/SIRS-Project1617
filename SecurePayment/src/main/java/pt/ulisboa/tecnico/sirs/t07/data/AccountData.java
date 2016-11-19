/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Jo�o
 *
 */
public class AccountData extends AbstractData{
	//
	//	/**
	//	 * 
	//	 */
	//


	public Vector<Float> getBalanceFromIBAN(String IBAN){

		Vector<Float> result = new Vector<Float>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM accounts WHERE iban = ?");
			stmt.setString(1,IBAN);
			ResultSet rs = stmt.executeQuery();


			while (rs.next()){
				result.add(rs.getFloat("balance"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public void updateBalance(String iban,float balance) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("UPDATE accounts SET balance=? WHERE iban= ?;");
		stmt.setFloat(1,balance);
		stmt.setString(2,iban);
		stmt.executeQuery();
	}
	
	
	public List<Account> getAccounts(){
		
		List<Account> result = new ArrayList<Account>();
		Account aux;
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts");
			
			ResultSet rs = stmt.executeQuery();


			while (rs.next()){
				aux = new Account(rs.getString("iban"),rs.getFloat("balance"));
				result.add(aux);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
		
	}




}