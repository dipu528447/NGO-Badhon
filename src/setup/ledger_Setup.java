package setup;

import groovy.model.DefaultTableModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import share.db_connection;
import share.search;
import share.seasionbean;

public class ledger_Setup extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlwest=new JPanel();
	JPanel pnleast=new JPanel();
	JPanel pnltxt=new JPanel();
	JPanel pnlbtn=new JPanel();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JLabel lblledgername=new JLabel("Ledger Name");
	JLabel lblbalancesheet=new JLabel("In Balance Sheet");
	JLabel lblincome_expendure=new JLabel("In Income and Expendure");
	JLabel lblrecieve_payment=new JLabel("In Recieve and Payment");
	
	JTextField txtledgername=new JTextField(15);
	JTextField txtsearch=new JTextField(15);
	
	String recieve_payment[]={"","Credit","Debit"};
	
	String balance_sheet[]={"","Asset","libilities"};
	
	String income_expendure[]={"","income","expendure"};
	
	JComboBox cmbrecieve_payment=new JComboBox(recieve_payment);
	JComboBox cmbbalance_sheet=new JComboBox(balance_sheet);
	JComboBox cmbincome_payment=new JComboBox(income_expendure);
	
	JButton btncreate=new JButton("Create");
	JButton btnreset=new JButton("Reset");
	JButton btndelete=new JButton("Delete");
	JButton btnupdate=new JButton("Update");
	JButton btnsearch=new JButton("Search");
	
	String col[]={"Ledger Name","Recieve-Payment","Income and Expendure","Balance Sheet"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	public ledger_Setup(seasionbean s)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sa=s;
		setVisible(true);
		setPreferredSize(new Dimension(900,400));
		setOpaque(false);
		background();
		add(pnlmain);
		design();
		load();
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

	public boolean exitance(String ledger)
	{
		try {
			ResultSet r=db.sta.executeQuery("select ledger_name from ledger where ledger_name='"+ledger+"' && activition='y'");
			while(r.next())
			{
				if(r.getString("ledger_name").equalsIgnoreCase(ledger))
				{
					r.close();
					return true;
				}
			}
			r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void btnwrk()
	{
		btncreate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true && exitance(txtledgername.getText().trim().toString())==false)
				{
					int id=new_id();
						int a=JOptionPane.showConfirmDialog(null,"Are you sure to create this ledger?","Confirm",JOptionPane.YES_NO_OPTION);
						if(a==JOptionPane.YES_OPTION)
						{
							try {
								db.sta.execute("insert into ledger values("+id+",'"+txtledgername.getText().trim().toString()+"','"+cmbbalance_sheet.getSelectedIndex()+"','"+cmbrecieve_payment.getSelectedIndex()+"','"+cmbincome_payment.getSelectedIndex()+"','y',current_timestamp(),'"+sa.getuserid()+"')");
								JOptionPane.showMessageDialog(null, "Ledger is successfully Launched");
								load();
								reset();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					else{
						JOptionPane.showMessageDialog(null, "This ledger is already taken...please try another ledger");
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
		btndelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true)
				{
				
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to delete this ledger?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							db.sta.execute("update ledger set activition='n' where ledger_name='"+txtledgername.getText().trim().toString()+"' && activition='y'");
							reset();
							load();
							JOptionPane.showMessageDialog(null, "This Group is successfully deleted");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		btnupdate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true)
				{
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to update this group?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							db.sta.execute("update ledger set balance_sheet='"+cmbbalance_sheet.getSelectedIndex()+"',recieve_payment='"+cmbrecieve_payment.getSelectedIndex()+"',income_expendure='"+cmbincome_payment.getSelectedIndex()+"' where ledger_name='"+txtledgername.getText().trim().toString()+"' && activition='y'");
							reset();
							load();
							JOptionPane.showMessageDialog(null, "This Group is successfully Updated");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		btnsearch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!txtsearch.getText().trim().toString().equalsIgnoreCase(""))
				{
					searchevent(txtsearch.getText().trim().toString());
				}
				else{
					JOptionPane.showMessageDialog(null, "please enter the ledger name in search field");
					txtledgername.requestFocus();
				}
			}
		});
	}
	public void searchevent(String ledger)
	{
		try {
		
			ResultSet r=db.sta.executeQuery("select * from ledger where activition='y' && ledger_name='"+ledger+"';");
			while(r.next())
			{
				txtledgername.setText(r.getString("ledger_name"));
				cmbbalance_sheet.setSelectedIndex(Integer.parseInt(r.getString("balance_sheet")));
				cmbincome_payment.setSelectedIndex(Integer.parseInt(r.getString("income_expendure")));
				cmbrecieve_payment.setSelectedIndex(Integer.parseInt(r.getString("recieve_payment")));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reset()
	{
		cmbbalance_sheet.setSelectedIndex(0);
		cmbincome_payment.setSelectedIndex(0);
		cmbrecieve_payment.setSelectedIndex(0);
		txtledgername.setText("");
	}
	public int new_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(id),0)+1 as id from ledger");
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
	public boolean valid()
	{
		if(!txtledgername.getText().trim().toString().equalsIgnoreCase(""))
		{
			if(cmbbalance_sheet.getSelectedIndex()!=0)
			{
				if(cmbincome_payment.getSelectedIndex()!=0)
				{
					if(cmbrecieve_payment.getSelectedIndex()!=0)
					{
						return true;
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please Enter The Type in Recieve Payment");
						cmbrecieve_payment.requestFocus();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please Enter The Type in Income and Expendure");
					cmbincome_payment.requestFocus();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please Enter The Type in Balance sheet");
				cmbbalance_sheet.requestFocus();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please Enter The Ledger Name");
			txtledgername.requestFocus();
		}
		return false;
	}
	public void load() 
	{
		model.getDataVector().removeAllElements();
		revalidate();
		ResultSet r;
		try {
			r = db.sta.executeQuery("select * from ledger where activition='y' && balance_sheet!=0 && income_expendure!=0 && recieve_payment!=0;");
			while(r.next())
			{
				String ledgername=r.getString("ledger_name");
				String balancesheet=balance_sheet[Integer.parseInt(r.getString("balance_sheet"))];
				String recievepayment=recieve_payment[Integer.parseInt(r.getString("recieve_payment"))];
				String incomeexpendure=income_expendure[Integer.parseInt(r.getString("income_expendure"))];
				model.addRow(new Object[]{ledgername,recievepayment,incomeexpendure,balancesheet});
			}
		
		r.close();
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(900,400));
		pnlmain.setLayout(new FlowLayout());
		pnlmain.add(pnlwest);
		pnlmain.add(pnleast);
		
		westpnlwrk();
		eastpnlwrk();
	}
	public void westpnlwrk()
	{
		pnlwest.setOpaque(false);
		pnlwest.setPreferredSize(new Dimension(350,320));
		pnlwest.setLayout(new FlowLayout());
		pnlwest.setBorder(BorderFactory.createLineBorder(Color.black));
		
		pnlwest.add(pnltxt);
		pnlwest.add(pnlbtn);
		
		pnltxtwrk();
		pnlbtnwrk();
	}
	public void pnltxtwrk()
	{
		pnltxt.setOpaque(false);
		pnltxt.setPreferredSize(new Dimension(350,250));
		pnltxt.setBorder(BorderFactory.createLineBorder(Color.black));
		pnltxt.setLayout(new GridBagLayout());
		grid.insets=new Insets(4,4,4,4);
		grid.gridx=0;
		grid.gridy=0;
		pnltxt.add(lblledgername,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnltxt.add(txtledgername,grid);
		
		grid.gridx=0;
		grid.gridy=1;
		pnltxt.add(lblrecieve_payment,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnltxt.add(cmbrecieve_payment,grid);
		
		grid.gridx=0;
		grid.gridy=2;
		pnltxt.add(lblbalancesheet,grid);
		grid.gridx=1;
		grid.gridy=2;
		pnltxt.add(cmbbalance_sheet,grid);
		
		grid.gridx=0;
		grid.gridy=3;
		pnltxt.add(lblincome_expendure,grid);
		grid.gridx=1;
		grid.gridy=3;
		pnltxt.add(cmbincome_payment,grid);
		
	}
	public void pnlbtnwrk()
	{
		pnlbtn.setOpaque(false);
		pnlbtn.setPreferredSize(new Dimension(350,50));
		pnlbtn.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlbtn.setLayout(new FlowLayout());
		pnlbtn.add(btncreate);
		pnlbtn.add(btnreset);
		pnlbtn.add(btndelete);
		pnlbtn.add(btnupdate);
	}
	public void eastpnlwrk()
	{
		pnleast.setOpaque(false);
		pnleast.setBorder(BorderFactory.createLineBorder(Color.black));
		pnleast.setPreferredSize(new Dimension(500,320));
		pnleast.setLayout(new FlowLayout());
		pnleast.add(scro);
		scro.setOpaque(false);
		scro.setPreferredSize(new Dimension(470,250));
		pnleast.add(txtsearch);
		pnleast.add(btnsearch);
		
	}
}
