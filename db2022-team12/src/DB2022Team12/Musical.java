package DB2022Team12;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Musical {
	
	private class DateInfo {
		String id, date, time;
	}
	
	private ArrayList<DateInfo> dateInfoList;
	private String[] id, date, time;
	private String title, summary, price, avgRate;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;

	// ������ ���� ������ ��� �������� ���� (������ ���� + ��� ���� ���� + �� ���� ����)
	// ������ ���̺�� ��� ���� �並 NATURAL LEFT JOIN �� ��, �̸� ���� ���̺�� JOIN 
	private final String GET_MUSICAL_QUERY = "SELECT * "
			+ "FROM (musical NATURAL LEFT JOIN avg_rate) "
			+ "JOIN theater ON musical.theater_name = theater.name "
			+ "WHERE musical.title = ?";
	
	public Musical(String musical) {
		try (
			Connection conn = new ConnectionClass().getConnection();
			PreparedStatement pStmt = conn.prepareStatement(GET_MUSICAL_QUERY);
		) {
			pStmt.setString(1, musical);
			ResultSet rs = pStmt.executeQuery();
			
			// ù ��° Ʃ�ÿ��� ���� ����(����, ������, ����, �ٰŸ�) ����
			rs.next();
			this.title = rs.getString("title");
			this.theaterName = rs.getString("theater_name");
			this.price = rs.getString("price");
			this.summary = rs.getString("summary");
			
			System.out.println(price);
			System.out.println(rs.getString("date"));
			
			while (rs.next()) {
				System.out.println("begin");
				
			}
			System.out.println("end");
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
	}
}