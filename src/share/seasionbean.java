package share;

public class seasionbean {
	String company_name="",username="",password="",user_type="",user_login_time="",userid="";
	public String getcompanyname()
	{
		return company_name;
	}
	public void setcompanyname(String companyname)
	{
		company_name=companyname;
	}
	
	public String getuserid()
	{
		return userid;
	}
	public void setuserid(String user)
	{
		userid=user;
	}
	
	public String getusername()
	{
		return username;
	}
	public void setusername(String user)
	{
		username=user;
	}
	public String getpassword()
	{
		return password;
	}
	public void setpassword(String pass)
	{
		password=pass;
	}
	public String getusertype()
	{
		return user_type;
	}
	public void setusertype(String type)
	{
		user_type=type;
	}
	public String getlogintime()
	{
		return user_login_time;
	}
	public void setlogintime(String login)
	{
		user_login_time=login;
	}
}
