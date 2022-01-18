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
import share.seasionbean;

public class product_setup extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlwest=new JPanel();
	JPanel pnleast=new JPanel();
	JPanel pnltxt=new JPanel();
	JPanel pnlbtn=new JPanel();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JLabel lblproductname=new JLabel("Product Name");
	JLabel lblproductcategory=new JLabel("Product Category");
	
	
	JTextField txtproductname=new JTextField(10);
	String category[]={"","Savings with loan","D.P.S"};
	JComboBox cmbcategory=new JComboBox(category);
	
	JButton btncreate=new JButton("Create",new ImageIcon("icon/Add.png"));
	JButton btnreset=new JButton("Reset",new ImageIcon("icon/reresh.png"));
	
	String col[]={"Product Name","Product Category"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	seasionbean sa;
	db_connection db=new db_connection();
	public product_setup(seasionbean s)
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
		setPreferredSize(new Dimension(700,400));
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

	public void btnwrk()
	{
		btncreate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true)
				{
					int id=new_id();
					int led_id=led_id();
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to create this product?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							db.sta.execute("insert into product values("+id+",'"+txtproductname.getText().trim().toString()+"',"+cmbcategory.getSelectedIndex()+","+sa.getuserid()+",current_timestamp)");
							db.sta.execute("insert into ledger values("+id+",'"+txtproductname.getText().trim().toString()+"','0','0','0','0',current_timestamp(),'"+sa.getuserid()+"')");
							JOptionPane.showMessageDialog(null, "Product is successfully Launched");
							load();
							reset();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	public void reset()
	{
		txtproductname.setText("");
		cmbcategory.setSelectedIndex(0);
	}
	public int new_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(id),0)+1 as id from product");
			while(r1.next())
			{
				i=Integer.parseInt(r1.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	public int led_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(id),0)+1 as id from ledger");
			while(r1.next())
			{
				i=Integer.parseInt(r1.getString("id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	
	public boolean valid()
	{
		if(!txtproductname.getText().trim().toString().equalsIgnoreCase(""))
		{
			if(!cmbcategory.getSelectedItem().toString().equals("")){
				return true;
			}
			else{
				JOptionPane.showMessageDialog(null, "Please select the product category");
				cmbcategory.requestFocus();
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Please enter the product name");
			txtproductname.requestFocus();
		}
		return false;
	}
	public void load()
	{
		model.getDataVector().removeAllElements();
		revalidate();
		ResultSet r;
		try {
			r = db.sta.executeQuery("select * from product");
			while(r.next())
			{
				String category="";
				if(r.getString("product_category").toString().equalsIgnoreCase("1"))
				{
					category="Savings with loan";
				}
				else if(r.getString("product_category").toString().equalsIgnoreCase("2"))
				{
					category="D.P.S";
				}
				model.addRow(new Object[]{r.getString("product_name"),category});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void design()
	{
		pnlmain.setOpaque(false);
		pnlmain.setPreferredSize(new Dimension(700,400));
		pnlmain.setLayout(new FlowLayout());
		pnlmain.add(pnlwest);
		pnlmain.add(pnleast);
		
		westpnlwrk();
		eastpnlwrk();
	}
	public void westpnlwrk()
	{
		pnlwest.setOpaque(false);
		pnlwest.setPreferredSize(new Dimension(300,320));
		pnlwest.setLayout(new FlowLayout());
	//	pnlwest.setBorder(BorderFactory.createLineBorder(Color.black));
		
		pnlwest.add(pnltxt);
		pnlwest.add(pnlbtn);
		
		pnltxtwrk();
		pnlbtnwrk();
	}
	public void pnltxtwrk()
	{
		pnltxt.setOpaque(false);
		pnltxt.setPreferredSize(new Dimension(250,250));
		//pnltxt.setBorder(BorderFactory.createLineBorder(Color.black));
		pnltxt.setLayout(new GridBagLayout());
		grid.insets=new Insets(4,4,4,4);
		grid.gridx=0;
		grid.gridy=0;
		pnltxt.add(lblproductname,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnltxt.add(txtproductname,grid);
		
		grid.gridx=0;
		grid.gridy=1;
		pnltxt.add(lblproductcategory,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnltxt.add(cmbcategory,grid);
		
	}
	public void pnlbtnwrk()
	{
		pnlbtn.setOpaque(false);
		pnlbtn.setPreferredSize(new Dimension(250,50));
		//pnlbtn.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlbtn.setLayout(new FlowLayout());
		pnlbtn.add(btncreate);
		pnlbtn.add(btnreset);
	}
	public void eastpnlwrk()
	{
		pnleast.setOpaque(false);
	//	pnleast.setBorder(BorderFactory.createLineBorder(Color.black));
		pnleast.setPreferredSize(new Dimension(300,320));
		pnleast.setLayout(new FlowLayout());
		pnleast.add(scro);
		scro.setOpaque(false);
		scro.setPreferredSize(new Dimension(270,250));
		
	}
}
