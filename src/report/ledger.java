package report;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.event.PrintEvent;
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

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import com.toedter.calendar.JDateChooser;

import share.convert;
import share.db_connection;
import share.search;
import share.seasionbean;

public class ledger extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnldps=new JPanel();
	JPanel pnlgeneral=new JPanel();
	
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser from_date=new JDateChooser();
	JDateChooser to_date=new JDateChooser();

	JLabel lblfrom=new JLabel("From");
	JLabel lblto=new JLabel("To");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblgroup=new JLabel("Group No");
	JLabel lblsearch=new JLabel("Search");
	
	JComboBox cmbproduct=new JComboBox();
	JComboBox cmbgroup=new JComboBox();
	
	JButton btnsearch=new JButton("Search"); 
	JButton btnprint=new JButton("Print");
	
	convert c=new convert();
	String col[]={"Account","Pre Savings","Savings","Withdraw","Return","Current Savings","Pre Due Installment","Installment","Due Installment"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	String dps_col[]={"Account","Pre Savings","Savings","Total Savings"};
	Object dps_row[][]={};
	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
	JTable dps_table=new JTable(dps_model);
	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	DecimalFormat df=new DecimalFormat();
	search s=new search();
	
	String product_category="";
	public ledger(seasionbean s)
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
	public void printevent(){
		try{
			HashMap map=null;
			int a=0;
			if(product_category.equals("1"))
			{
				while(a<table.getRowCount())
				{
					map=new HashMap();
					map.put("date", table.getValueAt(a, 0));
					map.put("name", table.getValueAt(a, 3));
					map.put("groupno", table.getValueAt(a, 1));
					map.put("memberno", table.getValueAt(a, 2));
					map.put("addmission", table.getValueAt(a, 4));
					//map.put("total",c.etob(df.format(total(4))));
					map.put("from", c.etob(new SimpleDateFormat("YYYY-MM-dd").format(from_date.getDate())));
					map.put("to", c.etob(new SimpleDateFormat("YYYY-MM-dd").format(to_date.getDate())));
					a++;
					list.add(map);
					
				}
				JasperPrint jp=null;
				String input = "report/addmission_register.jrxml";
				JasperReport com=JasperCompileManager.compileReport(input);
				jp = JasperFillManager.fillReport(com, map, new JRBeanCollectionDataSource(list));
				JasperViewer.viewReport(jp, false);
				list.clear();
			}
			else if(product_category.equals("2"))
			{
				while(a<dps_table.getRowCount())
				{
					map=new HashMap();
					map.put("name", dps_table.getValueAt(a, 2));
					map.put("memberno", dps_table.getValueAt(a, 1));
					map.put("year", dps_table.getValueAt(a, 4));
					map.put("amount", dps_table.getValueAt(a, 3));
					map.put("interest", dps_table.getValueAt(a, 6));
					map.put("total_payable", dps_table.getValueAt(a, 7));
					map.put("date", dps_table.getValueAt(a, 0));
					map.put("mature", dps_table.getValueAt(a, 5));
					
				
					
					map.put("from", c.etob(new SimpleDateFormat("YYYY/MM/dd").format(from_date.getDate())));
					map.put("to", c.etob(new SimpleDateFormat("YYYY/MM/dd").format(to_date.getDate())));
					a++;
					list.add(map);
					
				}
				JasperPrint jp=null;
				String input = "report/bss_addmission.jrxml";
				JasperReport com=JasperCompileManager.compileReport(input);
				jp = JasperFillManager.fillReport(com, map, new JRBeanCollectionDataSource(list));
				JasperViewer.viewReport(jp, false);
				list.clear();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
				ResultSet r=db.sta.executeQuery("select id,savings from member_info where activition='y' && groupno=(select id from group_info where activition='y' && groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) order by id asc;");
				while(r.next())
				{
					model.addRow(new Object[]{r.getString("id"),r.getString("savings"),"0","0","0","0","0","0","0"});
				}
				r.close();
				r=db.sta.executeQuery("select account_id,mendatory_savings_return+additional_savings_return as sav_return from transaction where date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" && mendatory_savings_return+additional_savings_return!=0 && groupno=(select id from group_info where groupno="+cmbgroup.getSelectedItem().toString()+" && product=1)");
				while(r.next())
				{
					model.addRow(new Object[]{r.getString("account_id"),s.name(r.getString("account_id"), "member_info", "savings"),"0","0",r.getString("sav_return"),"0","0","0","0"});
				}
				r.close();
			
			}
			else{
				ResultSet r=db.sta.executeQuery("select name_eng,account from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroup.getSelectedItem().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) order by id asc;");
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

	public void btnwrk()
	{
		btnprint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				printevent();
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
		
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbproduct.getSelectedIndex()!=0 && !from_date.getDate().equals("") && !to_date.getDate().equals(""))
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
		presavings_installment();
		currentsavings_installment();
		table.setFont(new Font("Nirmala UI",Font.PLAIN,13));
		int total1=0,total2=0,total3=0,total4=0,total5=0,total6=0,total7=0,total8=0;
		for(int i=0;i<table.getRowCount();i++)
		{
			int ps=Integer.parseInt(table.getValueAt(i, 1).toString());
			int cs=Integer.parseInt(table.getValueAt(i, 2).toString());
			int ws=Integer.parseInt(table.getValueAt(i, 3).toString());
			int rs=Integer.parseInt(table.getValueAt(i, 4).toString());
			int ins=Integer.parseInt(table.getValueAt(i, 6).toString());
			int cur_ins=Integer.parseInt(table.getValueAt(i, 7).toString());
			total1+=ps;
			total2+=cs;
			total3+=ws;
			total4+=rs;
			total5+=(ps+cs)-(ws+rs);
			total6+=ins;
			total7+=cur_ins;
			total8+=ins-cur_ins;
			table.setValueAt((ps+cs)-(ws+rs), i, 5);
			table.setValueAt(ins-cur_ins, i, 8);
			table.setValueAt(s.name(table.getValueAt(i, 0).toString(),"member_info","account"), i, 0);
			
			table.setValueAt(c.etob(table.getValueAt(i, 0).toString()),i, 0);
			table.setValueAt(c.etob(table.getValueAt(i, 1).toString()),i, 1);
			table.setValueAt(c.etob(table.getValueAt(i, 2).toString()),i, 2);
			table.setValueAt(c.etob(table.getValueAt(i, 3).toString()),i, 3);
			table.setValueAt(c.etob(table.getValueAt(i, 4).toString()),i, 4);
			table.setValueAt(c.etob(table.getValueAt(i, 5).toString()),i, 5);
			table.setValueAt(c.etob(table.getValueAt(i, 6).toString()),i, 6);
			table.setValueAt(c.etob(table.getValueAt(i, 7).toString()),i, 7);
			table.setValueAt(c.etob(table.getValueAt(i, 8).toString()),i, 8);
			
		
		}
		model.addRow(new Object[]{"",c.etob(String.valueOf(total1)),c.etob(String.valueOf(total2)),c.etob(String.valueOf(total3)),c.etob(String.valueOf(total4)),c.etob(String.valueOf(total5)),c.etob(String.valueOf(total6)),c.etob(String.valueOf(total7)),c.etob(String.valueOf(total8))});
	}
	public void currentsavings_installment()
	{
		for(int i=0;i<table.getRowCount();i++)
		{
			try {
				ResultSet r=db.sta.executeQuery("select ifnull((sum(mendatory_savings)+sum(additional_savings)),0) as savings,ifnull(sum(mendatory_Savings_withdraw)+sum(additional_savings_withdraw),0) as withdraw,ifnull(sum(installment)+sum(last_installment),0) as installment from transaction where account_id="+table.getValueAt(i, 0).toString()+" && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+";");
				while(r.next())
				{
					table.setValueAt(r.getString("savings"), i, 2);
					table.setValueAt(r.getString("withdraw"), i, 3);
					table.setValueAt(r.getString("installment"), i, 7);
				}
				r.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void presavings_installment()
	{
		
		for(int i=0;i<table.getRowCount();i++)
		{
			try {
				ResultSet r;
				String s="";
				r=db.sta.executeQuery("select DATE_FORMAT(ifnull(date,20181101),'%Y%m%d')as date,ifnull(cast(((max(loan)*13.5)/100+max(loan)) as unsigned),(select ifnull(loan_installment,0)  from member_info where id="+table.getValueAt(i, 0).toString()+")) as installment from transaction where account_id="+table.getValueAt(i, 0).toString()+" && loan!=0");
			    while(r.next())
			    {
			    	int ins=Integer.parseInt(r.getString("installment"));
			    	s=r.getString("date");
			    	System.out.println(s);
			    	table.setValueAt(ins, i, 6);
			    	
			    }
				r=db.sta.executeQuery("select ifnull(sum(installment)+sum(last_installment),0) as installment from transaction where account_id="+table.getValueAt(i, 0).toString()+" && date>="+s+" && date<"+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+";");
				while(r.next())
				{
					int paid=Integer.parseInt(r.getString("installment"));
					int ins=Integer.parseInt(table.getValueAt(i, 6).toString());
					table.setValueAt(ins-paid, i, 6);
				}
				
				r=db.sta.executeQuery("select ifnull((sum(mendatory_savings)+sum(additional_savings))-(sum(mendatory_savings_withdraw)+sum(additional_savings_withdraw)),0) as savings from transaction where account_id="+table.getValueAt(i, 0).toString()+" && date<"+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+";");
				while(r.next())
				{
					int opening=Integer.parseInt(table.getValueAt(i, 1).toString());
					int savings=Integer.parseInt(r.getString("savings"));
					table.setValueAt(opening+savings,i,1);
					//table.setValueAt(r.getString("installment"), i, 6);
				}
				r.close();
			    
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void reset()
	{
		cmbproduct.setSelectedIndex(0);	
		from_date.setDate(new Date());
		to_date.setDate(new Date());
		model.getDataVector().removeAllElements();
		revalidate();
		dps_model.getDataVector().removeAllElements();
		revalidate();
		
	}
	
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(1200,600));
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
		pnlnorth.setPreferredSize(new Dimension(1200,100));
		pnlnorth.setLayout(new FlowLayout());
		pnlnorth.setBorder(BorderFactory.createLineBorder(Color.black));
		
		pnlnorth.add(lblfrom);
		pnlnorth.add(from_date);

		pnlnorth.add(lblto);
		pnlnorth.add(to_date);
		pnlnorth.add(lblproduct);
		pnlnorth.add(cmbproduct);
		pnlnorth.add(lblgroup);
		pnlnorth.add(cmbgroup);
		pnlnorth.add(btnsearch);
		pnlnorth.add(btnprint);
		from_date.setDate(new Date());
		to_date.setDate(new Date());
	}
	
 	public void generalpnlwrk()
 	{
 		pnlgeneral.setOpaque(false);
 		pnlgeneral.setPreferredSize(new Dimension(1200,490));
 		pnlgeneral.setLayout(new FlowLayout());
 		pnlgeneral.add(scro);
 		scro.setOpaque(false);
 		scro.setPreferredSize(new Dimension(1180,470));
	}
	public void dpspnlwrk()
	{
		pnldps.setOpaque(false);
		pnldps.setPreferredSize(new Dimension(1200,490));
 		pnldps.setLayout(new FlowLayout());
 		pnldps.add(dps_scro);
 		dps_scro.setPreferredSize(new Dimension(1180,470));
 		dps_scro.setOpaque(false);
 		
 		dps_table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
 		//table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
	}
}
