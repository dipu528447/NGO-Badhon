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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class group_setup extends JPanel{
	BufferedImage image;
	JPanel pnlmain=new JPanel();
	JPanel pnlwest=new JPanel();
	JPanel pnleast=new JPanel();
	JPanel pnltxt=new JPanel();
	JPanel pnlbtn=new JPanel();
	
	GridBagConstraints grid=new GridBagConstraints();
	
	JLabel lblgroupname=new JLabel("Group Name");
	JLabel lblgroupno=new JLabel("Group No");
	JLabel lblgrouplocation=new JLabel("Group Location");
	JLabel lblgroup_po=new JLabel("Group P.O");
	JLabel lblgroupday=new JLabel("Group Day");
	JLabel lblproduct=new JLabel("Product");
	
	JTextField txtgroupno=new JTextField(15);
	JTextField txtgroupname=new JTextField(15);
	JTextField txtgrouplocation=new JTextField(15);
	JTextField txtsearch=new JTextField(15);
	
	
	String day[]={"","Sat","Sun","Mon","Tue","Wed","Thu"};
	JComboBox cmbday=new JComboBox(day);
	
	JComboBox cmbpo=new JComboBox();
	JComboBox cmbproduct=new JComboBox();
	
	JButton btncreate=new JButton("Create");
	JButton btnreset=new JButton("Reset");
	JButton btndelete=new JButton("Delete");
	JButton btnupdate=new JButton("Update");
	JButton btnsearch=new JButton("Search");
	
	String col[]={"Group No","Group Day","Group P.O","Group location","Product"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	public group_setup(seasionbean s)
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
		setPreferredSize(new Dimension(900,400));
		background();
		add(pnlmain);
		design();
		load();
		btnwrk();
		try{
			ResultSet r=db.sta.executeQuery("select * from product");
			cmbproduct.addItem("");
			while(r.next()){
				cmbproduct.addItem(r.getString("product_name"));
			}
			r.close();
			cmbpo.addItem("");
			r=db.sta.executeQuery("select * from staff");
			while(r.next())
			{
				cmbpo.addItem(r.getString("name"));
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

	public boolean exitance(String group,String product)
	{
		try {
			ResultSet r=db.sta.executeQuery("select groupno from group_info where groupno='"+group+"' && activity='y' && product=(select id from product where product_name='"+product+"')");
			while(r.next())
			{
				if(r.getString("groupno").equalsIgnoreCase(group))
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
				if(valid()==true)
				{
					int id=new_id();
					if(exitance(txtgroupno.getText(),cmbproduct.getSelectedItem().toString())==false)
					{
						int a=JOptionPane.showConfirmDialog(null,"Are you sure to create this group?","Confirm",JOptionPane.YES_NO_OPTION);
						if(a==JOptionPane.YES_OPTION)
						{
							try {
								System.out.println("insert into group_info values("+id+",'"+txtgroupno.getText().trim().toString()+"','"+txtgroupname.getText().trim().toString()+"','"+cmbday.getSelectedItem()+"',(select id from staff where name='"+cmbpo.getSelectedItem().toString()+"'),'"+txtgrouplocation.getText()+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),"+"'y',"+sa.getuserid()+",CURRENT_TIMESTAMP())");
								db.sta.execute("insert into group_info values("+id+",'"+txtgroupno.getText().trim().toString()+"','"+txtgroupname.getText().trim().toString()+"','"+cmbday.getSelectedItem()+"',(select id from staff where name='"+cmbpo.getSelectedItem().toString()+"'),'"+txtgrouplocation.getText()+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),"+"'y',"+sa.getuserid()+",CURRENT_TIMESTAMP())");
								JOptionPane.showMessageDialog(null, "Group is successfully Launched");
							
								load();
								reset();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "This group no is already taken...please try another group no");
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
				
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to delete this group?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							db.sta.execute("update group_info set activity='n' where groupno='"+txtgroupno.getText().trim().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
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
							db.sta.execute("update group_info set group_name='"+txtgroupname.getText().trim().toString()+"',group_day='"+cmbday.getSelectedIndex()+"',p_o=(select id from staff where name='"+cmbpo.getSelectedItem().toString()+"'),group_location='"+txtgrouplocation.getText().trim().toString()+"' where groupno='"+txtgroupno.getText().trim().toString()+"' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && activity='y'");
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
				if(!cmbproduct.getSelectedItem().toString().equalsIgnoreCase("") && !txtsearch.getText().trim().toString().equalsIgnoreCase(""))
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
			System.out.println("select * from group_info where groupno='"+txtsearch.getText().trim().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
			ResultSet r=db.sta.executeQuery("select * from group_info where groupno='"+txtsearch.getText().trim().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
			while(r.next())
			{
				txtgroupno.setText(r.getString("groupno"));
				txtgroupname.setText(r.getString("group_name"));
				txtgrouplocation.setText(r.getString("group_location"));
				cmbday.setSelectedIndex(Integer.parseInt(r.getString("group_day")));
				cmbpo.setSelectedItem(sea.name(r.getString("p_o"),"staff","name"));
				cmbproduct.setSelectedItem(sea.name(r.getString("product"), "product", "product_name"));
			}
			cmbproduct.setEnabled(false);
			txtgroupno.setEnabled(false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reset()
	{
		cmbproduct.setEnabled(true);
		txtgroupno.setEnabled(true);
		txtgrouplocation.setText("");
		txtgroupname.setText("");
		txtgroupno.setText("");
		cmbday.setSelectedIndex(0);
		cmbproduct.setSelectedIndex(0);
		cmbpo.setSelectedIndex(0);
		
		
	}
	public int new_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(id),0)+1 as id from group_info");
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
		if(!txtgroupno.getText().trim().toString().equalsIgnoreCase(""))
		{
			if(!cmbday.getSelectedItem().toString().equals(""))
			{
				if(!txtgroupname.getText().trim().toString().equalsIgnoreCase(""))
				{
					if(!txtgrouplocation.getText().trim().toString().equalsIgnoreCase(""))
					{
						if(!cmbpo.getSelectedItem().toString().equalsIgnoreCase(""))
						{
							return true;
						}
						else{
							JOptionPane.showMessageDialog(null, "Please select the group p.o");
							cmbpo.requestFocus();
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "Please enter the group location");
						txtgrouplocation.requestFocus();
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Please enter the group name");
					txtgroupname.requestFocus();
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please select the group day");
				cmbday.requestFocus();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please enter the group no");
			txtgroupno.requestFocus();
		}
		return false;
	}
	public void load()
	{
		model.getDataVector().removeAllElements();
		revalidate();
		ResultSet r;
		try {
			r = db.sta.executeQuery("select * from group_info where activity='y'");
			while(r.next())
			{
				
				String p_o=sea.name(r.getString("p_o"),"staff","name");
				//System.out.println(r.getString("product"));
				String product=sea.name(r.getString("product"),"product","product_name");
				model.addRow(new Object[]{r.getString("groupno"),r.getString("group_day"),p_o,r.getString("group_location"),product});
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
		pnltxt.add(lblgroupno,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnltxt.add(txtgroupno,grid);
		
		grid.gridx=0;
		grid.gridy=1;
		pnltxt.add(lblgroupname,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnltxt.add(txtgroupname,grid);
		
		grid.gridx=0;
		grid.gridy=2;
		pnltxt.add(lblgroupday,grid);
		grid.gridx=1;
		grid.gridy=2;
		pnltxt.add(cmbday,grid);
		
		grid.gridx=0;
		grid.gridy=3;
		pnltxt.add(lblgrouplocation,grid);
		grid.gridx=1;
		grid.gridy=3;
		pnltxt.add(txtgrouplocation,grid);
		
		grid.gridx=0;
		grid.gridy=4;
		pnltxt.add(lblgroup_po,grid);
		grid.gridx=1;
		grid.gridy=4;
		pnltxt.add(cmbpo,grid);
		
		grid.gridx=0;
		grid.gridy=5;
		pnltxt.add(lblproduct,grid);
		grid.gridx=1;
		grid.gridy=5;
		pnltxt.add(cmbproduct,grid);
		
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
