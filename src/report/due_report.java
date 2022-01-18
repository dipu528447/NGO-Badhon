package report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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

public class due_report extends JPanel
{
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	db_connection db=new db_connection();
	seasionbean sa;
	search search=new search();
	JDateChooser date1=new JDateChooser();
	JDateChooser date2=new JDateChooser();
	JLabel lbldate=new JLabel("to");
	JButton btnsearch=new JButton("Search");
	JButton btnprint=new JButton("Print");
	String col[]={"name","group no","member no","installment","Program organizer"};
	Object row[][]={};
	convert c=new convert();
	DefaultTableModel model=new DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	public due_report(seasionbean s)
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
	public void design()
	{
		 pnlmain.setPreferredSize(new Dimension(1200,600));
		 pnlmain.setLayout(new BorderLayout());
		 pnlmain.add(pnlnorth,BorderLayout.NORTH);
		 pnlmain.add(pnlsouth,BorderLayout.SOUTH);
		 pnlmain.setOpaque(false);
		 
		 pnlnorthwrk();
		 pnlsouthwrk();
		 
	}
	public void pnlnorthwrk()
	{
		pnlnorth.setPreferredSize(new Dimension(1200,100));
		pnlnorth.setLayout(new FlowLayout());
		pnlnorth.setOpaque(false);
		pnlnorth.add(date1);
		pnlnorth.add(lbldate);
		pnlnorth.add(date2);
		pnlnorth.add(btnsearch);
		pnlnorth.add(btnprint);
	}
	public void pnlsouthwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(1200,500));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.add(scro);
		pnlsouth.setOpaque(false);
		table.setFont(new Font("Nirmala UI",Font.PLAIN,12));
		scro.setPreferredSize(new Dimension(1100,450));
		
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
				try{
					HashMap map=null;
					int a=0;
					
						while(a<table.getRowCount())
						{
							map=new HashMap();
							map.put("month", new SimpleDateFormat("MMM-YYYY").format(date1.getDate()));
							map.put("name", table.getValueAt(a, 0));
							map.put("group", table.getValueAt(a,1));
							map.put("member", table.getValueAt(a,2));
							map.put("due", table.getValueAt(a, 3));
							map.put("po", table.getValueAt(a, 4));
							
							a++;
							list.add(map);
							
						}
						JasperPrint jp=null;
						String input = "report/due.jrxml";
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
		});
	}
	public void searchevent()
	{ 
		model.getDataVector().removeAllElements();
		revalidate();
		try {
			ResultSet r=db.sta.executeQuery("select * from member_info where product=1 && activition='y' && loan!=0 order by id");
			while(r.next())
			{
				model.addRow(new Object[]{r.getString("name_ban"),search.name(r.getString("groupno"), "group_info", "groupno"),r.getString("account"),"20181101,"+r.getString("loan_installment"),r.getString("id")});
				//model.addRow(new Object[]{r.getString("name_ban"),r.getString("groupno"),r.getString("account"),"2018-11-01",r.getString("id")});
			}
			r.close();
			for(int i=0;i<table.getRowCount();i++)
			{
				r=db.sta.executeQuery("select ifnull(Date_format(max(date),'%Y%m%d'),'20181101')as date,cast(ifnull(loan+((loan*13.5)/100),0)as unsigned) as ins from transaction where account_id="+table.getValueAt(i, 4).toString()+" && loan!=0 && product=1");
				while(r.next())
				{
					if(!r.getString("date").equals("20181101"))
					{
						table.setValueAt(r.getString("date")+","+r.getString("ins"), i, 3);
					}
				}
				r.close();
			}
			
			for(int i=0;i<table.getRowCount();i++)
			{
				if(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(',')+1,table.getValueAt(i, 3).toString().length()).equals("0"))
				{
					model.removeRow(i);	
					i=0;
				}
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				String p="select count(date) as cont from day_history where date>"+table.getValueAt(i, 3).toString().substring(0,8)+" && date<="+new SimpleDateFormat("YYYYMMdd").format(date2.getDate())+" && day_name=(select group_day from group_info where groupno="+table.getValueAt(i, 1).toString()+" && product=1)";
				System.out.println(p);
				r=db.sta.executeQuery(p);
				while(r.next())
				{
					String o=r.getString("cont").toString();
					System.out.println(table.getValueAt(i, 4).toString()+","+o);
					String w="";
					if(!table.getValueAt(i, 3).toString().substring(0,8).equals("20181101") && !o.equals("0"))
					{
						o=String.valueOf(Integer.parseInt(o));
						System.out.println(table.getValueAt(i, 4).toString()+","+o);
					}
					if(o.length()==1)
					{
						w="0"+o;
					}
					else{
						w=o;
					}
					
					
					//System.out.println(r.getString("count(date)").toString().length());
					table.setValueAt(w+"/"+table.getValueAt(i, 3).toString(),i,3);
				}
				r.close();
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				if(Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf('/')))<=0)
				{
					model.removeRow(i);	
					i=0;
				}
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				String p="select (cast((loan)/1000 as unsigned)*25)*"+table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf("/"))+" as ins from member_info where id="+table.getValueAt(i,4).toString()+";";
				System.out.println(p);
				r=db.sta.executeQuery(p);
				while(r.next())
				{
					table.setValueAt(r.getString("ins")+" "+table.getValueAt(i, 3).toString(), i, 3);
				}
				r.close();
			}
			
			for(int i=0;i<table.getRowCount();i++)
			{
				if(Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(' ')))>Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(',')+1,table.getValueAt(i, 3).toString().length())))
				{
					model.removeRow(i);	
					i=0;
				}
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				//int current_installment=Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(".")+1,table.getValueAt(i, 3).toString().toString().indexOf(':')));
				int current_installment=Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(" ")))>0?Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(" "))):Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(",")+1),table.getValueAt(i, 3).toString().length());
				System.out.println(current_installment);
				table.setValueAt(current_installment+"L"+table.getValueAt(i, 3).toString(), i, 3);
				
			}
			for(int i=0;i<table.getRowCount();i++)	
			{
				String s="select ifnull(sum(installment)+sum(last_installment),0) as installment from transaction where date>="+table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf("/")+1,table.getValueAt(i, 3).toString().indexOf(","))+" && date<="+new SimpleDateFormat("YYYYMMdd").format(date2.getDate())+" && account_id="+table.getValueAt(i,4).toString()+";";
				//System.out.println(s);
				r=db.sta.executeQuery(s);
			
				while(r.next())
				{
					int pay=Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf("L")));
					int paid=Integer.parseInt(r.getString("installment"));
					//table.setValueAt(table.getValueAt(i, 3).toString()+"."+String.valueOf(pay-paid)+":"+String.valueOf(paid), i, 3);
					table.setValueAt(String.valueOf(pay-paid),i, 3);
				}
				r.close();
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				if(Integer.parseInt(table.getValueAt(i, 3).toString())<=0)
				{
					model.removeRow(i);	
					i=0;
				}
			}
