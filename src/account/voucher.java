package account;

import groovy.model.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

public class voucher extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnldps=new JPanel();
	JPanel pnlgeneral=new JPanel();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser date=new JDateChooser();

	JLabel lbldate=new JLabel("Date");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblsearch=new JLabel("Search");
	
	JComboBox cmbproduct=new JComboBox();
	
	JButton btnsearch=new JButton("Search"); 
	
	String col[]={"Group No","Addmission Fee","Passbook/Duplicate Passbook","Drop Out Fee","Loan Processing Fee","Risk Fund","Mendatory Savings","Additional Savings","Installment","Last installment","Total"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	String dps_col[]={"Group No","Addmission Fee","Duplicate Passbook","Drop Out Fee","Savings","Late fee","Total"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	search s=new search();
	
	String product_category="";
	public voucher(seasionbean s)
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
	public void btnwrk()
	{
		cmbproduct.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				try {
					ResultSet r=db.sta.executeQuery("select product_category from product where product_name='"+cmbproduct.getSelectedItem().toString()+"';");
					while(r.next())
					{
						product_category=r.getString("product_category");
					
					}
					r.close();
					if(product_category.equals("1"))
					{
						pnldps.setVisible(false);
						pnlmain.add(pnlgeneral);
						pnlgeneral.setVisible(true);
						
					}
					else if(product_category.equals("2"))
					{
						pnlgeneral.setVisible(false);
						pnlmain.add(pnldps);
						pnldps.setVisible(true);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbproduct.getSelectedIndex()!=0 && !date.getDate().equals(""))
				{
					searchevent();
				}
				else{
					JOptionPane.showMessageDialog(null, "please select the product type and enter the group number in search field");
					cmbproduct.requestFocus();
				}
			}
		});
		
	}
	
	
	public void searchevent()
	{
		try {
			if(!date.getDate().equals("") && product_category.equals("1"))
			{
				model.getDataVector().removeAllElements();
				revalidate();
				System.out.println("select groupno from member_info where addmission_date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
				ResultSet r=db.sta.executeQuery("select groupno from member_info where addmission_date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
				while(r.next())
				{
					model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"10","10","0","0","0","0","0","0","0","20"});
				}
				System.out.println("select groupno from member_info where addmission_date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && activition='y'");
				r.close();
				r=db.sta.executeQuery("select groupno from transaction where duplicate_passbook!=0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"');");
				while(r.next())
				{
					model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","10","0","0","0","0","0","0","0","10"});
				}
				r.close();
				r=db.sta.executeQuery("select groupno from transaction where mendatory_savings_return+additional_Savings_return>0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"');");
				while(r.next())
				{
					model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","0","10","0","0","0","0","0","0","10"});
				}
				r.close();
				r=db.sta.executeQuery("select groupno,loan as risk_fund from transaction where loan!=0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"');");
				while(r.next())
				{
					model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","0","0","05",String.valueOf(Integer.parseInt(r.getString("risk_fund"))/100),"0","0","0","0",Integer.parseInt(r.getString("risk_fund"))/100+5});
				}
				r.close();
				r=db.sta.executeQuery("select groupno,sum(mendatory_savings) as mendatory_savings,sum(additional_savings) as additional_savings,sum(installment) as installment,sum(last_installment) as last_installment,sum(mendatory_savings)+sum(additional_savings)+sum(installment)+sum(last_installment) as total from transaction where groupno!=0 && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" group by groupno");
				while(r.next())
				{
					model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","0","0","0","0",r.getString("mendatory_savings"),r.getString("additional_savings"),r.getString("installment"),r.getString("last_installment"),r.getString("total")});
				}
				r.close();
					
				model.addRow(new Object[]{"<html><b>Total</b><html>",total(1),total(2),total(3),total(4),total(5),total(6),total(7),total(8),total(9),total(10)});
				
				table.setEnabled(false);
			}
			else if(product_category.equals("2"))
			{
				dps_model.getDataVector().removeAllElements();
				revalidate();
				System.out.println("select groupno from member_info where addmission_date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"');");
				ResultSet r=db.sta.executeQuery("select groupno from member_info where addmission_date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"');");
				while(r.next())
				{
					dps_model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"50","0","0","0","0","50"});
				}
				r.close();
				System.out.println("select groupno,dps_duplicate_passbook from transaction where dps_duplicate_passbook!=0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno;");
				r=db.sta.executeQuery("select groupno,dps_duplicate_passbook from transaction where dps_duplicate_passbook!=0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno;");
				while(r.next())
				{
					dps_model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0",r.getString("dps_duplicate_passbook"),"0","0","0",r.getString("dps_duplicate_passbook")});
				}
				r.close();
				System.out.println("select groupno from transaction where dps_return>0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno;");
				r=db.sta.executeQuery("select groupno from transaction where dps_return>0 && date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno;");
				while(r.next())
				{
					dps_model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","0","100","0","0","100"});
				}
				System.out.println("select groupno,sum(dps_savings),sum(late_fee),sum(dps_savings)+sum(late_fee) as total from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno ");
				r=db.sta.executeQuery("select groupno,sum(dps_savings),sum(late_fee),sum(dps_savings)+sum(late_fee) as total from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')group by groupno");
				while(r.next())
				{
					dps_model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),"0","0","0",r.getString("sum(dps_savings)"),r.getString("sum(late_fee)"),r.getString("total")});
				}
				r.close();
				dps_model.addRow(new Object[]{"<html><b>Total</b></html>",dpstotal(1),dpstotal(2),dpstotal(3),dpstotal(4),dpstotal(5),dpstotal(6)});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int total(int col)
	{
		int t=0;
		for(int a=0;a<table.getRowCount();a++)
		{
			t+=Integer.parseInt(table.getValueAt(a, col).toString());
		}
		return t;
	}
	public int dpstotal(int col)
	{
		int t=0;
		for(int a=0;a<dps_table.getRowCount();a++)
		{
			t+=Integer.parseInt(dps_table.getValueAt(a, col).toString());
		}
		return t;
	}
	public void reset()
	{
		cmbproduct.setSelectedIndex(0);	
		date.setDate(new Date());
		model.getDataVector().removeAllElements();
		revalidate();
		dps_model.getDataVector().removeAllElements();
		revalidate();
		
	}
	
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(1200,500));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlgeneral,BorderLayout.CENTER);
		pnlmain.add(pnldps,BorderLayout.CENTER);
		pnldps.setVisible(false);
		northpnlwrk();
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
		pnlnorth.add(btnsearch);
		date.setDate(new Date());
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
