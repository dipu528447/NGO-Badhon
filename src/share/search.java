package share;

import java.sql.ResultSet;
import java.sql.SQLException;

public class search {
	db_connection db=new db_connection();
	public search()
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String name(String id,String table_name,String column_name)
	{
		String name="";
		try {
			//System.out.println("select "+column_name+" as name from "+table_name+" where id="+id+";");
			ResultSet rs=db.sta.executeQuery("select "+column_name+" as name from "+table_name+" where id="+id+";");
			while(rs.next())
			{
				name=rs.getString("name");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("name "+name);
		return name;
	}

}
