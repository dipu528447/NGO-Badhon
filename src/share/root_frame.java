package share;
import report.addmission_report;
import report.drop_out_report;
import report.due_report;
import report.interest_bss;
import report.ledger;
import report.loan_disburst_report;
import report.loan_fee_register;
import report.loan_paid_report;
import report.loan_risk_fund;
import report.passbook_report;
import report.recieve_payment;
import report.savings_return_withdraw_report;
import setup.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.title.ImageTitle;

import account.debit_voucher;
import account.external_expense;
import account.external_income;
import account.general_credit_voucher;
import account.general_debit_voucher;
import account.voucher;

import setup.product_setup;


public class root_frame extends JPanel{
	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	seasionbean sa;
	db_connection db=new db_connection();
	
	JMenuBar menubar=new JMenuBar();
	
	JMenu menu_setup=new JMenu("Setup");
	JMenu menu_account=new JMenu("Accountance");
	JMenu menu_report=new JMenu("Report");
	JMenu menu_logout=new JMenu("logout");
	
	String setup[]={"Product Create","Group Create","Ledger Create","Addmission"};
	String account[]={"General Credit Voucher","External Crebit Voucher","General Debit Voucher","External Debit voucher","Credit_Voucher","Debit_Voucher"};
	String report[]={"Monthly Reciept Payment Report","Income Expendure","Balance Sheet","Addmission Report","Passbook Report","Drop Out Report","Savings Withdraw-Return Report","Loan Disburst Report","Loan fee Report","Risk Fund Report","Loan Paid Report","ledger","due report","Bss interest Report"};
	
