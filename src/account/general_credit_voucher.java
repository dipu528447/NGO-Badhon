package account;

import groovy.model.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import share.db_connection;
import share.search;
import share.seasionbean;

public class general_credit_voucher extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnltxt=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnldps=new JPanel();
	JPanel pnlgeneral=new JPanel();
	
	JTextField txtmendatory=new JTextField(10);
	JTextField txtadditional=new JTextField(10);
	JTextField txtinstallment=new JTextField(10);
	JTextField txtlast_installment=new JTextField(10);
	JTextField txtduplicate=new JTextField(10);
	//DecimalFormat df=new DecimalFormat();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser date=new JDateChooser();
	
	search s=new search();
	
	JLabel lblgroupno=new JLabel("Group No");
	JLabel lbldate=new JLabel("Date");
	JLabel lblgroup_po=new JLabel("Group P.O");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblsearch=new JLabel("Search");
	JLabel lblmendatory=new JLabel("Mendatory Savings");
	JLabel lbladditional=new JLabel("Additional Savings");
	JLabel lblinstallment=new JLabel("Installment");
	JLabel lbllast_installment=new JLabel("last Installment");
	JLabel lblduplicate=new JLabel("Duplicate Passbook");
	
	JComboBox cmbgroup=new JComboBox();
	
	JComboBox cmbpo=new JComboBox();
	JComboBox cmbproduct=new JComboBox();
	
	JButton btnsubmit=new JButton("Submit");
	//JButton btnupdate=new JButton("Total");
//	JButton btndelete=new JButton("Delete");
	JButton btnreset=new JButton("Reset");
	JButton btntotal=new JButton("Total");
	
	String col[]={"Name","Account","Mendatory Savings","Additional Savings","Payable Amount","Installment","Last installment","Duplicate Passbook"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model){
	public boolean isCellEditable(int row,int col){
			if(col==0 || col==1 || col==4){
				return false;
			}
			return true;
		}
	};
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	String dps_col[]={"Name","Account","Payable amount","Savings","Payable Late fee","Late fee","Duplicate Passbook"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	
	String product_category="";
	public general_credit_voucher(seasionbean s)
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
		btnwrk();
		try{
			ResultSet r=db.sta.executeQuery("select * from product");
			cmbproduct.addItem("");
			while(r.next()){
				cmbproduct.addItem(r.getString("product_name"));
			}
			r.close();
			cmbpo.addItem("");
			r=db.sta.executeQuery("select * from staff");
			while(r.next())
			{
				cmbpo.addItem(r.getString("name"));
			}
			r.close();
		}
		catch(Exception e)
		{
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
	public int total(int col){
		int total=0;
		for(int i=0;i<table.getRowCount();i++)
		{
			total+=Integer.parseInt(table.getValueAt(i, col).toString());
		}
		return total;
	}
	public void btnwrk()
	{
		cmbproduct.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbproduct.getSelectedIndex()!=0)
				{
					cmbgroup.removeAllItems();
					revalidate();
					cmbgroup.addItem("");
					
					try {
						ResultSet r=db.sta.executeQuery("select groupno,(select product_category from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') as product_category from group_info where product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && activity='y'");
						while(r.next())
						{
							product_category=r.getString("product_category");
							cmbgroup.addItem(r.getString("groupno"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
				if(product_category.equalsIgnoreCase("1"))
				{
					pnldps.setVisible(false);
					pnlgeneral.setVisible(true);
					pnlmain.add(pnlgeneral);	
				}
				else if(product_category.equalsIgnoreCase("2"))
				{
					pnlgeneral.setVisible(false);
					pnldps.setVisible(true);
					pnlmain.add(pnldps);
				}
				else{
					pnlgeneral.setVisible(false);
					pnldps.setVisible(false);
				}
			}
			
		});
		
		cmbgroup.addFocusListener(new FocusAdapter(){
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbproduct.getSelectedIndex()!=0 && cmbgroup.getSelectedIndex()!=0)
				{
					load();
				}
			}
		});
		
		btnsubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int a=JOptionPane.showConfirmDialog(null,"Are you sure to create this group?","Confirm",JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION)
				{
					try {
						if(product_category.equalsIgnoreCase("1")){
							//System.out.println("f");
							for(int b=0;b<table.getRowCount();b++)
							{
								if(Integer.parseInt(table.getValueAt(b, 2).toString())+Integer.parseInt(table.getValueAt(b, 3).toString())+Integer.parseInt(table.getValueAt(b,5).toString())+Integer.parseInt(table.getValueAt(b, 6).toString())+Integer.parseInt(table.getValueAt(b, 7).toString())>0)
								{
									int id=new_id();
								
									System.out.println("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b,5).toString()+","+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 7).toString()+",0,0,0,0,0,0,0,0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 5).toString()+","+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 7).toString()+",0,0,0,0,0,0,0,0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									id=new_id2();
									System.out.println("insert into transaction_credit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 5).toString()+","+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 7).toString()+",0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction_credit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 5).toString()+","+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 7).toString()+",0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
								}
							}
						}
						else 
						{
							for(int b=0;b<dps_table.getRowCount();b++)
							{
								if(Integer.parseInt(dps_table.getValueAt(b, 3).toString())+Integer.parseInt(dps_table.getValueAt(b, 5).toString())+Integer.parseInt(dps_table.getValueAt(b, 6).toString())>0)
								{
									int id=new_id();
									
									System.out.println("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,"+dps_table.getValueAt(b, 3).toString()+","+dps_table.getValueAt(b, 5).toString()+","+dps_table.getValueAt(b, 6).toString()+",0,0,0,0,0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,"+dps_table.getValueAt(b, 3).toString()+","+dps_table.getValueAt(b, 5).toString()+","+dps_table.getValueAt(b, 6).toString()+",0,0,0,0,0,0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									id=new_id2();
									System.out.println("insert into transaction_credit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,"+dps_table.getValueAt(b, 3).toString()+","+dps_table.getValueAt(b, 5).toString()+","+dps_table.getValueAt(b, 6).toString()+",(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction_credit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,"+dps_table.getValueAt(b, 3).toString()+","+dps_table.getValueAt(b, 5).toString()+","+dps_table.getValueAt(b, 6).toString()+",(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
								}
							}
						}
						JOptionPane.showMessageDialog(null, "Transaction Are Successfully Saved");					
						reset();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				else{
					JOptionPane.showMessageDialog(null, "Please Fill Up All Cell of Table");
				}

			}
		});
		btntotal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				txtmendatory.setText(String.valueOf(total(2)));
				txtadditional.setText(String.valueOf(total(3)));
				txtinstallment.setText(String.valueOf(total(5)));
				txtlast_installment.setText(String.valueOf(total(6)));
				txtduplicate.setText(String.valueOf(total(7)));
			}
		});
		btnreset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				// TODO Auto-generated method stub
				int a=JOptionPane.showConfirmDialog(null,"Are you sure to reset?","Confirm",JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION)
				{
					reset();
				}
			}
		});
