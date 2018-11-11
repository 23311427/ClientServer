README

This will be a very simple game.
Press "m" and enter to make a move
Press "f" to finish a move

So when it is your turn, you will press "m" to move.
When you see your message, and you want to end: press "f".

When connecting to machine names...
Connect alphabetically. I did all my testing on the 228 macs the ones that all start 
with the letter 'h' but this should also work with the 930 lab names.

Example of connecting alphabetically. The client will accept command line arguments.
One of the command-line argument will be a machine name. Make sure you execute the
clients with machine names in alphabet order, as shown below. 
Also. uniqueID should start at 0 and increment by one for each subsequent player.
	java Client <host> harbor 0
	java Client <host> hardware 1
	java Client <host> harmony 2
	java Client <host> harpoon 3
I believe the connection would work connecting however way you want.

I have a small issue with getting odd number of players. 
The ring idea needs 4 streams when talking.

I had some trouble with getting the odd person connected.
	

	