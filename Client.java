import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
	public static void main(String[] args) 
	{
	Socket socket;
	ServerSocket listen;
	ObjectInputStream from_server;
	ObjectOutputStream to_server;
	int clientPort, serverPort;
	ArrayList<Integer> portNumberList = new ArrayList<Integer>();
	if (args.length < 3)
	{
		System.out.println("Need 3 arguments");
		System.out.println("java Client <hostname> <clientHostName> <uniqueID>");
		System.exit(1);
	}
	String host = args[0];
	String clientHost = args[1];
	int uniqueID = Integer.parseInt(args[2]);
	System.out.println("Attempting to contact " + host);
	try {
		socket = new Socket(host, 11445);
		System.out.println(socket.toString());
		to_server = new ObjectOutputStream(socket.getOutputStream());
		from_server = new ObjectInputStream(socket.getInputStream());

		
		System.out.println("sending host name, " + clientHost + ", to server");
		to_server.writeObject(clientHost); // <<
		to_server.flush();
		System.out.println("done sending host name, " + clientHost + ", to server");
		int totalPlayers = 0;
		
		to_server.writeObject(uniqueID); // <
		to_server.flush();
		
		while (true)
		{
			totalPlayers = (int)from_server.readObject(); // <<<<
			System.out.println(clientHost + " received message " + totalPlayers + " from harvard");
			
			for (int i = 0; i < totalPlayers; i++)
			{
				int portNumber = (int)from_server.readObject(); // <<<<<<
				portNumberList.add(portNumber);
				System.out.println("added " + portNumber + " to our list");
			}
			break;
		}
		
		String nextMachineName = (String) from_server.readObject();
		System.out.println(nextMachineName + " received as the machine to connect to");
		listen = new ServerSocket(portNumberList.get(uniqueID));
		System.out.println("listen is on port " + portNumberList.get(uniqueID));
		Socket clientSocket = null;
		
		System.out.println("uniqueID: " + uniqueID);
		if (uniqueID % 2 == 0)
		{
			System.out.println("waiting for " + nextMachineName);
			clientSocket = listen.accept();
			System.out.println("connected to " + nextMachineName);
		}
		if (uniqueID % 2 == 1)
		{
			System.out.println("connecting to another server");
			try 
			{
				if (uniqueID == portNumberList.size() - 1)
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(0));
					clientSocket = new Socket(nextMachineName, portNumberList.get(0));
				}
				else 
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(uniqueID + 1));
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
				}
			} 
			catch (Exception e)
			{
				if (uniqueID == portNumberList.size() - 1)
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(0));
					clientSocket = new Socket(nextMachineName, portNumberList.get(0));
				}
				else 
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(uniqueID + 1));
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
				}
			}
			
		}
		if (uniqueID % 2 == 1)
		{
			System.out.println("waiting for " + nextMachineName);
			clientSocket = listen.accept();
			System.out.println("connected to " + nextMachineName);
		}
		if (uniqueID % 2 == 0)
		{
			System.out.println("connecting to another server");
			try
			{
				if (uniqueID == portNumberList.size() - 1)
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(0));
					clientSocket = new Socket(nextMachineName, portNumberList.get(0));
				}
				else 
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(uniqueID + 1));
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));	
				}
			}
			catch (Exception e)
			{
				if (uniqueID == portNumberList.size() - 1)
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(0));
					clientSocket = new Socket(nextMachineName, portNumberList.get(0));
				}
				else 
				{
					System.out.println("connecting to: " + nextMachineName);
					System.out.println("connecting to port number: " + portNumberList.get(uniqueID + 1));
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
				}
			}
		}
		
		ObjectOutputStream to_player = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream from_player = new ObjectInputStream(clientSocket.getInputStream());
	
		System.out.println("GAME STARTING");
		System.out.println("YOUR BOARD POSITION IS AT 0");
		int myPosition = 0;
		if (uniqueID == 0)
		{
			Random rand = new Random();
			Scanner scanner = new Scanner(System.in);
			System.out.println("PLAYER ONE YOUR MOVE!");
			if (scanner.nextLine().equals("m"))
			{
				int stepsToTake = rand.nextInt(7);
				
				// "skipping a turn" this would actually just give you 0 steps
				if (stepsToTake == 0)
				{
					int skipTurn = rand.nextInt(100);
					if (skipTurn < 7)
					{	
						stepsToTake = 0;
					}
					else 
					{
						while(stepsToTake == 0)
						{
							stepsToTake = rand.nextInt(7);
						}
					}
				}
				myPosition += stepsToTake;
				if (myPosition >= 100)
				{
					System.out.println("PLAYER " + uniqueID + ", CONGRATULATIONS!! YOU WON");
				}
				else 
				{
					System.out.println("PLAYER " + uniqueID + ", your new position is at " + myPosition);
					if (scanner.nextLine().equals("f"))
					{
						to_player.writeObject("your move");
						to_player.flush();
					}
				}
			}
			
		}		
		while (true)
		{
			System.out.println((String)from_player.readObject());
			Random rand = new Random();
			Scanner scanner = new Scanner(System.in);
			if (scanner.nextLine().equals("m"))
			{
				int stepsToTake = rand.nextInt(7);
				
				// "skipping a turn" this would actually just give you 0 steps
				if (stepsToTake == 0)
				{
					int skipTurn = rand.nextInt(100);
					if (skipTurn < 7)
					{	
						stepsToTake = 0;
					}
					else 
					{
						while(stepsToTake == 0)
						{
							stepsToTake = rand.nextInt(7);
						}
					}
				}
				myPosition += stepsToTake;
				if (myPosition >= 100)
				{
					System.out.println("PLAYER " + uniqueID + ", CONGRATULATIONS!! YOU WON");
				}
				else 
				{
					System.out.println("PLAYER " + uniqueID + ", your new position is at " + myPosition);
					if (scanner.nextLine().equals("f"))
					{
						to_player.writeObject("your move");
					}
				}
			}
		}
	}
	catch (Exception e) {	// report any exceptions
		System.err.println(e);
		e.printStackTrace();
	}
	} // main
} // Client class

// Player extends Thread
