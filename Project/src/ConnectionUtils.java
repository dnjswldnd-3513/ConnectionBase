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
		List<Row> list =new ArrayList<>();//리스트로 반환할것이기에 리스트를 준비해줌
		try (Connection con = createConnection()){//데이터 베이스 연결을 생성시키고
			PreparedStatement s = settingPreparedstatement(con, sql, val);//sql 쿼리와 파라미터를 설정시켜서 파라미터 객체 생성
			ResultSet rs = s.executeQuery();//쿼리를 실행하고 resusltset으로 반환을 받는다. 쿼리 결과의 데이터에 접근 할수 있게 해준다.
			
			//executeQuery();
			//select 쿼리를 실행할때 사용된다. resultset을 반환하기 때문에.
			//조회목적
			
			
			//Resultset은 sql데이터를 테이블 형태로 표현한다 행과 열로 이루어진 2차원 자료구조임
			while (rs.next()) {// 다음 행으로 이동한다. next()가 그뜻임 
				//여기서 true? 그럼 반복시키는거
				Row row = new Row();//현재 행의 데이터들을 저장할 row를 만듬
				for (int i =1; i <= rs.getMetaData().getColumnCount(); i++) {
					//resultset의 열 수만큼 반복시킨다. 왜 i = 1인가? resultset 열 인덱스가 1부터 시작임
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
			//데이터를 수정할때 사용한다. 
			//정수형 값을 반환한다. ex) 양향을 받은 행의 수.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final static Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/데이터베이스이름?allowLoadLocalInfile=true&serverTimzone=UTC","root","1234");
	}
	
	private static PreparedStatement settingPreparedstatement(Connection con, String sql, Object[] val) throws SQLException {
		//쿼리문을 만드는 뭐 구문을 만드는? 이후의 실행시킴
		PreparedStatement s = con.prepareStatement(sql);
		for (int i = 0; i < val.length; i++) {
			s.setObject(i+1, val[i]);
		}
		return s;
	}
	
}
