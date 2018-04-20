
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Serialinterface extends Thread implements SerialPortEventListener {
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM5", // Windows
	};

	private BufferedReader input;
	private OutputStream output;
	private double puls = 0.0;
	private double timebetween = 0.0;
	private Sound sd = new Sound();
	private double[] zeit = new double[60];
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	private boolean close = false;
	private int z = 0;
	private Gui gui;
	private double bpm = 0.0;
	private double timebuffer = 0.0;
	private boolean up = false;
	private boolean now = false;
	private FileWriter fw;
	private BufferedWriter bw;
	private double[] realtime = new double[60];
	private double[] timesound = new double[60];
	private boolean record = false;

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	private boolean dec = false;
	private boolean inc = false;

	public void stopRecord() {
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setVerringern(boolean dec) {
		this.dec = dec;
	}

	public void setErhoehen(boolean inc) {
		this.inc = inc;
	}

	public void setUp(boolean up) {
		this.up = up;
	}
	
	private javax.swing.Timer timerSound;

	/**
	 * @param close
	 *            the close to set
	 */
	public void setClose(boolean close) {
		this.close = close;
	}

	public Serialinterface(Gui gui) {
		this.gui = gui;
		System.out.println("Started");
		timerSound = new javax.swing.Timer(100, new SoundListener());

		initialize();

	}

	public void run() {

		while (close != true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public void startRecord() {
		try {
			fw = new FileWriter("Test.txt");
			bw = new BufferedWriter(fw);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				if (input.ready()) {
					System.out.println("counter: " + z);

					if (timesound[z] == 0.0 && now) {
						sd.playSound();
						timerSound.stop();
					}

					if (z == 59) {
						z = 0;
						if (inc) {
							now = true;
						}
					}

					System.out.println("Line: " + input.readLine());// + " Time: "+ timebetween);
					timebetween = System.currentTimeMillis() - puls;
					realtime[z] = timebetween;
					if (timebetween >= 3000) {
						timebetween = 0;
					}

					zeit[z] = timebetween;
					for (int a = 0; a < 60; a++) {
						timebuffer += zeit[a];
					}

					bpm = (60 * 60) / (timebuffer / 1000);
					if (inc) {
						if (timebuffer != 0) {
							increase(timebuffer / 60);
						}
					} else if (dec) {
						decrease(z);
					}

					puls = System.currentTimeMillis();

					System.out
							.println("Echt: " + realtime[z] + " Sound: " + timesound[z] + " ds: " + (timebuffer / 60));
					gui.setValue(bpm);
					z++;
					if (isRecord()) {
						bw.write(Double.toString((timebuffer / 60)));
						bw.newLine();
					}
					timebuffer = 0;

				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	private class SoundListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			sd.playSound();
			timerSound.stop();
			if (!up) {
				timesound[z] = System.currentTimeMillis() - puls;
				// System.out.println("sound: " + timesound[z] + realtime[z]);
			} else {
				timesound[z] = System.currentTimeMillis() - puls;
			}
		}
	}

	public void decrease(int z) {
		timerSound.setInitialDelay(gui.getDis());
		timerSound.start();
	}

	public void increase(double time) {
		int reload = (int) time;
		if (now || up == true) {
			up = true;
			timerSound.setInitialDelay((int) (time - gui.getDis()));
			timerSound.start();
		}
	}

}