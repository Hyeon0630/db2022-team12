package DB2022Team12;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

class Musical {
	
	// ������ ��¥ ������ �����ϴ� ��: <���� ��¥, ���� ��¥�� ���� ���� �ð����� �����ϴ� ����Ʈ>
	private HashMap<String, Vector<String>> dateInfo;
	
	private String title, summary, price, score;
	private int remainSeat;
	private String theaterName, theaterAddress, theaterPhone, theaterSize;

	// ������ ���� ������ ��� �������� ���� (������ ���� + ��� ���� ���� + �� ���� ����)
	// ������ ���̺�� ��� ���� �並 NATURAL LEFT JOIN �� ��, �̸� ���� ���̺�� JOIN 
	private final static String GET_MUSICAL_QUERY = "SELECT * "
			+ "FROM (musical NATURAL LEFT JOIN avg_rate) "
			+ "JOIN theater ON musical.theater_name = theater.name "
			+ "WHERE musical.title = ?";
	
	// ������ ��¥ ������ �������� ����
	private final static String GET_MUSICAL_DATE_QUERY = "SELECT * "
			+ "FROM musical NATURAL JOIN musical_date "
			+ "WHERE title = ?";
	
	public Musical(String musical) {
		try (
			Connection conn = new ConnectionClass().getConnection();
			PreparedStatement musicalStmt = conn.prepareStatement(GET_MUSICAL_QUERY);
			PreparedStatement dateStmt = conn.prepareStatement(GET_MUSICAL_DATE_QUERY);
		) {
			// ������ ���� ��������
			musicalStmt.setString(1, musical);
			ResultSet rs = musicalStmt.executeQuery();
			
			// ������ ���� ����
			rs.next();
			this.title = rs.getString("title");
			this.price = rs.getString("price");
			this.remainSeat = rs.getInt("remain_seat");
			this.summary = rs.getString("summary");
			this.score = rs.getString("score");
			this.theaterName = rs.getString("theater_name");
			this.theaterAddress = rs.getString("address");
			this.theaterPhone = rs.getString("phone");
			this.theaterSize = rs.getString("size");

			// ������ ��¥ ���� ��������
			dateStmt.setString(1, musical);
			rs = dateStmt.executeQuery();
			
			// ������ ��¥ ���� ����
			dateInfo = new HashMap<>();
			String date, time;
			while (rs.next()) {
				date = rs.getString("date");
				time = rs.getString("time");
				
				// <key: date, value: time�� �����ϴ� Vector> �������� ����
				Vector<String> v = dateInfo.getOrDefault(date, new Vector<String>());
				v.add(time);
				dateInfo.put(date, v);
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
		}
	}
  
	Vector<String> getDateVector() {
		Vector<String> dateVector = new Vector<>();
		for (String date: dateInfo.keySet())
			dateVector.add(date);
		return dateVector;
	}
	
	Vector<String> getTimeVector(String date) {
		return dateInfo.get(date);
	}

	String getTitle() {
		return title;
	}

	String getSummary() {
		return summary;
	}

	String getPrice() {
		return price;
	}

	String getScore() {
		return score;
	}

	int getRemainSeat() {
		return remainSeat;
	}

	String getTheaterName() {
		return theaterName;
	}

	String getTheaterAddress() {
		return theaterAddress;
	}

	String getTheaterPhone() {
		return theaterPhone;
	}

	String getTheaterSize() {
		return theaterSize;
	}
}