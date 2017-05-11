import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class T extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			Socket s= null;
			ObjectOutputStream oos = null;
				try {
					if(s == null || s.isClosed()){
						s = new Socket(args[0], Integer.valueOf(args[1]));
						oos = new ObjectOutputStream(s.getOutputStream());
					}
					oos.writeObject(new Object[]{true});
					Thread.sleep(50);
					oos.writeObject(new Object[]{false});
					
				} catch (Exception e1) {
					if (s != null) {
						try{s.close();}catch(IOException ee){ee.printStackTrace();System.exit(1);}
					}
				} finally {
					if (s != null) {
						try{s.close();}catch(IOException ee){ee.printStackTrace();System.exit(1);}
					}
				}
	}

}
