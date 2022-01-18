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

import report.savings_return_withdraw_report;
import share.db_connection;
import share.search;
import share.seasionbean;

public class debit_voucher extends JPanel{
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
	
	String col[]={"Group No","Member No","Name","Mendatory Savings Withdraw","Additional Savings Withdraw","Mendatory Savings Return","Additional Savings Return","Loan Distribution"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	String dps_col[]={"Group No","Member No","Name","Savings Return"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	search s=new search();
	
	String product_category="";
	public debit_voucher(seasionbean s)
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
					JOptionPane.showMessageDialog(null, "please select the product type and enter the date in date field");
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
				ResultSet r=db.sta.executeQuery("select groupno,account_id,mendatory_savings_withdraw,additional_savings_withdraw,mendatory_savings_return,additional_savings_return,loan from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
				
				
				while(r.next())
				{
					int mendatory_savings_withdraw=Integer.parseInt(r.getString("mendatory_savings_withdraw"));
					int additional_savings_withdraw=Integer.parseInt(r.getString("additional_savings_withdraw"));
					int mendatory_savings_return=Integer.parseInt(r.getString("mendatory_savings_return"));
					int additional_savings_return=Integer.parseInt(r.getString("additional_savings_return"));
					int loan=Integer.parseInt(r.getString("loan"));
					if(mendatory_savings_withdraw+additional_savings_withdraw+mendatory_savings_return+additional_savings_return+loan>0)
					{
						model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),s.name(r.getString("account_id"), "member_info", "account"),s.name(r.getString("account_id"), "member_info", "name_eng"),mendatory_savings_withdraw,additional_savings_withdraw,mendatory_savings_return,additional_savings_return,loan});	
					}
				}
				model.addRow(new Object[]{"<html><b>Total</b></html>","","",total(3),total(4),total(5),total(6),total(7)});
				table.setEnabled(false);
			}
			else if(product_category.equals("2"))
			{
				dps_model.getDataVector().removeAllElements();
				revalidate();
				ResultSet r=db.sta.executeQuery("select groupno,account_id,dps_return from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && product=(select id from product where dps_return!=0 && product_name='"+cmbproduct.getSelectedItem().toString()+"')");
				while(r.next())
				{
					dps_model.addRow(new Object[]{s.name(r.getString("groupno"),"group_info","groupno"),s.name(r.getString("account_id"), "member_info", "account"),s.name(r.getString("account_id"), "member_info", "name_eng"),r.getString("dps_return")});

				}
				dps_model.addRow(new Object[]{"<html><b>Total</b></html>","","",dpstotal(3)});
				dps_table.setEnabled(false);
			}
		} catch (Exception e) {
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
