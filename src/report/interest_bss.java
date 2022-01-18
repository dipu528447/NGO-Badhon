package report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import share.convert;
import share.db_connection;
import share.search;
import share.seasionbean;

public class interest_bss  extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnldps=new JPanel();
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	GridBagConstraints grid=new GridBagConstraints();
	JLabel lblfrom=new JLabel("From");
	JLabel lblto=new JLabel("To");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblgroup=new JLabel("Group No");
	JLabel lblsearch=new JLabel("Search");
	String dps_col[]={"name","Account","category","year","Interest","Total"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	JButton btnsearch=new JButton("Search"); 
	JButton btnprint=new JButton("Print");
	
	JComboBox<String> cmbgroup=new JComboBox<String>();
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	DecimalFormat df=new DecimalFormat();
	search s=new search();
	convert c=new convert();
	public interest_bss(seasionbean s)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sa=s;
		setVisible(true);
		setOpaque(false);
		setPreferredSize(new Dimension(1200,600));
		background();
		add(pnlmain);
		design();
		load();
		btnaction();
	}
	public void btnaction()
	{
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbgroup.getSelectedIndex()!=0){
					
					searchevent();
//					for(int i=0;i<dps_table.getRowCount();i++)
//					{
//						dps_model.setValueAt(c.etob(dps_table.getValueAt(i, 1).toString()), i, 1);
//						dps_model.setValueAt(c.etob(dps_table.getValueAt(i,2).toString()), i, 2);
//						dps_model.setValueAt(c.etob(dps_table.getValueAt(i, 3).toString()), i, 3);
//						//dps_model.setValueAt(c.etob(dps_table.getValueAt(i, 4).toString()), i, 4);
//						//dps_model.setValueAt(c.etob(dps_table.getValueAt(i, 5).toString()), i, 5);
//					}
				}
			}
		});
	}
	public void searchevent()
	{
		try {
			ResultSet r=db.sta.executeQuery("select name_ban,addmission_date,Date_format(addmission_date,'%Y%m') as admission_date,account,dps_year,dps_category from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='bss')) order by account asc;");
	
			Date date=new Date();
			while(r.next())
			{
				String grp=(cmbgroup.getSelectedItem().toString().length()==1)?"0"+cmbgroup.getSelectedItem().toString():cmbgroup.getSelectedItem().toString();
				String account=r.getString("account").length()==1?"000"+r.getString("account"):r.getString("account").length()==2?"00"+r.getString("account"):r.getString("account").length()==3?"0"+r.getString("account"):r.getString("account");
				dps_model.addRow(new Object[]{r.getString("name_ban"),"02"+r.getString("admission_date")+grp+account,r.getString("dps_category"),r.getString("dps_year"),r.getString("admission_date"),""});
			}
			r.close();
			for(int i=0;i<dps_table.getRowCount();i++)
			{
				int year=Integer.parseInt(dps_table.getValueAt(i, 4).toString().substring(0,4));
				int current_year=date.getYear();
				int mature_year=(year+Integer.parseInt(dps_table.getValueAt(i, 3).toString()))-current_year>0?current_year:year+Integer.parseInt(dps_table.getValueAt(i, 3).toString());
				
				int month=Integer.parseInt(dps_table.getValueAt(i, 4).toString().substring(4,dps_table.getValueAt(i, 4).toString().length()));
				//System.out.println(month);
				for(int j=year;j<=mature_year;j++)
				{
					for(int k=month;k<=12;k++)
					{
						 r=db.sta.executeQuery("select * from transaction where date>=20181101 && date<=20181201 && account_id=(select id from member_info where account=1 && activition='y' && groupno=(select id from group_info where groupno='1' && product=(select id from product where product_name='bss'))) ");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		cmbgroup.addItem(" ");
		try {
			ResultSet r=db.sta.executeQuery("select groupno from group_info where product=2 && activity='y' order by id");
			while(r.next())
			{
				cmbgroup.addItem(r.getString("groupno"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void background(){
		try {                
			image = ImageIO.read(new File("icon/aura02.jpg"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	}
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(1200,600));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		
		pnlmain.add(pnldps,BorderLayout.CENTER);
		pnldps.setVisible(true);
		northpnlwrk();
		
		dpspnlwrk();
	}
	public void northpnlwrk()
	{
		pnlnorth.setOpaque(false);
		pnlnorth.setPreferredSize(new Dimension(1200,100));
		pnlnorth.setLayout(new FlowLayout());
		pnlnorth.setBorder(BorderFactory.createLineBorder(Color.black));
	
		pnlnorth.add(lblgroup);
		pnlnorth.add(cmbgroup);
		pnlnorth.add(btnsearch);
		pnlnorth.add(btnprint);
	
	}
	
 	
	public void dpspnlwrk()
	{
		pnldps.setOpaque(false);
		pnldps.setPreferredSize(new Dimension(1200,490));
 		pnldps.setLayout(new FlowLayout());
 		pnldps.add(dps_scro);
 		dps_scro.setPreferredSize(new Dimension(1180,470));
 		dps_scro.setOpaque(false);
 		
 		//dps_table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
 		//table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
	}
}

