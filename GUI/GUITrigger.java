import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUITrigger extends JFrame {
	static class MyPanel extends JPanel {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GUITrigger gui = new GUITrigger();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MyPanel panel = new MyPanel();
//		panel.setPreferredSize(new Dimension(400, 400));
		JButton b = new JButton("Rotate");
		final JTextField tf = new JTextField("192.168.1.103");
		JLabel lb1 = new JLabel("IP: ");
		lb1.setLabelFor(tf);
		final JTextField tf2 = new JTextField("1010");
		JLabel lb2 = new JLabel("Port: ");
		lb1.setLabelFor(tf2);
		b.addActionListener(new ActionListener() {
			Socket s;
			ObjectOutputStream oos;
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(s == null || s.isClosed()){
						s = new Socket();
						s.connect(new InetSocketAddress(tf.getText(), Integer.valueOf(tf2.getText())), 50);
						oos = new ObjectOutputStream(s.getOutputStream());
					}
					oos.writeObject(new Object[]{true});
					Thread.sleep(50);
					oos.writeObject(new Object[]{false});
					
				} catch (Exception e1) {
					if (s != null) {
						try{s.close();}catch(IOException ee){ee.printStackTrace();System.exit(1);}
					}
				}
			}
		});
		panel.setLayout(new GridBagLayout());
		panel.add(lb1);
		panel.add(tf);
		panel.add(Box.createRigidArea(new Dimension(15,0)));
		panel.add(lb2);
		panel.add(tf2);
		panel.add(Box.createRigidArea(new Dimension(15,0)));
		panel.add(b);
		gui.add(panel);
		gui.pack();
		gui.setVisible(true);

	}

}