//		btndelete.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				if(valid()==true)
//				{
//				
//					int a=JOptionPane.showConfirmDialog(null,"Are you sure to delete this group?","Confirm",JOptionPane.YES_NO_OPTION);
//					if(a==JOptionPane.YES_OPTION)
//					{
//						try {
//							db.sta.execute("");
//							reset();
//							
//							JOptionPane.showMessageDialog(null, "This Group is successfully deleted");
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					
//				}
//			}
//		});
////		btnupdate.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				if(valid()==true)
//				{
//					int a=JOptionPane.showConfirmDialog(null,"Are you sure to update this group?","Confirm",JOptionPane.YES_NO_OPTION);
//					if(a==JOptionPane.YES_OPTION)
//					{
//						try {
//							db.sta.execute("");
//							reset();
//							JOptionPane.showMessageDialog(null, "This Group is successfully Updated");
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					
//				}
//			}
//		});
////		btnsearch.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				if(true)
//				{
//					searchevent();
//				}
//				else{
//					JOptionPane.showMessageDialog(null, "please select the product type and enter the group number in search field");
//					cmbproduct.requestFocus();
//				}
//			}
//		});
//		
	}
	public void load()
	{
		model.getDataVector().removeAllElements();
		validate();
		dps_model.getDataVector().removeAllElements();
		validate();
		try {
			if(product_category.equalsIgnoreCase("1"))
			{
				ResultSet r=db.sta.executeQuery("select name_eng,account from member_info where groupno=(select id from group_info where activition='y' && groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) order by account asc;");
				while(r.next())
				{
					model.addRow(new Object[]{r.getString("name_eng"),r.getString("account"),"0","0","0","0","0","0"});
				}
				r.close();
				installment();
			}
			else{
				ResultSet r=db.sta.executeQuery("select name_eng,account,dps_category from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) order by account asc;");
				while(r.next())
				{
					dps_model.addRow(new Object[]{r.getString("name_eng"),r.getString("account"),r.getString("dps_category"),"0","0","0","0"});
				}
				r.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int paid(int i,Date date2)
	{
		int p=0;
		try {
			System.out.println("select ifnull(sum(installment)+sum(last_installment),0) as installment from transaction where account_id=(select id from member_info where activition='y' && account="+table.getValueAt(i, 1).toString()+" && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && date>="+new SimpleDateFormat("YYYYMMdd").format(date2)+";");
			ResultSet r=db.sta.executeQuery("select ifnull(sum(installment)+sum(last_installment),0) as installment from transaction where account_id=(select id from member_info where activition='y' && account="+table.getValueAt(i, 1).toString()+" && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && date>="+new SimpleDateFormat("YYYYMMdd").format(date2)+";");
			while(r.next())
			{
				p=Integer.parseInt(r.getString("installment"));
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	public void installment()
	{
		for(int i=0;i<table.getRowCount();i++)
		{
			
			try {
				System.out.println("select cast(ifnull((max(loan)*13.5)/100+max(loan),(select ifnull(loan_installment,0) from member_info where id=(select id from member_info where account="+table.getValueAt(i, 1).toString()+" && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && activition='y'))) as unsigned) as loan_installment,ifnull(max(date),20170701) as date from transaction where account_id=(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && account="+table.getValueAt(i, 1).toString()+" && activition='y') && loan!=0;");
				ResultSet r=db.sta.executeQuery("select cast(ifnull((max(loan)*13.5)/100+max(loan),(select ifnull(loan_installment,0) from member_info where id=(select id from member_info where account="+table.getValueAt(i, 1).toString()+" && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && activition='y'))) as unsigned) as loan_installment,ifnull(max(date),20170701) as date from transaction where account_id=(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && account="+table.getValueAt(i, 1).toString()+" && activition='y') && loan!=0;");
				while(r.next())
				{
					int loan=Integer.parseInt(r.getString("loan_installment").toString());
					int paid=paid(i,r.getDate("date"));
					System.out.println(loan+","+paid);
					model.setValueAt(loan-paid, i,4 );
					break;
				}
				r.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//	public void searchevent()
//	{
//		
//		try {
//			if(!date.getDate().equals("") && product_category.equals("1"))
//			{
//				model.getDataVector().removeAllElements();
//				revalidate();
//				System.out.println("");
//				ResultSet r=db.sta.executeQuery("select * from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && ledger_id=(select id from ledger where ledger_name='"+cmbproduct.getSelectedItem().toString()+"' && activition='y')");
//				while(r.next())
//				{
//					model.addRow(new Object[]{r.getString("date"),r.getString(arg0)});
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public void reset()
	{
		txtadditional.setText("");
		txtduplicate.setText("");
		txtinstallment.setText("");
		txtlast_installment.setText("");
		txtmendatory.setText("");
		cmbgroup.setSelectedIndex(0);
		cmbproduct.setSelectedIndex(0);
		cmbpo.setSelectedIndex(0);
		date.setDate(new Date());
		model.getDataVector().removeAllElements();
		revalidate();
		dps_model.getDataVector().removeAllElements();
		revalidate();
		
	}
	public int new_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(transaction_id),0)+1 as id from transaction");
			while(r1.next())
			{
				i=Integer.parseInt(r1.getString("id"));
			}
			r1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	public int new_id2()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(transaction_id),0)+1 as id from transaction_credit");
			while(r1.next())
			{
				i=Integer.parseInt(r1.getString("id"));
			}
			r1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(1200,600));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlsouth,BorderLayout.SOUTH);
		pnlmain.add(pnlgeneral,BorderLayout.CENTER);
		pnlmain.add(pnldps,BorderLayout.CENTER);
		pnldps.setVisible(false);
		northpnlwrk();
		southpnlwrk();
		generalpnlwrk();
		dpspnlwrk();
	}
	public void northpnlwrk()
	{
		pnlnorth.setOpaque(false);
		pnlnorth.setPreferredSize(new Dimension(1200,50));
		pnlnorth.setLayout(new FlowLayout());
		pnlnorth.setBorder(BorderFactory.createLineBorder(Color.black));
		
		pnlnorth.add(lbldate);
		pnlnorth.add(date);
		pnlnorth.add(lblproduct);
		pnlnorth.add(cmbproduct);
		pnlnorth.add(lblgroupno);
		pnlnorth.add(cmbgroup);
		pnlnorth.add(lblgroup_po);
		pnlnorth.add(cmbpo);
		cmbpo.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
		
		date.setDate(new Date());
	}
	public void southpnlwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(1200,120));
		pnlsouth.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.setOpaque(false);
		pnlsouth.add(pnltxt);
		pnltxtwrk();
		pnlsouth.add(btnsubmit);
//		pnlsouth.add(btndelete);
		pnlsouth.add(btntotal);
		pnlsouth.add(btnreset);
 	}
	public void pnltxtwrk()
	{
		pnltxt.setPreferredSize(new Dimension(1200,70));
		pnltxt.setBorder(BorderFactory.createLineBorder(Color.black));
		pnltxt.setOpaque(false);
		pnltxt.setLayout(new FlowLayout());
		pnltxt.add(lblmendatory);
		pnltxt.add(txtmendatory);
		pnltxt.add(lbladditional);
		pnltxt.add(txtadditional);
		pnltxt.add(lblinstallment);
		pnltxt.add(txtinstallment);
		pnltxt.add(lbllast_installment);
		pnltxt.add(txtlast_installment);
		pnltxt.add(lblduplicate);
		pnltxt.add(txtduplicate);
		
		
	}
 	public void generalpnlwrk()
 	{
 		pnlgeneral.setOpaque(false);
 		pnlgeneral.setPreferredSize(new Dimension(1200,430));
 		pnlgeneral.setLayout(new FlowLayout());
 		pnlgeneral.add(scro);
 		scro.setOpaque(false);
 		scro.setPreferredSize(new Dimension(1180,400));
	}
	public void dpspnlwrk()
	{
		pnldps.setOpaque(false);
		pnldps.setPreferredSize(new Dimension(1200,430));
 		pnldps.setLayout(new FlowLayout());
 		pnldps.add(dps_scro);
 		dps_scro.setPreferredSize(new Dimension(1180,400));
	}
}
