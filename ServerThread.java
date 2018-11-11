import java.io.*;      
import java.net.*;
import java.util.*;

public class ServerThread extends Thread 
{
	private Socket mySocket;
	private ArrayList<Integer> portList;
	private ArrayList<String> machineNames;
	private int clientSocketNumber, serverSocketNumber;
	
	public ServerThread(Socket socket, ArrayList<Integer> portList, ArrayList<String> machineNames, int clientSocketNumber, int serverSocketNumber) 
	{
		mySocket = socket;
		System.out.println(mySocket.toString());
		this.portList = portList;
		this.clientSocketNumber = clientSocketNumber;
		this.serverSocketNumber = serverSocketNumber;
		this.machineNames = machineNames;
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
			
			
			while (machineNames.size() != portList.size()) // revert back to a variable (number of players)
			{
				System.out.println("waiting to receive host name");
				System.out.println("names size " + machineNames.size() + ", portList " + portList.size());
				String s = (String) from_client.readObject(); // <<
				machineNames.add(s);
				System.out.println("received host name " + s);
			}
			
			int clientsID = (int)from_client.readObject(); // <
			
			System.out.println("in here");
			to_client.writeObject(portList.size()); // <<<<
			to_client.flush();
			
			for (int i = 0; i < portList.size(); i++)
			{
				to_client.writeObject(portList.get(i)); // <<<<<<
				to_client.flush();
			}
			synchronized (machineNames)
			{
				Collections.sort(machineNames);
			}
			if (clientsID == portList.size() - 1)
			{
				System.out.println("sending machine name: " + machineNames.get(0) + " to " + machineNames.get(clientsID));
				to_client.writeObject(machineNames.get(0));
			}
			else 
			{
				System.out.println("sending machine name: " + machineNames.get(clientsID + 1) + " to " + machineNames.get(clientsID));
				to_client.writeObject(machineNames.get(clientsID + 1));
			}
		}
		catch (Exception e)
		{
			System.out.println("caught an exception!");
			e.printStackTrace();
		}
	} // run

} // FileReaderThread class