	BufferedImage image;
	final JDesktopPane dtp = new JDesktopPane(){
		private static final long serialVersionUID = 1L;
		ImageIcon icon = new ImageIcon("icon/tk.jpg");
		Image image = icon.getImage();

		Image newimage = image.getScaledInstance(1360, 760, Image.SCALE_SMOOTH);

		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(newimage, 0, 0, this);
		}
	};	
	JFrame f=new JFrame();
	public root_frame(JFrame frm,seasionbean s)
	{
		try {
			db.conect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f=frm;
		f.setSize((int)d.getWidth(), (int) d.getHeight());
		f.add(this);
		setPreferredSize(new Dimension((int)d.getWidth(),(int)d.getHeight()));
		setVisible(true);
		f.setResizable(true);
		f.setLocationRelativeTo(null);
		sa=s;
		f.setTitle("HOME PANEL :: "+sa.getcompanyname());
		f.setJMenuBar(menubar);
		f.add(dtp);
		f.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage("icon/accouts.png"));
	
		menubar.add(menu_setup);
		menubar.add(menu_account);
		menubar.add(menu_report);
		menubar.add(menu_logout);


		
		//setup menu work
		for(int a=0;a<setup.length;a++)
		{
			JMenuItem menu=new JMenuItem(setup[a]);
			menu_setup.add(menu);
			menu_setup.addSeparator();
			if(a==0){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame setup_product =new JInternalFrame();
							
							dtp.add(setup_product);
							setup_product.setOpaque(false);
							setup.product_setup p=new setup.product_setup(sa);
							setup_product.add(p);
							setup_product.setSize(700,400);
							setup_product.setTitle("Product Launch ::"+sa.getcompanyname());
							setup_product.setVisible(true);
							setup_product.setLocation(10, 20);
							setup_product.setClosable(true);
							
					}
				});
			}
			
			else if(a==1){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame setup_group =new JInternalFrame();
							dtp.add(setup_group);
							setup.group_setup p=new setup.group_setup(sa);
							setup_group.add(p);
							setup_group.setOpaque(false);
							
							setup_group.setSize(900,400);
							setup_group.setTitle("Group Launch ::"+sa.getcompanyname());
							setup_group.setVisible(true);
							setup_group.setLocation(100, 20);
							setup_group.setClosable(true);
							setup_group.setResizable(false);
							
					}
				});
			}
			
			else if(a==2){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame setup_ledger=new JInternalFrame();
							dtp.add(setup_ledger);
							ledger_Setup p=new ledger_Setup(sa);
							setup_ledger.add(p);
							setup_ledger.setSize(900,400);
							setup_ledger.setTitle("Ledger Launch ::"+sa.getcompanyname());
							setup_ledger.setVisible(true);
							setup_ledger.setLocation(100, 20);
							setup_ledger.setClosable(true);
							setup_ledger.setResizable(false);
							setup_ledger.setOpaque(false);
					}
				});
			}
			
			else if(a==3){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
						System.out.println("full");
						JInternalFrame setup_addmission =new JInternalFrame();
						dtp.add(setup_addmission);
						admission2 p=new admission2(sa,f);
						setup_addmission.add(p);
						setup_addmission.setSize(1250,850);
						setup_addmission.setTitle("Addmission ::"+sa.getcompanyname());
						setup_addmission.setVisible(true);
						setup_addmission.setLocation(10, 10);
						setup_addmission.setClosable(true);
						setup_addmission.setOpaque(false);
				}
				});
			}
		}
		
		//account menu work
		for(int a=0;a<account.length;a++)
		{
			JMenuItem menu=new JMenuItem(account[a]);
			menu_account.add(menu);
			menu_account.addSeparator();
			if(a==0){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							general_credit_voucher p=new general_credit_voucher(sa);
							account_general.add(p);
							account_general.setSize(1200,650);
							account_general.setTitle("General Receive Voucher :: "+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
			else if(a==1){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							external_income p=new external_income(sa);
							account_general.add(p);
							account_general.setSize(600,600);
							account_general.setTitle("External Income Voucher ::"+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
			else if(a==2){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							general_debit_voucher p=new general_debit_voucher(sa);
							account_general.add(p);
							account_general.setSize(1200,650);
							account_general.setTitle("General Debit Voucher :: "+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
			else if(a==3){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							external_expense p=new external_expense(sa);
							account_general.add(p);
							account_general.setSize(600,600);
							account_general.setTitle("External Expense Voucher :: "+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
			else if(a==4){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							voucher p=new voucher(sa);
							account_general.add(p);
							account_general.setSize(1200,600);
							account_general.setTitle("Recieve Voucher :: "+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
			else if(a==5){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame account_general =new JInternalFrame();
							dtp.add(account_general);
							debit_voucher p=new debit_voucher(sa);
						
							account_general.add(p);
							account_general.setSize(1200,600);
							account_general.setTitle("Debit Voucher :: "+sa.getcompanyname());
							account_general.setVisible(true);
							account_general.setLocation(100, 20);
							account_general.setClosable(true);
							account_general.setOpaque(false);
						
					}
				});
			}
		}
		
		// report menu work
		
		for(int a=0;a<report.length;a++)
		{
			JMenuItem menu=new JMenuItem(report[a]);
			menu_report.add(menu);
			menu_report.addSeparator();
			if(a==0){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							recieve_payment p=new recieve_payment(sa);
							
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Monthly Receive Payment Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==1){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							addmission_report p=new addmission_report(sa);
							
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Addmission Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==2){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							addmission_report p=new addmission_report(sa);
							
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Addmission Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==3){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							addmission_report p=new addmission_report(sa);
							
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Addmission Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==4){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							passbook_report p=new passbook_report(sa);
							
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Passbook Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==5){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							drop_out_report p=new drop_out_report(sa);
							report_addmission.mouseDrag(null, 1,2);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Drop Out Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==6){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							savings_return_withdraw_report p=new savings_return_withdraw_report(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Savings Return And Withdraw Register :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==7){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							loan_disburst_report p=new loan_disburst_report(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Disburst Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==8){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							loan_fee_register p=new loan_fee_register(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Disburst Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==9){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							loan_risk_fund p=new loan_risk_fund(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Risk Fund Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==10){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							loan_paid_report p=new loan_paid_report(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Paid Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==11){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							ledger p=new ledger(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Paid Report :: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==12){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							due_report p=new due_report(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Loan Due Report:: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
			else if(a==13){
				menu.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
							System.out.println("full");
							JInternalFrame report_addmission =new JInternalFrame();
							dtp.add(report_addmission);
							interest_bss p=new interest_bss(sa);
							report_addmission.add(p);
							report_addmission.setSize(1200,650);
							report_addmission.setTitle("Interest BSS Report:: "+sa.getcompanyname());
							report_addmission.setVisible(true);
							report_addmission.setLocation(100, 20);
							report_addmission.setClosable(true);
							report_addmission.setOpaque(false);
						
					}
				});
			}
		}
		
		

		//background();
	}
	public void background()
	{
		try {                
			image = ImageIO.read(new File("icon/tk.jpg"));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	}
}
