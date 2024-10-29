import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class ConnectionUtils {
	
	public static class Row extends ArrayList{
		public Integer getint(int i) {
			return Integer.parseInt(this.get(i).toString());
		}
		
		public String getStr(int i) {
			return this.get(i).toString();
		}
		public ImageIcon getBytes(int i,int x,int y) {
			return new ImageIcon(new ImageIcon((byte[])this.get(i)).getImage().getScaledInstance(x, y, 4));
		}
		
	}
	
	
	public List<Row> query(String sql,Object...val){//try를 사용하면 블록이 종료되는 순간 자동으로 닫힌다.connection이
		List<Row> list =new ArrayList<>();
		try (Connection con = createConnection()){
			PreparedStatement s = settingPreparedstatement(con, sql, val);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				Row row = new Row();
				for (int i =1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getObject(i));
				}
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public void update(String sql,Object...val){
		List<Row> list =new ArrayList<>();
		try (Connection con = createConnection()){
			PreparedStatement s = settingPreparedstatement(con,sql,val);
			s.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final static Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/데이터베이스이름?allowLoadLocalInfile=true&serverTimzone=UTC","root","1234");
	}
	private static PreparedStatement settingPreparedstatement(Connection con, String sql, Object[] val) throws SQLException {
		PreparedStatement s = con.prepareStatement(sql);
		for (int i = 0; i < val.length; i++) {
			s.setObject(i+1, val[i]);
		}
		return s;
	}
	
}
