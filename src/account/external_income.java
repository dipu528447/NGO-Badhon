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

public class external_income extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlnorth=new JPanel();
	JPanel pnlsouth=new JPanel();
	JPanel pnlgeneral=new JPanel();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JDateChooser date=new JDateChooser();
	
	JLabel lbldate=new JLabel("Date");
	JLabel lblpurpose=new JLabel("Purpose of:");
	JLabel lblamount=new JLabel("Amount");
	
	JTextField txtamount=new JTextField(20);
	
	JComboBox cmbpurpose=new JComboBox();
	
	JButton btnsubmit=new JButton("Submit");
	JButton btnreset=new JButton("Reset");
	JButton btnsearch=new JButton("Search");
	
	String col[]={"Date","Purpose","Amount"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	search s=new search();
	
	String product_category="";
	public external_income(seasionbean s)
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
		setPreferredSize(new Dimension(600,600));
		background();
		add(pnlmain);
		design();
		btnwrk();
		load();
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
		
		btnsubmit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int a=JOptionPane.showConfirmDialog(null,"Are you sure to create this group?","Confirm",JOptionPane.YES_NO_OPTION);
				if(a==JOptionPane.YES_OPTION)
				{
					try 
					{
						if(!txtamount.getText().equalsIgnoreCase(""))
						{
							int id=new_id();
						
							System.out.println("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from ledger where ledger_name='"+cmbpurpose.getSelectedItem().toString()+"' && activition='y'),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+txtamount.getText()+",0,0,CURRENT_TIMESTAMP(),'1')");
							db.sta.execute("insert into transaction values("+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+",'"+id+"',(select id from ledger where ledger_name='"+cmbpurpose.getSelectedItem().toString()+"' && activition='y'),0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+txtamount.getText()+",0,0,CURRENT_TIMESTAMP(),'1')");
							
						}
						
						JOptionPane.showMessageDialog(null, "Transaction Are Successfully Saved");
						searchevent();
						reset();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
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
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!date.getDate().equals(""))
				{
					searchevent();
				}
				else{
					JOptionPane.showMessageDialog(null, "Please Select The Valid Date");
				}
			}
		});
		
	}
	public void load()
	{
		cmbpurpose.removeAllItems();
		revalidate();
		try {
			cmbpurpose.addItem(" ");
			ResultSet r=db.sta.executeQuery("select ledger_name from ledger where recieve_payment=1 && activition='y'");
			while(r.next())
			{
				cmbpurpose.addItem(r.getString("ledger_name"));
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void searchevent()
	{
		
		try {
				model.getDataVector().removeAllElements();
				revalidate();
				System.out.println("select date,ledger_id,other_transaction from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && other_transaction!=0;");
				ResultSet r=db.sta.executeQuery("select date,ledger_id,other_transaction from transaction where date="+new SimpleDateFormat("YYYYMMdd").format(date.getDate())+" && other_transaction!=0;");
				while(r.next())
				{
					
					model.addRow(new Object[]{r.getString("date"),s.name(r.getString("ledger_id"), "ledger", "ledger_name"),r.getString("other_transaction")});
				}
				r.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reset()
	{
		cmbpurpose.setSelectedIndex(0);

		date.setDate(new Date());
				
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
	
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(600,500));
		pnlmain.setLayout(new BorderLayout());
		pnlmain.add(pnlnorth,BorderLayout.NORTH);
		pnlmain.add(pnlsouth,BorderLayout.CENTER);
		pnlmain.add(pnlgeneral,BorderLayout.SOUTH);
		northpnlwrk();
		southpnlwrk();
		generalpnlwrk();
	}
	public void northpnlwrk()
	{
		pnlnorth.setOpaque(false);
		pnlnorth.setPreferredSize(new Dimension(600,200));
		pnlnorth.setLayout(new GridBagLayout());
		pnlnorth.setBorder(BorderFactory.createLineBorder(Color.black));
		grid.insets=new Insets(5, 5,5,5);
		grid.gridx=0;
		grid.gridy=0;
		pnlnorth.add(lbldate,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnlnorth.add(date,grid);
		grid.gridx=0;
		grid.gridy=1;
		pnlnorth.add(lblpurpose,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnlnorth.add(cmbpurpose,grid);
		grid.gridx=0;
		grid.gridy=2;
		pnlnorth.add(lblamount,grid);
		grid.gridx=1;
		grid.gridy=2;
		pnlnorth.add(txtamount,grid);
		
		
		date.setDate(new Date());
	}
	public void southpnlwrk()
	{
		pnlsouth.setPreferredSize(new Dimension(600,50));
		pnlsouth.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlsouth.setLayout(new FlowLayout());
		pnlsouth.setOpaque(false);
		pnlsouth.add(btnsubmit);
//		pnlsouth.add(btndelete);
//		pnlsouth.add(btnupdate);
		pnlsouth.add(btnreset);
 	}
 	public void generalpnlwrk()
 	{
 		pnlgeneral.setOpaque(false);
 		pnlgeneral.setPreferredSize(new Dimension(600,250));
 		pnlgeneral.setLayout(new FlowLayout());
 		pnlgeneral.add(scro);
 		scro.setOpaque(false);
 		scro.setPreferredSize(new Dimension(580,230));
	}
	
}
