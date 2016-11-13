/**
 * 
 */
package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author João
 *
 */
public abstract class ContaData extends AbstractData{
	//
	//	/**
	//	 * 
	//	 */
	//


	public Vector<Float> getSaldoFromIBAN(String IBAN){

		Vector<Float> result = new Vector<Float>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM conta WHERE iban = ?");
			stmt.setString(1,IBAN);
			ResultSet rs = stmt.executeQuery();


			while (rs.next()){
				result.add(rs.getFloat("saldo"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}


}