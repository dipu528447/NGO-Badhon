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

public class recieve_payment extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	//JPanel pnldps=new JPanel();
	JPanel pnlgeneral=new JPanel();
	
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser from_date=new JDateChooser();
	JDateChooser to_date=new JDateChooser();

	JLabel lblfrom=new JLabel("From");
	JLabel lblto=new JLabel("To");
	JLabel lblsearch=new JLabel("Search");
	
	JButton btnsearch=new JButton("Search"); 
	JButton btnprint=new JButton("Print");
	
	convert c=new convert();
	String col[]={"Purpose","Amount"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
//	String dps_col[]={"Date","Account","Name","Total"};
//	Object dps_row[][]={};
//	javax.swing.table.DefaultTableModel dps_model=new javax.swing.table.DefaultTableModel(dps_row,dps_col);
//	JTable dps_table=new JTable(dps_model);
//	JScrollPane dps_scro=new JScrollPane(dps_table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//	
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	DecimalFormat df=new DecimalFormat();
	search s=new search();
	
	//String product_category="";
	public recieve_payment(seasionbean s)
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
			while(a<table.getRowCount())
			{
				map=new HashMap();
				map.put("purpose", table.getValueAt(a, 0));
				map.put("amount", table.getValueAt(a, 1));
				map.put("month",new SimpleDateFormat("MMM-YYYY").format(to_date.getDate()));
				a++;
				list.add(map);
			}
			JasperPrint jp=null;
			String input = "report/recieve_paymentg.jrxml";
			JasperReport com=JasperCompileManager.compileReport(input);
			jp = JasperFillManager.fillReport(com, map, new JRBeanCollectionDataSource(list));
			JasperViewer.viewReport(jp, false);
			list.clear();	
		}
		catch(Exception e)
		{
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
			
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!from_date.getDate().equals("") && !to_date.getDate().equals(""))
				{
					searchevent();
				}
			}
		});
		
	}
	public void searchevent()
	{
		try {
			if(!from_date.getDate().equals("") && !to_date.getDate().equals(""))
			{
				model.getDataVector().removeAllElements();
				revalidate();
				model.addRow(new Object[]{"Recieved",""});
				ResultSet r=db.sta.executeQuery("select count(id),product from member_info where addmission_date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && addmission_date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product");
				while(r.next())
				{
					String product=s.name(r.getString("product"), "product","product_name");
					String category=s.name(r.getString("product"), "product", "product_category");
					int addmission=category.equals("1")?Integer.parseInt(r.getString("count(id)"))*10:Integer.parseInt(r.getString("count(id)"))*50;
					model.addRow(new Object[]{product +" Addmission Fee",addmission});
					
					if(category.equals("1")){
						model.addRow(new Object[]{product +" Passbook Fee",addmission});
					}
				}
				r.close();
				System.out.println("select sum(duplicate_passbook) duplicate_passbook,sum(loan) as loan,sum(mendatory_savings)+sum(additional_savings) savings,cast((((sum(installment)/25)*22)+sum(last_installment)) as unsigned) as installment,cast((sum(installment)+sum(last_installment))-(((sum(installment)/25)*22)+sum(last_installment)) as unsigned) as last_installment,sum(dps_savings) as dps,sum(dps_duplicate_passbook) as dps_duplicate_passbook,product from transaction where date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" && product!=0 group by product");
				r=db.sta.executeQuery("select sum(duplicate_passbook) duplicate_passbook,sum(loan) as loan,sum(mendatory_savings)+sum(additional_savings) savings,cast((((sum(installment)/25)*22)+sum(last_installment)) as unsigned) as installment,cast((sum(installment)+sum(last_installment))-(((sum(installment)/25)*22)+sum(last_installment)) as unsigned) as last_installment,sum(dps_savings) as dps,sum(dps_duplicate_passbook) as dps_duplicate_passbook,product from transaction where date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" && product!=0 group by product");
				while(r.next())
				{
					String product=s.name(r.getString("product"), "product","product_name");
					String category=s.name(r.getString("product"), "product", "product_category");
					int riskfund=Integer.parseInt(r.getString("loan"))/100;
					if(category.equals("1"))
					{
						model.addRow(new Object[]{product +" Duplicate Passbook Fee",r.getString("duplicate_passbook")});
						model.addRow(new Object[]{product +" Risk Fund",riskfund});
						model.addRow(new Object[]{product +" Savings",r.getString("savings")});
						model.addRow(new Object[]{product +" Installment",r.getString("installment")});
						model.addRow(new Object[]{product +" Service Charge",r.getString("last_installment")});
					}
					else{
						model.addRow(new Object[]{product +" Duplicate Passbook Fee",r.getString("dps_duplicate_passbook")});
						model.addRow(new Object[]{product +" Savings",r.getString("dps")});
					}
				}
				r.close();
				r=db.sta.executeQuery("select count(transaction_id) as loanfee,product from transaction where loan!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product;");
				while(r.next())
				{
					String product=s.name(r.getString("product"), "product","product_name");
					int loanfee=Integer.parseInt(r.getString("loanfee"))*5;
					model.addRow(new Object[]{product +" loan fee",loanfee});
				}
				r.close();
				r=db.sta.executeQuery("select count(transaction_id) as dropout,product from transaction where mendatory_Savings_return+additional_savings_return!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product;");
				while(r.next())
				{
					String product=s.name(r.getString("product"), "product","product_name");
					int loanfee=Integer.parseInt(r.getString("dropout"))*10;
					model.addRow(new Object[]{product +" Drop Out Fee",loanfee});
				}
				r.close();
				r=db.sta.executeQuery("select count(transaction_id) as dropout,product from transaction where dps_return!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product;");
				while(r.next())
				{
					String product=s.name(r.getString("product"), "product","product_name");
					int loanfee=Integer.parseInt(r.getString("dropout"))*100;
					model.addRow(new Object[]{product +" Drop Out Fee",loanfee});
				}
				r.close();
				r=db.sta.executeQuery("select sum(other_transaction) as amount,ledger_id from transaction where other_transaction!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by ledger_id");
				while(r.next())
				{
					String category=s.name(r.getString("ledger_id"), "ledger","recieve_payment");
					if(category.equals("1") && !r.getString("ledger_id").equals("9"))
					{
						String name=s.name(r.getString("ledger_id"), "ledger","ledger_name");
						model.addRow(new Object[]{name,r.getString("amount")});
					}
				}
				r.close();
				model.addRow(new Object[]{"TOTAL RECIEVED",total(1,1)});
				model.addRow(new Object[]{"",""});
				model.addRow(new Object[]{"Payment",""});
				int pay=table.getRowCount();
				r=db.sta.executeQuery("select sum(loan) as loan,product from transaction where loan!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product");
				while(r.next())
				{
					String name=s.name(r.getString("product"), "product","product_name");
					model.addRow(new Object[]{name +" Loan Distributed",r.getString("loan")});
				}
				r.close();
				r=db.sta.executeQuery("select sum(mendatory_savings_withdraw)+sum(additional_Savings_withdraw)+sum(mendatory_savings_return)+sum(additional_Savings_return) as withdraw,product from transaction where mendatory_savings_withdraw+additional_Savings_withdraw+mendatory_savings_return+additional_Savings_return!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product");
				while(r.next())
				{
					String name=s.name(r.getString("product"), "product","product_name");
					model.addRow(new Object[]{name +" Savings Withdraw and Return",r.getString("withdraw")});
				}
				r.close();
				r=db.sta.executeQuery("select sum(dps_return) as withdraw,product from transaction where dps_return!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by product");
				while(r.next())
				{
					String name=s.name(r.getString("product"), "product","product_name");
					model.addRow(new Object[]{name +" Savings Return",r.getString("withdraw")});
				}
				r.close();
				r=db.sta.executeQuery("select sum(other_transaction) as amount,ledger_id from transaction where other_transaction!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" group by ledger_id");
				while(r.next())
				{
					String category=s.name(r.getString("ledger_id"), "ledger","recieve_payment");
					if(category.equals("2") && !r.getString("ledger_id").equals("7"))
					{
						String name=s.name(r.getString("ledger_id"), "ledger","ledger_name");
						model.addRow(new Object[]{name,r.getString("amount")});
					}
				}
				model.addRow(new Object[]{"TOTAL PAYMENT",total(pay,1)});
				r.close();
				table.setEnabled(false);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int total(int start,int col)
	{
		int t=0;
		for(int a=start;a<table.getRowCount();a++)
		{
			t+=Integer.parseInt((table.getValueAt(a, col).toString()));
		}
		return t;
	}
	
	public void reset()
	{
		
		from_date.setDate(new Date());
		to_date.setDate(new Date());
		model.getDataVector().removeAllElements();
		revalidate();
		
	}
	
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(1200,600));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlgeneral,BorderLayout.CENTER);
		northpnlwrk();
		generalpnlwrk();
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
 	//	table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
	}
	
}
