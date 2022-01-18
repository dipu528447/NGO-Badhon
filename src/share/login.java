package share;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class login extends JFrame{
	seasionbean s=new seasionbean();
	
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnlcenter=new JPanel();
	JPanel pnleast=new JPanel();
	JLabel lblcompanyname=new JLabel("");
	
	JButton btnlogin=new JButton("LOGIN",new ImageIcon("icon/login.png"));
	JButton btnreset=new JButton("RESET",new ImageIcon("icon/reresh.png"));
	
	JTextField txtusername=new JTextField();
	JPasswordField txtpassword=new JPasswordField();
	
	JLabel lbluser=new JLabel("Enter the user name");
	JLabel lblpass=new JLabel("Enter the password");
	
	JLabel lblusername=new JLabel("USERNAME");
	JLabel lblpassword=new JLabel("PASSWORD");
	GridBagConstraints grid=new GridBagConstraints();
	seasionbean sa;
	db_connection db=new db_connection();
	
	boolean log=false;
	int count=3;
	public login(seasionbean s)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sa=s;
		setSize(500,300);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setTitle("LOGIN :: "+sa.getcompanyname());
		setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("icon/accouts.png"));
		design();
		btnwrk();
	}
	public void design()
	{
		add(pnlmain);
		pnlmain.setPreferredSize(new Dimension(500,350));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlsouth,BorderLayout.SOUTH);
		pnlmain.add(pnlcenter,BorderLayout.CENTER);
		
		pnlnorthwrk();
		pnlsouthwrk();
		pnlcenterwrk();
	}
	public void pnlnorthwrk()
	{
		pnlnorth.setPreferredSize(new Dimension(500,70));
		pnlnorth.setLayout(new FlowLayout());
		pnlnorth.add(lblcompanyname);
		lblcompanyname.setText(sa.getcompanyname());
		lblcompanyname.setFont(new Font("tahoma",Font.PLAIN,18));
		pnlnorth.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		pnlnorth.setBackground(Color.darkGray);
		lblcompanyname.setForeground(Color.white);
		
	}
	public void pnlsouthwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(500,70));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		pnlsouth.add(btnlogin);
		pnlsouth.add(btnreset);
		pnlsouth.setBackground(Color.darkGray);
	}
	public void pnlcenterwrk()
	{
		pnlcenter.setPreferredSize(new Dimension(500,160));
		pnlcenter.setLayout(new GridBagLayout());
		pnlcenter.setBackground(Color.lightGray);
		grid.insets=new Insets(3,3,3,3);
		grid.gridx=0;
		grid.gridy=0;
		pnlcenter.add(lblusername,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnlcenter.add(txtusername,grid);
		grid.gridx=0;
		grid.gridy=1;
		pnlcenter.add(lblpassword,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnlcenter.add(txtpassword,grid);
		
		txtusername.setLayout(new FlowLayout());
		txtpassword.setLayout(new FlowLayout());
		txtusername.setPreferredSize(new Dimension(250,40));
		txtpassword.setPreferredSize(new Dimension(250,40));
		
		txtusername.add(lbluser);
		txtpassword.add(lblpass);
		
		lblusername.setFont(new Font("tahoma",Font.PLAIN,20));
		lblpassword.setFont(new Font("tahoma",Font.PLAIN,20));
		
		lbluser.setFont(new Font("tahoma",Font.PLAIN,15));
		lblpass.setFont(new Font("tahoma",Font.PLAIN,15));
	}
	
	public void btnwrk()
	{
		txtusername.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				char a=arg0.getKeyChar();
				if((a==arg0.VK_BACK_SPACE || a==arg0.VK_DELETE)&& txtusername.getText().equalsIgnoreCase(""))
				{
					lbluser.setVisible(true);
				}
				else{
					lbluser.setVisible(false);
				}

			}
		});
		txtpassword.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				char a=e.getKeyChar();
				if((a==e.VK_BACK_SPACE || a==e.VK_DELETE)&& txtpassword.getText().equalsIgnoreCase(""))
				{
					lblpass.setVisible(true);
				}
				else{
					lblpass.setVisible(false);
				}
			}
		});
		btnlogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true){
					open();
					
				}
			}
		});
	}
	
	
	public boolean valid()
	{
		if(!txtusername.getText().trim().toString().equalsIgnoreCase(""))
		{
			if(!txtpassword.getText().trim().toString().equalsIgnoreCase(""))
			{
				return true;
			}
			else{
				JOptionPane.showMessageDialog(null,"enter the password please");
				txtpassword.requestFocus();
				return false;
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "enter the username please");
			txtusername.requestFocus();
			return false;
		}
	}
	
	public void open()
	{

		try {
			ResultSet rs=db.sta.executeQuery("select * from user;");
			while(rs.next())
			{
				String type=rs.getString("type");
				String id=rs.getString("id");
				if(rs.getString("username").equalsIgnoreCase(txtusername.getText())&& rs.getString("password").equalsIgnoreCase(txtpassword.getText()))
				{
					sa.setusertype(type);
					sa.setusername(txtusername.getText());
					sa.setuserid(id);
					log=true;
					entry(id);
					root_frame r=new root_frame(this,sa);
					pnlmain.setVisible(false);
					break;
					
				}
			}
			rs.close();
			if(log==false)
			{
				count--;
				JOptionPane.showMessageDialog(null, "please enter valid username and password. you have only"+count+" chances");
				
				if(count==0)
				{
					JOptionPane.showMessageDialog(null, "Sorry you are block for 30second");
					for(int i=0;i<3000;i++)
					{
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void entry(String id)
	{
		try {
			db.sta.execute("insert into login values('"+id+"',CURRENT_TIMESTAMP());");
			db.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

