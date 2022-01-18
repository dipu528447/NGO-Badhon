package setup;

import groovy.model.DefaultTableModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import com.toedter.calendar.JDateChooser;

import share.db_connection;
import share.root_frame;
import share.search;
import share.seasionbean;

public class admission2 extends JPanel{
	
	File file,file2;
	
	BufferedImage image;
	Image images;
	JPanel pnlmain=new JPanel();
	JPanel pnlwest=new JPanel();
	JPanel pnleast=new JPanel();
	JPanel pnltxt=new JPanel();
	JPanel pnlbtn=new JPanel();
	JPanel pnlpic=new JPanel();
	GridBagConstraints grid=new GridBagConstraints();
	search s=new search();
	JLabel lblnameinenglish=new JLabel("Name in English");
	JLabel lblnameinbangla=new JLabel("Name in Bangla");
	JLabel lblgroup=new JLabel("Group No");
	JLabel lblsavings=new JLabel("Savings");
	JLabel lblloan=new JLabel("Loan");
	JLabel lblmember_no=new JLabel("Member No");
	JLabel lblloan_installment=new JLabel("Loan Installment");
	JLabel lbldps_category=new JLabel("D.P.S Category");
	JLabel lbldps_savings=new JLabel("D.P.S Savings");
	JLabel lbldps_year=new JLabel("D.P.S Year");
	JLabel lblproduct=new JLabel("Product");
	JLabel lblformpic=new JLabel();
	JLabel lblidpic=new JLabel();
	JLabel lbljoindate=new JLabel("Addmission Date");
	JLabel lblnomini=new JLabel("Nominee name");
	JLabel lblrltn=new JLabel("Relationship ");
	JLabel lblfathername=new JLabel("Father name");
	JLabel lblhusbandname=new JLabel("Husband name");
	JLabel lblmothername=new JLabel("Mother name");
	JLabel lblage=new JLabel("age");
	JLabel lbloccupation=new JLabel("Occupation");
	JLabel lblcontact=new JLabel("contact");
	JLabel lblmendatory=new JLabel("Mendatory");
	JLabel lbladditional=new JLabel("Additional");
	JLabel lblmobile=new JLabel("Mobile");
	JLabel lbldue=new JLabel("Due");
	String user_id="";
	List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	JTextField txtmemberno=new JTextField(15);
	JTextField txtnameinenglish=new JTextField(15);
	JTextField txtsavings=new JTextField(15);
	JTextField txtloan=new JTextField(15);
	JTextField txtloaninstallment=new JTextField(15);
	JTextField txtnameinbangla=new JTextField(15);
	JTextField txtdpssavings=new JTextField(15);
	JTextField txtnomini=new JTextField(15);
	JTextField txtnominirltn=new JTextField(15);
	JTextField txtfathername=new JTextField(15);
	JTextField txtmothername=new JTextField(15);
	JTextField txthusbandname=new JTextField(15);
	JTextField txtage=new JTextField(15);
	JTextField txtoccupation=new JTextField(15);
	JTextField txtcontactno=new JTextField(15);
	JTextField txtmendatory=new JTextField(15);
	JTextField txtadditional=new JTextField(15);
	JTextField txtdue=new JTextField(15);
	JTextField txtmobile=new JTextField(15);
	
	JTextField txtsearch=new JTextField(15);
	
	search search=new search();
	JComboBox cmbgroupno=new JComboBox();
	JComboBox cmbproduct=new JComboBox();
	
	JTextField txtcategory=new JTextField(15);
	JTextField txtdpsyear=new JTextField(15);
	
	JButton btncreate=new JButton("Create");
	JButton btnreset=new JButton("Reset");
	JButton btndelete=new JButton("Delete");
	JButton btnupdate=new JButton("Update");
	JButton btnsearch=new JButton("Search");
	JButton btnupload1=new JButton("Upload form Photo");
	JButton btnupload2=new JButton("Upload NID Photo");
	JButton btnprint=new JButton("Print pic");
	JButton btnprintid=new JButton("Print id");
	JButton btnhistory=new JButton("statement");
	
