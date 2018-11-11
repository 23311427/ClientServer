import java.io.*;
import java.net.*;
import java.util.*;

public class A_Client
{
	public static void main(String[] args) 
	{
	Socket socket;
	ServerSocket listen;
	ObjectInputStream from_server;
	ObjectOutputStream to_server;
	int clientPort, serverPort;
	ArrayList<Integer> portNumberList = new ArrayList<Integer>();
	ArrayList<String> allMachines = new ArrayList<String>();
	ArrayList<String> playerMachines = new ArrayList<String>();
	if (args.length < 3)
	{
		System.out.println("Need 3 arguments");
		System.out.println("java Client <hostname> <clientHostName> <uniqueID>");
		System.exit(1);
	}
	String host = args[0];
	String clientHost = args[1];
	int uniqueID = Integer.parseInt(args[2]);
	
	// add all 228 machine names
	allMachines.add("harbor");
	allMachines.add("hardiment");
	allMachines.add("harding");
	allMachines.add("hardship");
	allMachines.add("hardware");
	allMachines.add("hardy");
	allMachines.add("hargreaves");
	allMachines.add("harken");
	allMachines.add("harlan");
	allMachines.add("harlem");
	allMachines.add("harlequin");
	allMachines.add("harmaline");
	allMachines.add("harmattan");
	allMachines.add("harmful");
	allMachines.add("harmine");
	allMachines.add("harmless");
	allMachines.add("harmonica");
	allMachines.add("harmony");
	allMachines.add("harmotome");
	allMachines.add("harpoon");
	allMachines.add("harpsichord");
	allMachines.add("harquebus");
	allMachines.add("harrumph");
	allMachines.add("hartal");
	allMachines.add("hartford");
	allMachines.add("harvard");
	allMachines.add("harvestry");
	allMachines.add("harvill");
	allMachines.add("harwood");
	
	System.out.println("Attempting to contact " + host);
	try {
		socket = new Socket(host, 13573);
		System.out.println(socket.toString());
		to_server = new ObjectOutputStream(socket.getOutputStream());
		from_server = new ObjectInputStream(socket.getInputStream());

		int totalPlayers = 0;
		
		totalPlayers = (int) from_server.readObject(); // <totalPlayers = (int) from_server.readObject();
		System.out.println("portListSize is " + totalPlayers);
		
		to_server.writeObject(uniqueID); // >>
		System.out.println("player " + uniqueID + " finished sending their id");
		to_server.flush();
		
		to_server.writeObject(clientHost); // >>>
		System.out.println("id " + uniqueID + " finished sending their machine");
		//to_server.flush();
		
		System.out.println("id " + uniqueID + " waiting for ids");
		int[] ids = (int [])from_server.readObject(); // <<<<
		System.out.println("read ids");
		to_server.writeObject("received"); // >>>>>
		String[] machines = (String []) from_server.readObject(); // <<<<<<
		System.out.println("read machines");
		to_server.writeObject("received machines");
		for (int i = 0; i < machines.length; i++)
		{
			portNumberList.add(13574 + i);
		}
		System.out.println("listening on port " + portNumberList.get(uniqueID));
		listen = new ServerSocket(portNumberList.get(uniqueID));
		Socket clientSocket = null;
		
	/* 	System.out.println("machine length is " + machines.length);
		System.out.println("ids length is " + ids.length);
		System.out.println("port list length is " + portNumberList.size()); */
		
	/* 	for (int i = 0; i < machines.length; i++)
		{
			System.out.println("machine name at " + i + " is " + machines[i]);
			System.out.println("id at " + i + " is " + ids[i]);
			System.out.println("port number at " + i + " is " + portNumberList.get(i));
		} */		
		System.out.println("uniqueID: " + uniqueID);
		String nextMachineName, previousMachineName;
		int portToListen;
		if (uniqueID == machines.length - 1)
		{
			nextMachineName = machines[0];
			portToListen = portNumberList.get(0);
		}
		else 
		{
			nextMachineName = machines[uniqueID + 1];
			portToListen = portNumberList.get(uniqueID);
		}
		
		if (uniqueID == 0)
		{
			previousMachineName = machines[portNumberList.size() - 1];
		}
		else 
		{
			previousMachineName = machines[uniqueID - 1];
		}
		
		System.out.println(clientHost + " will listen on " + portToListen);
		System.out.println(clientHost + " will connect to " + nextMachineName);
		
		ObjectOutputStream to_right = null;
		ObjectInputStream from_right = null;
		
		ObjectOutputStream to_left = null;
		ObjectInputStream from_left = null;
		
		// case 1
		if (totalPlayers % 2 == 0)
		{
			if (uniqueID % 2 == 0)
			{
				System.out.println("waiting for " + previousMachineName);
				clientSocket = listen.accept();
				System.out.println("connected to " + previousMachineName);
				to_left = new ObjectOutputStream(clientSocket.getOutputStream());
				from_left = new ObjectInputStream(clientSocket.getInputStream());
			}
			try {
				Thread.sleep(200);
			}
			catch (Exception e)
			{
				
			}
			// even clients have streams to their left
			if (uniqueID % 2 == 1)
			{
				try 
				{
					if (uniqueID == totalPlayers - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
					else 
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
				} 
				catch (Exception e)
				{
					if (uniqueID == portNumberList.size() - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
					else 
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
				}
				
			} // odd clients have streams to their right
			if (uniqueID % 2 == 1)
			{
				System.out.println("waiting for " + previousMachineName);
				clientSocket = listen.accept();
				to_left = new ObjectOutputStream(clientSocket.getOutputStream());
				from_left = new ObjectInputStream(clientSocket.getInputStream());
				System.out.println("connected to " + previousMachineName);
			} // odd clients have streams to their left now
			try {
				Thread.sleep(200);
			}
			catch (Exception e)
			{
				
			}
			if (uniqueID % 2 == 0)
			{
				System.out.println("connecting to another server");
				try
				{
					if (uniqueID == portNumberList.size() - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
					else 
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));	
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
				}
				catch (Exception e)
				{
					if (uniqueID == portNumberList.size() - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
					else 
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
						to_right = new ObjectOutputStream(clientSocket.getOutputStream());
						from_right = new ObjectInputStream(clientSocket.getInputStream());
					}
				}
			}// even have streams to the right
		}
		
		//case 2
		if (totalPlayers % 2 == 1)
		{
			if (uniqueID % 2 == 1)
			{
				System.out.println("waiting for " + previousMachineName);
				clientSocket = listen.accept();
				to_left = new ObjectOutputStream(clientSocket.getOutputStream());
				from_left = new ObjectInputStream(clientSocket.getInputStream());
				System.out.println("connected to " + previousMachineName);
			} // odds have streams on the left
			try {
				Thread.sleep(300);
			}
			catch (Exception e)
			{
				
			}
			
			if (uniqueID % 2 == 0 && uniqueID != totalPlayers - 1)
			{
				try 
				{
					/* if (uniqueID == totalPlayers - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
					}
					else 
					{ */
					System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
					to_right = new ObjectOutputStream(clientSocket.getOutputStream());
					from_right = new ObjectInputStream(clientSocket.getInputStream());
					//}//evens have streams on the right
				} 
				catch (Exception e)
				{
				/* 	if (uniqueID == totalPlayers - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
					}
					else 
					{ */
					System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));
					to_right = new ObjectOutputStream(clientSocket.getOutputStream());
					from_right = new ObjectInputStream(clientSocket.getInputStream());
					//} even have stuff on their right
				}
				
			}
			if (uniqueID % 2 == 0 && uniqueID != 0)
			{
				System.out.println("waiting for " + previousMachineName);
				clientSocket = listen.accept();
				to_left = new ObjectOutputStream(clientSocket.getOutputStream());
				from_left = new ObjectInputStream(clientSocket.getInputStream());
				System.out.println("connected to " + previousMachineName);
			}// evens have lefts
			try {
				Thread.sleep(200);
			}
			catch (Exception e)
			{
				
			}
			if (uniqueID % 2 == 1)
			{
				System.out.println("connecting to another server");
				try
				{
					/* if (uniqueID == portNumberList.size() - 1)
					{
						System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
						clientSocket = new Socket(nextMachineName, portNumberList.get(0));
						to_left = new ObjectOutputStream(clientSocket.getOutputStream());
						from_left = new ObjectInputStream(clientSocket.getInputStream());
					}
					else 
					{ */
					System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));	
					to_right = new ObjectOutputStream(clientSocket.getOutputStream());
					from_right = new ObjectInputStream(clientSocket.getInputStream());
					//}
				}
				catch (Exception e)
				{
					System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(uniqueID + 1)); 
					clientSocket = new Socket(nextMachineName, portNumberList.get(uniqueID + 1));	
					to_right = new ObjectOutputStream(clientSocket.getOutputStream());
					from_right = new ObjectInputStream(clientSocket.getInputStream());
				}// odds have right streams
			}
			if (uniqueID == 0)
			{
				clientSocket = listen.accept();
				to_left = new ObjectOutputStream(clientSocket.getOutputStream());
				from_left = new ObjectInputStream(clientSocket.getInputStream());
			}
			try {
				Thread.sleep(200);
			}
			catch (Exception e)
			{
				
			}
			if (uniqueID == totalPlayers - 1)
			{
				System.out.println("trying to connect to machine, " + nextMachineName + ". onport number: " + portNumberList.get(0)); 
				clientSocket = new Socket(nextMachineName, portNumberList.get(0));	
				to_right = new ObjectOutputStream(clientSocket.getOutputStream());
				from_right = new ObjectInputStream(clientSocket.getInputStream());
			}
		}
		
		
	
		System.out.println("GAME STARTING");
		System.out.println("YOUR BOARD POSITION IS AT 0");
		int myPosition = 0;
		if (uniqueID == 0) // revert back
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
						to_right.writeObject("your move");
						to_right.flush();
					}
				}
			}
			
		}		
		while (true)
		{
			System.out.println("waiting");
			System.out.println((String)from_left.readObject());
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
						try{
							Thread.sleep(100);
						}
						catch(InterruptedException e)
						{
							System.out.println(e);
						}  
						to_right.writeObject("your move Player " + (uniqueID + 1));
						try{
							Thread.sleep(100);
						}
						catch(InterruptedException e)
						{
							System.out.println(e);
						}  
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
