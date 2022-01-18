package share;

import javax.swing.UIManager;


import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaMauveMetallicLookAndFeel;




public class ngo_main_class {
	public static void main(String args[])
	{
		try {
			//javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			//UIManager.setLookAndFeel(new SyntheticaMauveMetallicLookAndFeel());
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		seasionbean s=new seasionbean();
		s.setcompanyname("BADHON KHUDRO BABOSAI SOMOBAI SOMITTEE LTD.");
		login l=new login(s);
	}
}