	String col[]={"Group No","Member no","Name"};
	Object row[][]={};
	javax.swing.table.DefaultTableModel model=new javax.swing.table.DefaultTableModel(row,col);
	JTable table=new JTable(model);
	JScrollPane scro=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JDateChooser joindate=new JDateChooser();
	
	search sea=new search();
	seasionbean sa;
	db_connection db=new db_connection();
	
	JFileChooser choser;
	JFrame fra;
	String form_pic=" ",id_pic=" ";
	public admission2(seasionbean s,JFrame f)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fra=f;
		sa=s;
		setVisible(true);
		setPreferredSize(new Dimension(1250,850));
		setOpaque(false);
		background();
		add(pnlmain);
		design();
	//	load();
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
		reset();
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

	public boolean exitance(String group,String product,String account)
	{
		try {
			ResultSet r=db.sta.executeQuery("select account from member_info where groupno=(select id from group_info where groupno='"+group+"' && product=(select id from product where product_name='"+product+"') ) && activition='y' && product=(select id from product where product_name='"+product+"') && account="+account+";");
			while(r.next())
			{
				if(r.getString("account").equalsIgnoreCase(account))
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
		txtdpssavings.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				char a=arg0.getKeyChar();
				if((a==arg0.VK_BACK_SPACE || a==arg0.VK_DELETE || a>=arg0.VK_0 || a<=arg0.VK_9 ))
				{
					txtdpssavings.setEnabled(true);
				}
				else{
					txtdpssavings.setEnabled(false);
				}
			}
			
		});
		cmbproduct.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				cmbgroupno.removeAllItems();
				revalidate();
				if(cmbproduct.getSelectedIndex()!=0)
				{
					try{
						cmbgroupno.addItem("");
						ResultSet r=db.sta.executeQuery("select * from group_info where product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && activity='y'");
						while(r.next())
						{
							cmbgroupno.addItem(r.getString("groupno"));
						}
						r.close();
						r=db.sta.executeQuery("select product_category from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'");
						while(r.next())
						{
							if(r.getString("product_category").toString().equalsIgnoreCase("1"))
							{
								txtdpsyear.setEnabled(false);
								txtcategory.setEnabled(false);
								txtdpssavings.setEnabled(false);
							}
							else{
								txtdpsyear.setEnabled(true);
								txtcategory.setEnabled(true);
								txtdpssavings.setEnabled(true);
							}
						}
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		
		});
		cmbgroupno.addFocusListener(new FocusAdapter()  {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(cmbproduct.getSelectedIndex()!=0 && cmbgroupno.getSelectedIndex()!=0){
					load();
				}
				else{
					JOptionPane.showMessageDialog(null, "Please select the product and group no");
				}
			}
		});
		btncreate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(valid()==true)
				{
					int id=new_id();
					
					if(exitance(cmbgroupno.getSelectedItem().toString(),cmbproduct.getSelectedItem().toString(),txtmemberno.getText())!=true)
					{
						int a=JOptionPane.showConfirmDialog(null,"Are you sure to add this member in this group?","Confirm",JOptionPane.YES_NO_OPTION);
						if(a==JOptionPane.YES_OPTION)
						{
							try {
								System.out.println("insert into member_info values('"+id+"',(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"'  &&  product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),'"+txtmemberno.getText().trim().toString()+"','"+txtnameinenglish.getText().trim().toString()+"','"+txtnameinbangla.getText().trim().toString()+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),"+txtsavings.getText()+","+txtloan.getText().trim().toString()+","+txtloaninstallment.getText().trim().toString()+","+txtdpsyear.getText().toString()+","+txtcategory.getText().toString()+","+txtdpssavings.getText().trim().toString()+","+new SimpleDateFormat("YYYYMMdd").format(joindate.getDate())+",'y','G:','"+form_pic+"','"+id_pic+"','"+txtfathername.getText().trim().toString()+"','"+txthusbandname.getText().trim().toString()+"','"+txtmothername.getText().trim().toString()+"','"+txtage.getText().trim().toString()+"','"+txtnomini.getText().trim().toString()+"','"+txtnominirltn.getText().trim().toString()+"','"+txtoccupation.getText().trim().toString()+"',CURRENT_TIMESTAMP(),'"+sa.getuserid()+"',"+txtmendatory.getText().trim().toString()+","+txtadditional.getText().trim().toString()+","+txtdue.getText().trim().toString()+","+txtmobile.getText().trim().toString()+")");
								db.sta.execute("insert into member_info values('"+id+"',(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' &&  product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')),'"+txtmemberno.getText().trim().toString()+"','"+txtnameinenglish.getText().trim().toString()+"','"+txtnameinbangla.getText().trim().toString()+"',(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"'),"+txtsavings.getText()+","+txtloan.getText().trim().toString()+","+txtloaninstallment.getText().trim().toString()+","+txtdpsyear.getText().toString()+","+txtcategory.getText().toString()+","+txtdpssavings.getText().trim().toString()+","+new SimpleDateFormat("YYYYMMdd").format(joindate.getDate())+",'y','G:','"+form_pic+"','"+id_pic+"','"+txtfathername.getText().trim().toString()+"','"+txthusbandname.getText().trim().toString()+"','"+txtmothername.getText().trim().toString()+"','"+txtage.getText().trim().toString()+"','"+txtnomini.getText().trim().toString()+"','"+txtnominirltn.getText().trim().toString()+"','"+txtoccupation.getText().trim().toString()+"',current_Timestamp,'"+sa.getuserid()+"',"+txtmendatory.getText().trim().toString()+","+txtadditional.getText().trim().toString()+","+txtdue.getText().trim().toString()+","+txtmobile.getText().trim().toString()+")");
								JOptionPane.showMessageDialog(null, "Addmission completed successfully ");
								pic(id);
								pic2(id);
								load();
								reset();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "This member no is already taken...please try another");
					}
				}
			}
		});
		btnupload1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				choser=new JFileChooser();
				
				FileNameExtensionFilter filter=new FileNameExtensionFilter("image", "jpg","png","gif");
				choser.setFileFilter(filter);
				choser.showOpenDialog(fra);
				file=choser.getSelectedFile();
				images=Toolkit.getDefaultToolkit().getImage(file.getPath());
				java.awt.Image resize=images.getScaledInstance(lblformpic.getWidth(), lblformpic.getHeight(), images.SCALE_DEFAULT);
				ImageIcon icon=new ImageIcon(resize);
				lblformpic.setIcon(icon);
				form_pic=file.getPath().toString().substring(3,file.getPath().toString().length());
				System.out.print(form_pic);
			}
		});
		btnupload2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				choser=new JFileChooser();
				
				FileNameExtensionFilter filter=new FileNameExtensionFilter("image", "jpg","png","gif");
				choser.setFileFilter(filter);
				choser.showOpenDialog(fra);
				file2=choser.getSelectedFile();
				images= Toolkit.getDefaultToolkit().getImage(file2.getPath());
				java.awt.Image resize=images.getScaledInstance(lblidpic.getWidth(), lblidpic.getHeight(), images.SCALE_DEFAULT);
				ImageIcon icon=new ImageIcon(resize);
				lblidpic.setIcon(icon);
				id_pic=file2.getPath().toString().substring(3,file2.getPath().toString().length());
				System.out.print(id_pic);
				
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
				
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to delete this member?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							System.out.println("update member_info set activition='n' where account="+txtmemberno.getText()+" && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
							db.sta.execute("update member_info set activition='n' where account="+txtmemberno.getText()+" && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
							
							reset();
							
							load();
							JOptionPane.showMessageDialog(null, "This account is successfully deleted");
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
					int a=JOptionPane.showConfirmDialog(null,"Are you sure to update this account?","Confirm",JOptionPane.YES_NO_OPTION);
					if(a==JOptionPane.YES_OPTION)
					{
						try {
							System.out.print("update member_info set mendatory="+txtmendatory.getText().trim().toLowerCase()+",additional="+txtadditional.getText().trim().toString()+",due="+txtdue.getText().trim().toString()+",mobile_no="+txtmobile.getText().trim().toString()+",occupation='"+txtoccupation.getText().trim().toString()+"',nominirltn='"+txtnominirltn.getText().trim().toString()+"',nomininame='"+txtnomini.getText().trim().toString()+"',age='"+txtage.getText().trim().toString()+"',mothername='"+txtmothername.getText().trim().toString()+"',husbandname='"+txthusbandname.getText().trim().toString()+"',fathername='"+txtfathername.getText().trim().toString()+"',name_eng='"+txtnameinenglish.getText().trim().toString()+"',name_ban='"+txtnameinbangla.getText().trim().toString()+"',savings="+txtsavings.getText()+",loan="+txtloan.getText()+",loan_installment="+txtloaninstallment.getText()+",dps_year="+txtdpsyear.getText().trim().toString()+",dps_savings="+txtdpssavings.getText()+",dps_category="+txtcategory.getText().trim().toString()+",addmission_date="+new SimpleDateFormat("YYYYMMdd").format(joindate.getDate())+",pic_form='"+form_pic+"',pic_id='"+id_pic+"' where activition='y' && account="+txtmemberno.getText()+" && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
							db.sta.execute("update member_info set mendatory="+txtmendatory.getText().trim().toLowerCase()+",additional="+txtadditional.getText().trim().toString()+",due="+txtdue.getText().trim().toString()+",mobile_no="+txtmobile.getText().trim().toString()+",occupation='"+txtoccupation.getText().trim().toString()+"',nominirltn='"+txtnominirltn.getText().trim().toString()+"',nomininame='"+txtnomini.getText().trim().toString()+"',age='"+txtage.getText().trim().toString()+"',mothername='"+txtmothername.getText().trim().toString()+"',husbandname='"+txthusbandname.getText().trim().toString()+"',fathername='"+txtfathername.getText().trim().toString()+"',name_eng='"+txtnameinenglish.getText().trim().toString()+"',name_ban='"+txtnameinbangla.getText().trim().toString()+"',savings="+txtsavings.getText()+",loan="+txtloan.getText()+",loan_installment="+txtloaninstallment.getText()+",dps_year="+txtdpsyear.getText().trim().toString()+",dps_savings="+txtdpssavings.getText()+",dps_category="+txtcategory.getText().trim().toString()+",addmission_date="+new SimpleDateFormat("YYYYMMdd").format(joindate.getDate())+",pic_form='"+form_pic+"',pic_id='"+id_pic+"' where activition='y' && account="+txtmemberno.getText()+" && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
							String acc="";
							ResultSet r=db.sta.executeQuery("select id from member_info where account="+txtmemberno.getText()+" && activition='y' && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
							while(r.next())
							{
								acc=r.getString("id");
										
							}
							pic(Integer.parseInt(acc));
							pic2(Integer.parseInt(acc));
							reset();
							load();
							
							JOptionPane.showMessageDialog(null, "This Account is successfully Updated");
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
		btnprint.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					String report="report/report1.jrxml";
					JasperDesign jd=JRXmlLoader.load(report);
					JRDesignQuery jq=new JRDesignQuery();
					String sql="select image from image where id="+user_id+";";
					System.out.println(sql);
					jq.setText(sql);
					jd.setQuery(jq);
					JasperReport jr=JasperCompileManager.compileReport(jd);
					JasperPrint jp=JasperFillManager.fillReport(jr, null,db.con);
					JasperViewer.viewReport(jp, false);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		});
		btnprintid.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try{
					String report="report/report2.jrxml";
					JasperDesign jd=JRXmlLoader.load(report);
					JRDesignQuery jq=new JRDesignQuery();
					String sql="select image from image1 where id="+user_id+";";
					System.out.println(sql);
					jq.setText(sql);
					jd.setQuery(jq);
					JasperReport jr=JasperCompileManager.compileReport(jd);
					JasperPrint jp=JasperFillManager.fillReport(jr, null,db.con);
					JasperViewer.viewReport(jp, false);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
		});
		btnhistory.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				history h=new history(sa,user_id);
			}
		});
	}
	public void pic(int id)
	{
		try {
			InputStream f=new FileInputStream(file);	
			
			java.sql.PreparedStatement p=db.con.prepareStatement("insert into image values(?,?)");
			p.setString(1, String.valueOf(id));
			p.setBlob(2, f);
			p.executeUpdate();		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void pic2(int id)
	{
		try {
			InputStream f=new FileInputStream(file2);	
			
			java.sql.PreparedStatement p=db.con.prepareStatement("insert into image1 values(?,?)");
			p.setString(1, String.valueOf(id));
			p.setBlob(2, f);
			p.executeUpdate();		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void searchevent()
	{
		try {
			System.out.println("select * from member_info where account="+txtsearch.getText()+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') ) ");
			ResultSet r=db.sta.executeQuery("select * from member_info where account="+txtsearch.getText()+" && activition='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"') ) ");
			while(r.next())
			{
				user_id=r.getString("id");
				txtnameinbangla.setText(r.getString("name_ban"));
				txtnameinenglish.setText(r.getString("name_eng"));
				txtfathername.setText(r.getString("fathername"));
				txthusbandname.setText(r.getString("husbandname"));
				txtmothername.setText(r.getString("mothername"));
				txtage.setText(r.getString("age"));
				txtoccupation.setText(r.getString("occupation"));
				txtnomini.setText(r.getString("nomininame"));
				txtnominirltn.setText(r.getString("nominirltn"));
				cmbgroupno.setSelectedItem(s.name(r.getString("groupno"), "group_info", "groupno"));
				txtmemberno.setText(r.getString("account"));
				txtdpssavings.setText(r.getString("dps_savings"));
				txtloan.setText(r.getString("loan"));
				txtloaninstallment.setText(r.getString("loan_installment"));
				txtsavings.setText(r.getString("savings"));
				txtdpsyear.setText(r.getString("dps_year"));
				txtcategory.setText(r.getString("dps_category"));
				txtmendatory.setText(r.getString("mendatory"));
				txtadditional.setText(r.getString("additional"));
				txtdue.setText(r.getString("due"));
				txtmobile.setText(r.getString("mobile_no"));
				joindate.setDate(r.getDate("addmission_date"));
				form_pic=r.getString("pic_form");
				id_pic=r.getString("pic_id");
				images=((Toolkit.getDefaultToolkit())).getImage(r.getString("pic")+r.getString("pic_form"));
				java.awt.Image resize=images.getScaledInstance(lblformpic.getWidth(), lblformpic.getHeight(), images.SCALE_DEFAULT);
				ImageIcon icon=new ImageIcon(resize);
				lblformpic.setIcon(icon);
				images=((Toolkit.getDefaultToolkit())).getImage(r.getString("pic")+r.getString("pic_id"));
				java.awt.Image resize1=images.getScaledInstance(lblidpic.getWidth(), lblidpic.getHeight(), images.SCALE_DEFAULT);
				ImageIcon icon1=new ImageIcon(resize1);
				lblidpic.setIcon(icon1);
			}
			r.close();
			//r=db.sta.executeQuery("");
			cmbproduct.setEnabled(false);
			txtdpsyear.setEnabled(false);
			cmbgroupno.setEnabled(false);
			txtcategory.setEnabled(false);
			txtmemberno.setEnabled(false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void reset()
	{
		lblformpic.setIcon(null);
		lblidpic.setIcon(null);
		cmbproduct.setEnabled(true);
		cmbgroupno.setEnabled(true);
		txtmemberno.setEnabled(true);
		txtoccupation.setText("");
		txtcategory.setEnabled(true);
		txtdpsyear.setEnabled(true);
		txtdpssavings.setText("0");
		txtcategory.setText("0");
		txtdpsyear.setText("0");
		txtfathername.setText("");
		txtmothername.setText("");
		txthusbandname.setText("");
		txtage.setText("০");
		txtnomini.setText("");
		txtnominirltn.setText("");
		cmbgroupno.setSelectedItem("");
		txtdpssavings.setText("0");
		txtloan.setText("0");
		txtloaninstallment.setText("0");
		txtmemberno.setText("0");
		txtnameinbangla.setText("");
		txtnameinenglish.setText("");
		txtsavings.setText("0");
		txtsearch.setText("");
		cmbproduct.setSelectedItem("");
		txtmendatory.setText("0");
		txtadditional.setText("0");
		txtmobile.setText("0");
		txtdue.setText("0");
		joindate.setDate(new Date());
	}
	public int new_id()
	{
		int i=0;
		try {
			ResultSet r1=db.sta.executeQuery("select ifnull(max(id),0)+1 as id from member_info");
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
		if(!txtnameinbangla.getText().trim().toString().equalsIgnoreCase(""))
		{
			if(!txtnameinenglish.getText().trim().toString().equalsIgnoreCase(""))
			{
				if(cmbgroupno.getSelectedIndex()!=0)
				{
					if(!txtmemberno.getText().trim().toString().equalsIgnoreCase(""))
					{
						if(!txtsavings.getText().trim().toString().equalsIgnoreCase(""))
						{
							if(!txtloan.getText().trim().toString().equalsIgnoreCase(""))
							{
								if(!txtloaninstallment.getText().trim().toString().equalsIgnoreCase(""))
								{
									if(!txtdpssavings.getText().trim().toString().equalsIgnoreCase(""))
									{
										if(!txtcategory.getText().equals(""))
										{
											if(!txtdpsyear.getText().equals(""))
											{
												if(cmbproduct.getSelectedIndex()!=0)
												{
													if(!joindate.getDate().toString().equalsIgnoreCase(""))
													{
														if(!txtfathername.toString().equalsIgnoreCase(""))
														{
															if(!txthusbandname.toString().equalsIgnoreCase(""))
															{
																if(!txtmothername.toString().equalsIgnoreCase(""))
																{
																	if(!txtage.toString().equalsIgnoreCase(""))
																	{
																		if(!txtnomini.toString().equalsIgnoreCase(""))
																		{
																			if(!txtnominirltn.toString().equalsIgnoreCase(""))
																			{
																				return true;
																			}
																			else
																			{
																				JOptionPane.showMessageDialog(null, "Please Enter The Relation between client and nominee ");
																				txtnominirltn.requestFocus();
																			}
																		}
																		else
																		{
																			JOptionPane.showMessageDialog(null, "Please Enter The Nominee Name");
																			txtnomini.requestFocus();
																		}
																	}
																	else
																	{
																		JOptionPane.showMessageDialog(null, "Please Enter The age ");
																		txtage.requestFocus();
																	}
																}
																else
																{
																	JOptionPane.showMessageDialog(null, "Please Enter The Mother Name");
																	txtmothername.requestFocus();
																}
															}
															else
															{
																JOptionPane.showMessageDialog(null, "Please Enter The Husband Name");
																txthusbandname.requestFocus();
															}
														}
														else
														{
															JOptionPane.showMessageDialog(null, "Please Enter The Father Name");
															txtfathername.requestFocus();
														}
													}
													else
													{
														JOptionPane.showMessageDialog(null, "Please Enter The Addmission Date");
														joindate.requestFocus();
													}
												}
												else
												{
													JOptionPane.showMessageDialog(null, "Please Select The Product");
													cmbproduct.requestFocus();
												}
											}
											else
											{
												JOptionPane.showMessageDialog(null, "Please Enter The DPS Deadline");
												txtdpsyear.requestFocus();
											}
										}
										else
										{
											JOptionPane.showMessageDialog(null, "Please Enter The DPS Category");
											txtcategory.requestFocus();
										}
									}
									else
									{
										JOptionPane.showMessageDialog(null, "Please Enter The Amount of DPS Savings");
										txtdpssavings.requestFocus();
									}
								}
								else
								{
									JOptionPane.showMessageDialog(null, "Please Enter The Amount Of Loan Installment");
									txtloaninstallment.requestFocus();
								}
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Please Enter The Amount of Loan");
								txtloan.requestFocus();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Please Enter The Amount of Savings");
							txtsavings.requestFocus();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please Enter The Member No");
						txtmemberno.requestFocus();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please Enter The Group No");
					cmbgroupno.requestFocus();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Please Enter The Name in English");
				txtnameinenglish.requestFocus();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Please Enter The Name in Bangla");
			txtnameinbangla.requestFocus();
		}
		return false;
	}
	public void load()
	{
		try{
			ResultSet r;
			if(cmbproduct.getSelectedIndex()==1)
			{
				r=db.sta.executeQuery("select max(cast(account as UNSIGNED))+1  as account from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
				while(r.next())
				{
					txtmemberno.setText(r.getString("account"));
				}
				r.close();
			}
			else{
				r=db.sta.executeQuery("select max(account)+1 as account from member_info");
				while(r.next())
				{
					txtmemberno.setText(r.getString("account"));
					
				}
				r.close();
			}
			model.getDataVector().removeAllElements();
			revalidate();
	
			//System.out.println("select * from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
			r = db.sta.executeQuery("select * from member_info where activition='y' && groupno=(select id from group_info where groupno='"+cmbgroupno.getSelectedItem().toString()+"' && activity='y' && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')) && product=(select id from product where product_name='"+cmbproduct.getSelectedItem().toString()+"')");
			while(r.next())
			{
				model.addRow(new Object[]{s.name(r.getString("groupno"), "group_info", "groupno"),r.getString("account"),r.getString("name_eng")});
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
		pnlmain.setPreferredSize(new Dimension(1200,800));
		pnlmain.setLayout(new FlowLayout());
		pnlmain.add(pnlwest);
		pnlmain.add(pnleast);
		
		westpnlwrk();
		eastpnlwrk();
	}
	public void westpnlwrk()
	{
		pnlwest.setOpaque(false);
		pnlwest.setPreferredSize(new Dimension(470,650));
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
		pnltxt.setPreferredSize(new Dimension(450,600));
		pnltxt.setBorder(BorderFactory.createLineBorder(Color.black));
		pnltxt.setLayout(new GridBagLayout());
		grid.insets=new Insets(2,2,2,2);
		grid.gridx=0;
		grid.gridy=0;
		pnltxt.add(lblnameinenglish,grid);
		grid.gridx=1;
		grid.gridy=0;
		pnltxt.add(txtnameinenglish,grid);
		
		grid.gridx=0;
		grid.gridy=1;
		pnltxt.add(lblnameinbangla,grid);
		grid.gridx=1;
		grid.gridy=1;
		pnltxt.add(txtnameinbangla,grid);
		txtnameinbangla.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		
		grid.gridx=0;
		grid.gridy=2;
		pnltxt.add(lblfathername,grid);
		grid.gridx=1;
		grid.gridy=2;
		pnltxt.add(txtfathername,grid);
		txtfathername.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		
		grid.gridx=0;
		grid.gridy=3;
		pnltxt.add(lblhusbandname,grid);
		grid.gridx=1;
		grid.gridy=3;
		pnltxt.add(txthusbandname,grid);
		txthusbandname.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		
		grid.gridx=0;
		grid.gridy=4;
		pnltxt.add(lblmothername,grid);
		grid.gridx=1;
		grid.gridy=4;
		pnltxt.add(txtmothername,grid);
		txtmothername.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		grid.gridx=0;
		grid.gridy=5;
		pnltxt.add(lblage,grid);
		grid.gridx=1;
		grid.gridy=5;
		pnltxt.add(txtage,grid);
		txtage.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		grid.gridx=0;
		grid.gridy=6;
		pnltxt.add(lbloccupation,grid);
		grid.gridx=1;
		grid.gridy=6;
		pnltxt.add(txtoccupation,grid);
		txtoccupation.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		
		grid.gridx=0;
		grid.gridy=7;
		pnltxt.add(lblnomini,grid);
		grid.gridx=1;
		grid.gridy=7;
		pnltxt.add(txtnomini,grid);
		txtnomini.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		grid.gridx=0;
		grid.gridy=8;
		pnltxt.add(lblrltn,grid);
		grid.gridx=1;
		grid.gridy=8;
		pnltxt.add(txtnominirltn,grid);
		txtnominirltn.setFont(new Font("SolaimanLipi",Font.PLAIN,13));
		
		grid.gridx=0;
		grid.gridy=9;
		pnltxt.add(lblproduct,grid);
		grid.gridx=1;
		grid.gridy=9;
		pnltxt.add(cmbproduct,grid);
		cmbproduct.setPreferredSize(new Dimension(180,20));
		grid.gridx=0;
		grid.gridy=10;
		pnltxt.add(lblgroup,grid);
		grid.gridx=1;
		grid.gridy=10;
		pnltxt.add(cmbgroupno,grid);
		cmbgroupno.setPreferredSize(new Dimension(180,20));
		grid.gridx=0;
		grid.gridy=11;
		pnltxt.add(lblmember_no,grid);
		grid.gridx=1;
		grid.gridy=11;
		pnltxt.add(txtmemberno,grid);
		
		grid.gridx=0;
		grid.gridy=12;
		pnltxt.add(lblsavings,grid);
		grid.gridx=1;
		grid.gridy=12;
		pnltxt.add(txtsavings,grid);
		
		grid.gridx=0;
		grid.gridy=13;
		pnltxt.add(lblloan,grid);
		grid.gridx=1;
		grid.gridy=13;
		pnltxt.add(txtloan,grid);
		
		grid.gridx=0;
		grid.gridy=14;
		pnltxt.add(lblloan_installment,grid);
		grid.gridx=1;
		grid.gridy=14;
		pnltxt.add(txtloaninstallment,grid);
		
		grid.gridx=0;
		grid.gridy=15;
		pnltxt.add(lbldps_category,grid);
		grid.gridx=1;
		grid.gridy=15;
		pnltxt.add(txtcategory,grid);
		
		grid.gridx=0;
		grid.gridy=16;
		pnltxt.add(lbldps_year,grid);
		grid.gridx=1;
		grid.gridy=16;
		pnltxt.add(txtdpsyear,grid);
		
		grid.gridx=0;
		grid.gridy=17;
		pnltxt.add(lbldps_savings,grid);
		grid.gridx=1;
		grid.gridy=17;
		pnltxt.add(txtdpssavings,grid);
		
		grid.gridx=0;
		grid.gridy=18;
		pnltxt.add(lbljoindate,grid);
		grid.gridx=1;
		grid.gridy=18;
		pnltxt.add(joindate,grid);
		
		grid.gridx=0;
		grid.gridy=19;
		pnltxt.add(lblmendatory,grid);
		grid.gridx=1;
		grid.gridy=19;
		pnltxt.add(txtmendatory,grid);
		
		grid.gridx=0;
		grid.gridy=20;
		pnltxt.add(lbladditional,grid);
		grid.gridx=1;
		grid.gridy=20;
		pnltxt.add(txtadditional,grid);
		
		
		grid.gridx=0;
		grid.gridy=21;
		pnltxt.add(lblmobile,grid);
		grid.gridx=1;
		grid.gridy=21;
		pnltxt.add(txtmobile,grid);
		
		grid.gridx=0;
		grid.gridy=22;
		pnltxt.add(lbldue,grid);
		grid.gridx=1;
		grid.gridy=22;
		pnltxt.add(txtdue,grid);
		
		joindate.setPreferredSize(new Dimension(180,20));
	}
	public void pnlbtnwrk()
	{
		pnlbtn.setOpaque(false);
		pnlbtn.setPreferredSize(new Dimension(450,48));
		pnlbtn.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlbtn.setLayout(new FlowLayout());
		pnlbtn.add(btncreate);
		pnlbtn.add(btnreset);
		pnlbtn.add(btndelete);
		pnlbtn.add(btnupdate);
		pnlbtn.add(btnsearch);
		pnlbtn.add(btnhistory);
	}
	public void eastpnlwrk()
	{
		pnleast.setOpaque(false);
		pnleast.setBorder(BorderFactory.createLineBorder(Color.black));
		pnleast.setPreferredSize(new Dimension(680,590));
		pnleast.setLayout(new FlowLayout());
		pnleast.add(pnlpic);
		pnlpic.setPreferredSize(new Dimension(280,550));
		pnlpicwrk();
		pnleast.add(scro);
		scro.setOpaque(false);
		scro.setPreferredSize(new Dimension(380,550));
		pnleast.add(txtsearch);
		pnleast.add(btnsearch);
		
	}
	public void pnlpicwrk()
	{
		pnlpic.setOpaque(false);
		pnlpic.setLayout(new FlowLayout());
		pnlpic.setBorder(BorderFactory.createLineBorder(Color.black));
		pnlpic.add(lblformpic);
		pnlpic.add(btnupload1);
		pnlpic.add(btnprint);
		pnlpic.add(lblidpic);
		pnlpic.add(btnupload2);
		pnlpic.add(btnprintid);
		lblformpic.setOpaque(false);
		lblidpic.setOpaque(false);
		lblformpic.setPreferredSize(new Dimension(250,200));
		lblidpic.setPreferredSize(new Dimension(250,150));
		lblformpic.setBorder(BorderFactory.createLineBorder(Color.black));
		lblidpic.setBorder(BorderFactory.createLineBorder(Color.black));
	}
}
