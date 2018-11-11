import java.io.*;      
import java.net.*;
import java.util.*;

public class A_ServerThread extends Thread 
{
	private Socket mySocket;
	private ArrayList<Integer> portList;
	private ArrayList<String> machineNames;
	private ArrayList<Integer> ids;
	private int clientSocketNumber, serverSocketNumber;
	private int nPlayers;
	private String thread;
	public A_ServerThread(int name, Socket socket, ArrayList<Integer> portList, ArrayList<String> machineNames, int clientSocketNumber, int serverSocketNumber, int players, ArrayList<Integer> ids) 
	{
		mySocket = socket;
		System.out.println(mySocket.toString());
		this.portList = portList;
		this.clientSocketNumber = clientSocketNumber;
		this.serverSocketNumber = serverSocketNumber;
		this.machineNames = machineNames;
		this.nPlayers = players;
		this.ids = ids;
		thread = "" + name;
	} // FileReaderThread constructor

	public void run() 
	{
		InetAddress inetAddr;
		ObjectInputStream from_client;
		ObjectOutputStream to_client;
		
		try 
		{
			inetAddr = mySocket.getInetAddress();
			to_client = new ObjectOutputStream(mySocket.getOutputStream());
			from_client = new ObjectInputStream(mySocket.getInputStream());
			
			to_client.writeObject(nPlayers); // >to_client.writeObject(portList.size());
			System.out.println("Thread " + thread + ", finsihed writing " + portList.size() + " to clients");
			
			int clientID = (int)from_client.readObject(); // <<
			ids.add(clientID);
			Collections.sort(ids);
			System.out.println("Thread " + thread + ", client sent back " + clientID);
			
			String clientHost = (String) from_client.readObject(); // <<<
			System.out.println("Thread " + thread + ", is not blocked here");
			machineNames.add(clientHost);
			Collections.sort(machineNames);
			System.out.println(clientHost);
			
			String[] machines = new String[nPlayers];
			int[] copyIds = new int[nPlayers];
			while (true)
			{
				if (ids.size() == nPlayers)
				{
					synchronized (copyIds)
					{
						for (int i = 0; i < nPlayers; i++)
						{
							copyIds[i] = ids.get(i);
							machines[i] = machineNames.get(i);
							
							System.out.println("Thread " + thread + ", ids[" + i + "] = " + copyIds[i]);
							System.out.println("Thread " + thread + ", machines[" + i + "] = " + machines[i]);
						}
					}
					break;
				}
				System.out.println("Thread " + thread + ", help i'm stuck");
				try {
					Thread.sleep(100);
				}
				catch (Exception e)
				{
					
				}
				System.out.println(" Thread " + thread + ", ids.size(): " + ids.size() + "\n\t nPlayers: " + nPlayers);
				continue;
			}
			System.out.println("Thread " + thread + ", ids size " + ids.size());
			System.out.println("Thread " + thread + ", machines size " + machineNames.size());
			
			
			to_client.writeObject(copyIds); // >>>>
			System.out.println("Thread " + thread + ", sent ids");
			from_client.readObject(); // <<<<<
			System.out.println("Thread " + thread + ", confirmed that client got our message");
			to_client.writeObject(machines); // >>>>>>
			System.out.println("Thread " + thread + ", sent machines");
			from_client.readObject();
			System.out.println("Thread " + thread + ", our client received their machines");
		
		}
		catch (Exception e)
		{
			System.out.println("caught an exception!");
			e.printStackTrace();
		}
	} // run

} // FileReaderThread class