////			for(int i=0;i<table.getRowCount();i++)
////			{
////				String p="select (cast((loan)/1000 as unsigned)*25)*"+table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf("/"))+" as ins from member_info where id="+table.getValueAt(i,4).toString()+";";
////				System.out.println(p);
////				r=db.sta.executeQuery(p);
////				while(r.next())
////				{
////					table.setValueAt(r.getString("ins")+" "+table.getValueAt(i, 3).toString(), i, 3);
////				}
////				r.close();
////			}
////			for(int i=0;i<table.getRowCount();i++)
////			{
////				int payable=Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(' ')));
////				int ins=Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(',')+1,table.getValueAt(i, 3).toString().indexOf('.')));
////				if(payable>=ins)
////				{
////					model.removeRow(i);
////					i=0;
////				}
////			}
////			for(int i=0;i<table.getRowCount();i++)
////			{
////				int payable=Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(' ')));
////				int ins=Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(':')+1,table.getValueAt(i, 3).toString().length()));
////				if(payable-ins<=0)
////				{
////					model.removeRow(i);
////					i=0;
////				}
////				
////			}
////			for(int i=0;i<table.getRowCount();i++)
////			{
////
////				int payable=Integer.parseInt(table.getValueAt(i, 3).toString().substring(0,table.getValueAt(i, 3).toString().indexOf(' ')));
////				int ins=Integer.parseInt(table.getValueAt(i, 3).toString().substring(table.getValueAt(i, 3).toString().indexOf(':')+1,table.getValueAt(i, 3).toString().length()));
////				table.setValueAt(String.valueOf(payable-ins), i, 3);
////				table.setValueAt("", i, 4);
////			}
	
			for(int i=0;i<table.getRowCount();i++)
			{

				if(table.getValueAt(i,3).toString().equals("0"))
				{
					model.removeRow(i);
				}
			}
			
			int total=0;
			for(int i=0;i<table.getRowCount();i++)
			{
				total+=Integer.parseInt(table.getValueAt(i, 3).toString());
			}
			
			for(int i=0;i<table.getRowCount();i++)
			{
				 r=db.sta.executeQuery("select p_o from group_info where groupno="+table.getValueAt(i, 1).toString()+" && product=1");
				 while(r.next())
				 {
					 table.setValueAt(search.name(r.getString("p_o"),"staff","name"), i, 4);
				 }
			}
			for(int i=0;i<table.getRowCount();i++)
			{
				table.setValueAt(c.etob(table.getValueAt(i, 1).toString()),i,1);
				table.setValueAt(c.etob(table.getValueAt(i, 2).toString()),i,2);
				table.setValueAt(c.etob(table.getValueAt(i, 3).toString()),i,3);
			}
			model.addRow(new Object[]{"","","মোট",c.etob(String.valueOf(total)),""});
			} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
