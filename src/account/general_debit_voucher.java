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

public class general_debit_voucher extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnldps=new JPanel();
	JPanel pnlgeneral=new JPanel();
	JPanel pnltxt=new JPanel();
	
	JLabel lblmendatory_withdraw=new JLabel("Mendatory Savings Withdraw");
	JLabel lbladditional_withdraw=new JLabel("Additional Savings Withdraw");
	JLabel lblmendatory_return=new JLabel("Mendatory Savings Return");
	JLabel lbladditional_return=new JLabel("Additional Savings Return");
	JLabel lblloan=new JLabel("Loan");
	

	JTextField txtmendatory_withdraw=new JTextField(7);
	JTextField txtadditional_withdraw=new JTextField(7);
	JTextField txtmendatory_return=new JTextField(7);
	JTextField txtadditional_return=new JTextField(7);
	JTextField txtloan=new JTextField(7);
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser date=new JDateChooser();
	
	JLabel lblgroupno=new JLabel("Group No");
	JLabel lbldate=new JLabel("Date");
	JLabel lblgroup_po=new JLabel("Group P.O");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblsearch=new JLabel("Search");
	
	JComboBox cmbgroup=new JComboBox();
	
	JComboBox cmbpo=new JComboBox();
	JComboBox cmbproduct=new JComboBox();
	
	JButton btnsubmit=new JButton("Submit");
