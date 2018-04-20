import java.awt.Font;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Gui {

	private JFrame frame = new JFrame();
	private JLabel label_1;
	private Serialinterface si;
	private JRadioButton rdbtnErhhen, rdbtnVerringern;
	private int dis = 100;

	public int getDis() {
		return dis;
	}

	public JRadioButton getRdbtnErhhen() {
		return rdbtnErhhen;
	}

	public JRadioButton getRdbtnVerringern() {
		return rdbtnVerringern;
	}

	public Gui() {

		init();
		si = new Serialinterface(this);
		si.start();

	}

	public void setValue(double bpm) {
		label_1.setText(Double.toString(bpm));
	}

	public void init() {
		frame = new JFrame("Tick");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(381, 292);
		frame.getContentPane().setLayout(null);

		JLabel lblPulssteuerung = DefaultComponentFactory.getInstance().createLabel("Pulssteuerung");
		lblPulssteuerung.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblPulssteuerung.setBounds(12, 13, 263, 43);
		frame.getContentPane().add(lblPulssteuerung);

		JLabel lblPulsInBpm = DefaultComponentFactory.getInstance().createLabel("Puls in bpm:");
		lblPulsInBpm.setBounds(12, 69, 110, 16);
		frame.getContentPane().add(lblPulsInBpm);

		label_1 = new JLabel();
		label_1.setBounds(134, 69, 110, 16);
		frame.getContentPane().add(label_1);

		rdbtnErhhen = new JRadioButton("Erh\u00F6hen");
		rdbtnErhhen.setBounds(12, 107, 127, 25);
		frame.getContentPane().add(rdbtnErhhen);
		rdbtnErhhen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnVerringern.isSelected()) {
					rdbtnVerringern.setSelected(false);

				}
			}
		});

		rdbtnVerringern = new JRadioButton("Verringern");
		rdbtnVerringern.setBounds(12, 137, 127, 25);
		frame.getContentPane().add(rdbtnVerringern);
		rdbtnVerringern.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					rdbtnErhhen.setSelected(false);
					
				
			}
		});

		JLabel lblAbstand = DefaultComponentFactory.getInstance().createLabel("Abstand:");
		lblAbstand.setBounds(12, 174, 110, 16);
		frame.getContentPane().add(lblAbstand);

		TextField textField = new TextField();
		textField.setBounds(12, 196, 150, 24);
		frame.getContentPane().add(textField);

		Button button = new Button("OK");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				si.setErhoehen(rdbtnErhhen.isSelected());
				si.setVerringern(rdbtnVerringern.isSelected());
				System.out.println("TF: "+textField.getText());
				dis = Integer.parseInt(textField.getText());	
				}
		});
		button.setBounds(210, 196, 79, 24);
		frame.getContentPane().add(button);
		
		JRadioButton rdbtnAufzeichnen = new JRadioButton("aufzeichnen");
		rdbtnAufzeichnen.setBounds(162, 107, 127, 25);
		frame.getContentPane().add(rdbtnAufzeichnen);
		frame.setVisible(true);
		 rdbtnAufzeichnen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!si.isRecord()) {
				si.startRecord();
				si.setRecord(true);
				}else if(si.isRecord()) {
					si.setRecord(false);
					si.stopRecord();
				}
			}
		});

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Gui();

	}
}
