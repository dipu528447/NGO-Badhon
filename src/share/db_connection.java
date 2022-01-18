package share;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
public class db_connection{
	String a=null,username=null,b=null,password=null,c=null,port=null,d=null,server=null,e=null,db_file=null;
	public Connection con;
	public Statement sta;
	public void conect()throws SQLException{
		try{
			File file=new File("src/db_connection.txt");
			Scanner scan=new Scanner(file);
			int temp=1;
			while(scan.hasNextLine()){
				
				if(temp==1){
					String s=scan.nextLine();
					StringTokenizer token=new StringTokenizer(s);
					a=token.nextToken();
					username=token.nextToken();
					temp=2;
				}
				else if(temp==2){
					String s=scan.nextLine();
					StringTokenizer token=new StringTokenizer(s);
					b=token.nextToken();
					password=token.nextToken();
					temp=3;
				}
				else if(temp==3){
					String s=scan.nextLine();
					StringTokenizer token=new StringTokenizer(s);
					c=token.nextToken();
					port=token.nextToken();
					temp=4;
				}
				else if(temp==4){
					String s=scan.nextLine();
					StringTokenizer token=new StringTokenizer(s);
					d=token.nextToken();
					server=token.nextToken();
					temp=5;
				}
				else if(temp==5){
					String s=scan.nextLine();
					StringTokenizer token=new StringTokenizer(s);
					e=token.nextToken();
					db_file=token.nextToken();
					break;
					}
			}
			
			String url="jdbc:mysql://"+server+":"+port+"/"+db_file;
			String uni="?useUnicode=true&characterEncoding=UTF-8";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con=DriverManager.getConnection(url+uni,username,password);
			sta=con.createStatement();
			//System.out.println ("Database connection established");
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}
	}
}
