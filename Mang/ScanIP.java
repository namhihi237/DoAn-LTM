import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Scan extends JFrame {
	private int width = 500;
    private int height = 600;
    
    JTextArea output;
	public static void main(String[] args) throws SocketException, UnknownHostException {
		new Scan();
//		getNetworkIPs();
		getSubnetMask();

	}
	public Scan() {
        this.setSize(width, height);
        this.setTitle("Scan IP");
        
        output = new JTextArea();
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBounds(100, 30, 300, 400);
        this.add(scroll);
        
        JButton scan = new JButton("Scan");
        scan.setBounds(160, 450, 70, 50);
        scan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                } catch (Exception e1) {
                }
            }
        });
        this.add(scan);
        
        JButton exit = new JButton("exit");
        exit.setBounds(280, 450, 70, 50);
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
	
	public static void getSubnetMask() throws SocketException, UnknownHostException {
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		System.out.println(networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
		
	}
	
	public static void getNetworkIPs() {
	    final byte[] ip;
	    try {
	        ip = InetAddress.getLocalHost().getAddress();
	    } catch (Exception e) {
	        return;     // exit method, otherwise "ip might not have been initialized"
	    }

	    for(int i=1;i<=254;i++) {
	        final int j = i;  // i as non-final variable cannot be referenced from inner class
	        new Thread(new Runnable() {   // new thread for parallel execution
	            public void run() {
	                try {
	                    ip[3] = (byte)j;
	                    InetAddress address = InetAddress.getByAddress(ip);
	                    String output = address.toString().substring(1);
	                    if (address.isReachable(5000)) {
	                        System.out.println(output + " is on the network");
	                    } else {
	                        System.out.println("Not Reachable: "+output);
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }).start();     
	    }
	}
}