//	JButton btnupdate=new JButton("Update");
//	JButton btndelete=new JButton("Delete");
	JButton btnreset=new JButton("Reset");
	JButton btntotal=new JButton("Total");
	
	String col[]={"Name","Account","Mendatory Savings Withdraw","Additional Savings Withdraw","Mendatory Savings Return","Additional Savings Return","Loan Distribution"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	String dps_col[]={"Name","Account","Savings Return"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	
	String product_category="";
	public general_debit_voucher(seasionbean s)
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
		btntotal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				txtmendatory_withdraw.setText(String.valueOf(total(2)));
				txtadditional_withdraw.setText(String.valueOf(total(3)));
				txtmendatory_return.setText(String.valueOf(total(4)));
				txtadditional_return.setText(String.valueOf(total(5)));
				txtloan.setText(String.valueOf(total(6)));
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
							for(int b=0;b<table.getRowCount();b++)
							{
								if(Integer.parseInt(table.getValueAt(b, 2).toString())+Integer.parseInt(table.getValueAt(b, 3).toString())+Integer.parseInt(table.getValueAt(b,4).toString())+Integer.parseInt(table.getValueAt(b, 5).toString())+Integer.parseInt(table.getValueAt(b, 6).toString())>0)
								{
									int id=new_id();
									
									System.out.println("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),0,0,0,0,0,0,0,0,"+table.getValueAt(b,6).toString()+","+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 4).toString()+","+table.getValueAt(b, 5).toString()+",0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),0,0,0,0,0,0,0,0,"+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 4).toString()+","+table.getValueAt(b, 5).toString()+",0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									
									id=new_id2();
									System.out.println("insert into transaction_debit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b,6).toString()+","+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 4).toString()+","+table.getValueAt(b, 5).toString()+",0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction_debit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+" && activition='y'),"+table.getValueAt(b, 6).toString()+","+table.getValueAt(b, 2).toString()+","+table.getValueAt(b, 3).toString()+","+table.getValueAt(b, 4).toString()+","+table.getValueAt(b, 5).toString()+",0,0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									
									if(Integer.parseInt(table.getValueAt(b, 4).toString())>0){
										db.sta.execute("update member_info set activition='n' where groupno=(select id from group_info where groupno="+cmbgroup.getSelectedItem().toString()+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+";");
									}
									if(Integer.parseInt(table.getValueAt(b, 6).toString())>0)
									{
										db.sta.execute("update member_info set loan="+table.getValueAt(b, 6).toString()+" where groupno=(select id from group_info where groupno="+cmbgroup.getSelectedItem().toString()+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+table.getValueAt(b, 1).toString()+";");
									}
									
								}
							}
						}
						else 
						{
							for(int b=0;b<dps_table.getRowCount();b++)
							{
								if(Integer.parseInt(dps_table.getValueAt(b, 2).toString())>0)
								{
									int id=new_id();
									System.out.println("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,0,0,0,0,0,0,0,0,"+dps_table.getValueAt(b, 2).toString()+",0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,0,0,0,0,0,0,0,0,"+dps_table.getValueAt(b, 2).toString()+",0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									id=new_id2();
									System.out.println("insert into transaction_debit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0."+dps_table.getValueAt(b, 2).toString()+",0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("insert into transaction_debit values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),(select id from member_info where groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+dps_table.getValueAt(b, 1).toString()+"),0,0,0,0,0,"+dps_table.getValueAt(b, 2).toString()+",0,(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),CURRENT_TIMESTAMP(),'"+sa.getuserid()+"')");
									db.sta.execute("update member_info set activition='n' where groupno=(select id from group_info where groupno="+cmbgroup.getSelectedItem().toString()+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && account="+dps_table.getValueAt(b, 1).toString()+";");
									
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
				ResultSet r=db.sta.executeQuery("select name_eng,account from member_info where groupno=(select id from group_info where activition='y' && groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' order by id asc;");
				while(r.next())
				{
					model.addRow(new Object[]{r.getString("name_eng"),r.getString("account"),"0","0","0","0","0","0"});
				}
				r.close();
			}
			else{
				ResultSet r=db.sta.executeQuery("select name_eng,account from member_info where groupno=(select id from group_info where activition='y' && groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && activition='y' order by id asc;");
				while(r.next())
				{
					dps_model.addRow(new Object[]{r.getString("name_eng"),r.getString("account"),"0","0","0","0","0"});
				}
				r.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		txtadditional_return.setText("");
		txtmendatory_return.setText("");
		txtadditional_withdraw.setText("");
		txtmendatory_withdraw.setText("");
		txtloan.setText("");
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
			ResultSet r1=db.sta.executeQuery("select ifnull(max(transaction_id),0)+1 as id from transaction_debit");
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
	public void pnltxtwrk()
	{
		pnltxt.setPreferredSize(new Dimension(1200,70));
		pnltxt.setLayout(new FlowLayout());
		pnltxt.setOpaque(false);
		pnltxt.add(lblmendatory_withdraw);
		pnltxt.add(txtmendatory_withdraw);
		pnltxt.add(lbladditional_withdraw);
		pnltxt.add(txtadditional_withdraw);
		pnltxt.add(lblmendatory_return);
		pnltxt.add(txtmendatory_return);
		pnltxt.add(lbladditional_return);
		pnltxt.add(txtadditional_return);
		pnltxt.add(lblloan);
		pnltxt.add(txtloan);
		
	}
	public void southpnlwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(1200,150));
		pnlsouth.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.setOpaque(false);
		pnlsouth.add(pnltxt);
		pnltxtwrk();
		pnlsouth.add(btnsubmit);
		pnlsouth.add(btntotal);
//		pnlsouth.add(btnupdate);
		pnlsouth.add(btnreset);
 	}
 	public void generalpnlwrk()
 	{
 		pnlgeneral.setOpaque(false);
 		pnlgeneral.setPreferredSize(new Dimension(1200,400));
 		pnlgeneral.setLayout(new FlowLayout());
 		pnlgeneral.add(scro);
 		scro.setOpaque(false);
 		scro.setPreferredSize(new Dimension(1180,380));
	}
	public void dpspnlwrk()
	{
		pnldps.setOpaque(false);
		pnldps.setPreferredSize(new Dimension(1200,400));
 		pnldps.setLayout(new FlowLayout());
 		pnldps.add(dps_scro);
 		dps_scro.setPreferredSize(new Dimension(1180,380));
	}
}
