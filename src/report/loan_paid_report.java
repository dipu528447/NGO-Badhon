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

public class loan_paid_report extends JPanel{
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
	JLabel lblproduct=new JLabel("Product");
	JLabel lblsearch=new JLabel("Search");
	
	JComboBox cmbproduct=new JComboBox();
	
	JButton btnsearch=new JButton("Search"); 
	JButton btnprint=new JButton("Print");
	
	convert c=new convert();
	String col[]={"Date","Group No","Member No","Name","Total"};
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
	public loan_paid_report(seasionbean s)
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
			ResultSet r=db.sta.executeQuery("select * from product where product_category=1");
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
			while(a<table.getRowCount())
			{
				map=new HashMap();
				map.put("date", table.getValueAt(a, 0));
				map.put("name", table.getValueAt(a, 3));
				map.put("groupno", table.getValueAt(a, 1));
				map.put("memberno", table.getValueAt(a, 2));
				map.put("loan_installment", table.getValueAt(a, 4).toString());
				map.put("from", c.etob(new SimpleDateFormat("YYYY-MM-dd").format(from_date.getDate())));
				map.put("to", c.etob(new SimpleDateFormat("YYYY-MM-dd").format(to_date.getDate())));
				a++;
				list.add(map);
				
			}
			JasperPrint jp=null;
			String input = "report/loan_paid_report.jrxml";
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
				if(cmbproduct.getSelectedIndex()!=0 && !from_date.getDate().equals("") && !to_date.getDate().equals(""))
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
				
				ResultSet r=db.sta.executeQuery("select date,groupno,account_id,last_installment from transaction where last_installment!=0 && date>="+new SimpleDateFormat("YYYYMMdd").format(from_date.getDate())+" && date<="+new SimpleDateFormat("YYYYMMdd").format(to_date.getDate())+" && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') order by date asc");
				while(r.next())
				{
					model.addRow(new Object[]{c.etob(r.getString("date")),c.etob(s.name(r.getString("groupno"), "group_info", "groupno")),c.etob(s.name(r.getString("account_id"), "member_info", "account")),s.name(r.getString("account_id"), "member_info", "name_ban"),c.etob(r.getString("last_installment"))});
				}
				r.close();
				model.addRow(new Object[]{"","","","?????????",c.etob(String.valueOf(total(4)))});
				table.setEnabled(false);
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
	
	public void reset()
	{
		cmbproduct.setSelectedIndex(0);	
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
		pnlnorth.add(lblproduct);
		pnlnorth.add(cmbproduct);
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
 		table.setFont(new Font("SolaimanLipi",Font.PLAIN,14));
	}
	
}
