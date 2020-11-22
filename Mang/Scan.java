import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Scan extends JFrame {
	private int width = 500;
	private int height = 600;

	static JTextArea output;
	static JTextField myip;

	static ArrayList<Integer> network;
	static ArrayList<String> hostList;
	static String ip;
	static String sm;
	static JTextField start1, start2, start3, start4, end1, end2, end3, end4;

	public static void main(String[] args) throws SocketException, UnknownHostException {
		new Scan();
		network = getNetWorkAddress();
		hostList = arangeIp(network);
		myip.setText(ip);
		String startIP = hostList.get(0);
		String endIP = hostList.get(hostList.size() - 1);
		setStartEnd(startIP, endIP);
	}

	public Scan() {
		this.setSize(width, height);
		this.setTitle("Scan IP Lan network");
		// my ip
		JLabel ipl = new JLabel();
		ipl.setText("My IP : ");
		ipl.setBounds(100, 30, 100, 40);
		this.add(ipl);

		myip = new JTextField();
		myip.setEditable(false);
		myip.setBounds(200, 30, 200, 40);
		this.add(myip);
		// ---------------------------------------------------------
		// ip start
		JLabel starlb = new JLabel();
		starlb.setText("Start: ");
		starlb.setBounds(50, 100, 40, 40);
		this.add(starlb);

		start1 = new JTextField();
		start1.setEditable(false);
		start1.setBounds(100, 100, 30, 40);
		this.add(start1);

		start2 = new JTextField();
		start2.setEditable(false);
		start2.setBounds(140, 100, 30, 40);
		this.add(start2);

		start3 = new JTextField();
		start3.setEditable(false);
		start3.setBounds(180, 100, 30, 40);
		this.add(start3);

		start4 = new JTextField();
		start4.setEditable(false);
		start4.setBounds(220, 100, 30, 40);
		this.add(start4);

		// ip end ----------------------------------------------------------
		JLabel endip = new JLabel();
		endip.setText("End: ");
		endip.setBounds(260, 100, 40, 40);
		this.add(endip);

		end1 = new JTextField();
		end1.setEditable(false);
		end1.setBounds(300, 100, 30, 40);
		this.add(end1);

		end2 = new JTextField();
		end2.setEditable(false);
		end2.setBounds(340, 100, 30, 40);
		this.add(end2);

		end3 = new JTextField();
		end3.setEditable(false);
		end3.setBounds(380, 100, 30, 40);
		this.add(end3);

		end4 = new JTextField();
		end4.setEditable(false);
		end4.setBounds(420, 100, 30, 40);
		this.add(end4);
		// result------------------------------------------------------------
		output = new JTextArea();
		output.setEditable(false);
		JScrollPane scroll = new JScrollPane(output);
		scroll.setBounds(100, 150, 300, 300);
		this.add(scroll);
		// button scan ----------------------------------------------------
		JButton scan = new JButton("Scan");
		scan.setBounds(160, 460, 70, 50);
		scan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					output.setText("");
					getNetworkIPs();
				} catch (Exception e1) {
				}
			}
		});
		this.add(scan);
		// button exit -------------------------------------------------------
		JButton exit = new JButton("exit");
		exit.setBounds(280, 460, 70, 50);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.exit(0);
				} catch (Exception e1) {
				}
			}
		});
		this.add(exit);

		this.setLayout(null);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
	}

	@SuppressWarnings("null")
	public static ArrayList<Integer> getNetWorkAddress() throws SocketException, UnknownHostException {
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		int u = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		String ipAddress = localHost.toString();

		String array[] = ipAddress.split("\\/");
		ip = array[1];
		sm = getIPv4LocalNetMask(localHost, u);
		String[] ip_array = ip.split("\\.");
		String[] sm_array = sm.split("\\.");
		ArrayList<Integer> network = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			int a = Integer.parseInt(ip_array[i]);
			int b = Integer.parseInt(sm_array[i]);
			int c = (int) (a & b);
			network.add(c);
		}
		network.add(u);
		return network;

	}

	public static String getIPv4LocalNetMask(InetAddress ip, int netPrefix) {

		try {
			int shiftby = (1 << 31);
			for (int i = netPrefix - 1; i > 0; i--) {
				shiftby = (shiftby >> 1);
			}
			String maskString = Integer.toString((shiftby >> 24) & 255) + "." + Integer.toString((shiftby >> 16) & 255)
					+ "." + Integer.toString((shiftby >> 8) & 255) + "." + Integer.toString(shiftby & 255);
			return maskString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static ArrayList<String> arangeIp(ArrayList<Integer> network) {
		int u = network.get(4);
		int host = (int) (Math.pow(2, 32 - u) - 2);
		ArrayList<String> hostList = new ArrayList<String>();
		int i = 0;
		int a = network.get(0);
		int b = network.get(1);
		int c = network.get(2);
		int d = network.get(3);

		while (i < host) {
			String ip = "";
			d++;
			if (d > 255) {
				d = 0;
				c++;
			}
			if (c > 255) {
				c = 0;
				b++;

			}
			if (b > 255) {
				b = 0;
				a++;
			}
			if (a > 255) {
				break;
			}
			ip = a + "." + b + "." + c + "." + d;
			hostList.add(ip);
			i++;
		}

		return hostList;
	}

	public static void setStartEnd(String start, String end) {
		String[] start_array = start.split("\\.");
		String[] end_array = end.split("\\.");

		start1.setText(start_array[0]);
		start2.setText(start_array[1]);
		start3.setText(start_array[2]);
		start4.setText(start_array[3]);

		end1.setText(end_array[0]);
		end2.setText(end_array[1]);
		end3.setText(end_array[2]);
		end4.setText(end_array[3]);
	}

	// public static boolean checkIP() {
	// int s1 = Integer.parseInt(start1.getText());
	// int s2 = Integer.parseInt(start2.getText());
	// int s3 = Integer.parseInt(start3.getText());
	// int s4 = Integer.parseInt(start4.getText());
	// int e1 = Integer.parseInt(end1.getText());
	// int e2 = Integer.parseInt(end2.getText());
	// int e3 = Integer.parseInt(end3.getText());
	// int e4 = Integer.parseInt(end4.getText());
	// if(s1 < 0 || s1>=255)

	// return true;
	// }

	public static void getNetworkIPs() throws IOException {

		for (String ipString : hostList) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					InetAddress ipAdress;
					try {
						ipAdress = InetAddress.getByName(ipString);
						String out = ipAdress.toString().substring(1);
						if (ipAdress.isReachable(5000)) {
							// System.out.println(out + " is on the network");
							output.append(out + " is on the network\n");

						} else {
							// System.out.println("Not Reachable: "+out);
							// output.append("Not Reachable: " + out + "\n");
						}
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

		}
	}
}
