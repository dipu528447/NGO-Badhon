package setup;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import share.convert;
import share.db_connection;
import share.seasionbean;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import com.toedter.calendar.JDateChooser;


public class history extends JFrame{
	seasionbean s;
	Object row[][]={};
	String col[]={"Date","mendatory savings","Additional Savings","installment","last installment","loan","mendatory savings withdraw","Additional Savings withdraw","Mendatory savings return","additional Savings return "};
	DefaultTableModel model=new DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnlcenter=new JPanel();
	JLabel lbluser=new JLabel("");
	db_connection db=new db_connection();
	JDateChooser date1=new JDateChooser();
	JDateChooser date2=new JDateChooser();
	JButton btnsearch=new JButton("খুজুন");
	JButton btnprint=new JButton("প্রিন্ট করুন");
	String group="3",account="24",user="জাবেদ";
	convert c=new convert();
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	GridBagConstraints grid=new GridBagConstraints();
	public history(seasionbean sea,String user)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
		
		s=sea;

		this.user=user;
		
		setSize(1000,700);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("image/Capture.PNG"));
		design();
		
	}
	public void design()
	{
		add(pnlmain);
		pnlmain.setPreferredSize(new Dimension(1000,700));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlsouth,BorderLayout.SOUTH);
		pnlmain.add(pnlcenter,BorderLayout.CENTER);
		
		northpnlwrk();
		southpnlwrk();
		centerpnlwrk();
		
		btnwrk();
	}
	public void northpnlwrk()
	{
		
		pnlnorth.setPreferredSize(new Dimension(1000,100));
		pnlnorth.setLayout(new GridBagLayout());
		lbluser.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
		grid.gridx=0;
		grid.gridy=0;
		pnlnorth.add(date1,grid);
		grid.gridx=1;
		grid.gridy=0;
		JLabel lblto=new JLabel("to");
		grid.gridx=2;
		grid.gridy=0;
		pnlnorth.add(lblto,grid);
		grid.gridx=3;
		grid.gridy=0;
		pnlnorth.add(date2,grid);
		grid.gridx=4;
		grid.gridy=0;
		pnlnorth.add(btnsearch,grid);
		btnsearch.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
		table.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
	}
	public void southpnlwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(1000,50));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.add(btnprint);
		btnprint.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
	}
	public void centerpnlwrk()
	{
		pnlcenter.setPreferredSize(new Dimension(1000,600));
		pnlcenter.setLayout(new FlowLayout());
		lbluser.setFont(new Font("SolaimanLipi",Font.PLAIN,15));
		pnlcenter.add(lbluser);
		pnlcenter.add(scro);
		scro.setPreferredSize(new Dimension(980,570));
	}
	public void btnwrk()
	{
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				searchevent();
			}
		});
		btnprint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				printevent();
			}
		});
	}
	public void printevent() 
	{
		
		try{
			HashMap map=new HashMap();
			for(int i=0;i<table.getRowCount();i++)
			{
				map=new HashMap();
				map.put("user", "জাবেদ চৌধুরী");
				map.put("groupno",c.etob(group));
				map.put("account",c.etob(account));
				map.put("date",table.getValueAt(i, 0));
				map.put("mendatory_savings",table.getValueAt(i, 1));
				map.put("additional_savings",table.getValueAt(i, 2));
				map.put("mendatory_savings_withdraw",table.getValueAt(i, 6));
				map.put("additional_savings_withdraw",table.getValueAt(i, 7));
				map.put("mendatory_savings_return",table.getValueAt(i, 8));
				map.put("additional_savings_return",table.getValueAt(i, 9));
				map.put("loan",table.getValueAt(i, 5));
				map.put("loan_installment",table.getValueAt(i, 3));
				map.put("last_installment",table.getValueAt(i, 4));
				list.add(map);
				
			}
			JasperPrint jp1=null;
			String input1 = "report/history.jrxml";
			JasperReport com1=JasperCompileManager.compileReport(input1);
			jp1 = JasperFillManager.fillReport(com1, map, new JRBeanCollectionDataSource(list));
			JasperViewer.viewReport(jp1, false);
			list.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void searchevent()
	{
		if(!date1.getDate().equals("") && !date2.getDate().equals(""))
		{
			try {
//				ResultSet r1=db.sta.executeQuery("select * from bss_member_info where groupno="+group+" && account="+account+";");
//				int previous=0;
//				while(r1.next())
//				{
//					model.addRow(new Object[]{"à¦ªà§?à¦°à¦¾à¦°à¦®à§?à¦­à¦¿à¦• à¦œà§‡à¦°",c.etob(r1.getString("savings")),"à§¦","à§¦","à§¦","à§¦","à§¦","à§¦","à§¦","à§¦"});
//					previous=Integer.parseInt(r1.getString("savings"));
//				}
				ResultSet r=db.sta.executeQuery("select * from member_info where id="+user+";");
				
				while(r.next())
				{
					model.addRow(new Object[]{"প্রারম্বিক জের",c.etob(r.getString("savings")),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0")});
					

				}
				r.close();
				r=db.sta.executeQuery("select * from transaction where date<"+new SimpleDateFormat("YYYYMMdd").format(date1.getDate())+" &&  account_id="+user+" order by date asc;");
				while(r.next())
				{
					model.setValueAt(c.etob(String.valueOf(Integer.parseInt(model.getValueAt(0, 1).toString())+Integer.parseInt(r.getString("mendatory_savings")))),0 , 1);
				} 
				r=db.sta.executeQuery("select * from transaction where date>="+new SimpleDateFormat("YYYYMMdd").format(date1.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(date2.getDate())+" &&  account_id="+user+" order by date asc;");
				int savings=0,total_savings=0;
				while(r.next())
				{
					model.addRow(new Object[]{c.etob(r.getString("date")),c.etob(r.getString("mendatory_savings")),c.etob(r.getString("additional_savings")),c.etob(r.getString("installment")),c.etob(r.getString("last_installment")),c.etob(r.getString("loan").toString()),c.etob(r.getString("mendatory_savings_withdraw")),c.etob(r.getString("additional_savings_withdraw")),c.etob(r.getString("mendatory_savings_return")),c.etob(r.getString("additional_savings_return"))});
				}
				int mendatory=0,additional=0,mendatory_return=0,additional_return=0,installment=0,last=0;
				int loan=0;
				for(int i=0;i<table.getRowCount();i++)
				{
					mendatory+=Integer.parseInt(table.getValueAt(i, 1).toString());
					additional+=Integer.parseInt(table.getValueAt(i, 2).toString());
					mendatory_return+=Integer.parseInt(table.getValueAt(i, 6).toString());
					additional_return+=Integer.parseInt(table.getValueAt(i, 7).toString());
					loan+=((Integer.parseInt(table.getValueAt(i, 5).toString())*13.5)/100)+Integer.parseInt(table.getValueAt(i, 5).toString());
					installment+=Integer.parseInt(table.getValueAt(i, 3).toString());
					last+=Integer.parseInt(table.getValueAt(i, 4).toString());
				}
				model.addRow(new Object[]{"মোট",c.etob(String.valueOf(mendatory)),c.etob(String.valueOf(additional)),c.etob(String.valueOf(installment)),c.etob(String.valueOf(last)),c.etob(String.valueOf(loan)),c.etob("0"),c.etob("0"),c.etob("0"),c.etob("0")});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}

