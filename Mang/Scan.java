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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Scan extends JFrame {
	private int width = 500;
    private int height = 600;
    
    static JTextArea output;
	public static void main(String[] args) throws SocketException, UnknownHostException {
		new Scan();
		

	}
	public Scan() {
        this.setSize(width, height);
        this.setTitle("Scan IP");
        
        output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBounds(100, 30, 300, 400);
        this.add(scroll);
        
        JButton scan = new JButton("Scan");
        scan.setBounds(160, 450, 70, 50);
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
	
	@SuppressWarnings("null")
	public static ArrayList<Integer> getNetWorkAddress() throws SocketException, UnknownHostException {
		InetAddress localHost = Inet4Address.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
		int u = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
		String ipAddress = localHost.toString();
		String array [] = ipAddress.split("\\/");
		String ip = array[1];
		String sm = getIPv4LocalNetMask(localHost, u );
		String [] ip_array = ip.split("\\.");
		String [] sm_array = sm.split("\\.");
		ArrayList<Integer> network = new ArrayList<Integer>();
		for(int i = 0 ; i < 4;i++) {
			int a = Integer.parseInt(ip_array[i]);
			int b=  Integer.parseInt(sm_array[i]);
			int c = (int) (a & b);
			network.add( c);
		}
		network.add(u);
		return network;
	
	}
	
	public static String getIPv4LocalNetMask(InetAddress ip, int netPrefix) {

	    try {
	        int shiftby = (1<<31);
	        for (int i=netPrefix-1; i>0; i--) {
	            shiftby = (shiftby >> 1);
	        }
	        String maskString = Integer.toString((shiftby >> 24) & 255) + "." + Integer.toString((shiftby >> 16) & 255) + "." + Integer.toString((shiftby >> 8) & 255) + "." + Integer.toString(shiftby & 255);
	        return maskString;
	    }
	        catch(Exception e){e.printStackTrace();
	    }
		return null;
	   
	}
	
	public static ArrayList<String> arangeIp(ArrayList<Integer> network){
		int u = network.get(4);
		int host = (int) (Math.pow(2, 32 - u) - 2);
		ArrayList<String> hostList = new ArrayList<String>();
		int i = 0;
		int a = network.get(0);
		int b = network.get(1);
		int c = network.get(2);
		int d = network.get(3);
		
		while(i < host) {
			String ip = "";
			d++;
			if(d > 255) {
				d = 0;
				c++;
			}
			if(c > 255) {
				c = 0;
				b ++;
				
			}
			if(b > 255) {
				b = 0;
				a ++;
			}
			if(a >255) {
				break;
			}
			ip = a + "." + b + "." + c + "." +d;
			hostList.add(ip);
			i++;
		}
		
		return hostList;
	}
	
	public static void getNetworkIPs() throws IOException {
	    ArrayList<Integer> network = getNetWorkAddress();
	    ArrayList<String> hostList = arangeIp(network);
	    for(String ipString : hostList) {
	    	
	    	new Thread(new Runnable() {
				@Override
				public void run() {
					InetAddress ipAdress;
					try {
						ipAdress = InetAddress.getByName(ipString);
						String out = ipAdress.toString().substring(1);
				    	if(ipAdress.isReachable(5000)) {
//				    		System.out.println(out + " is on the network");
				    		output.append(out + " is on the network\n");
				    		
				    	}else {
//				    		System.out.println("Not Reachable: "+out);
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
