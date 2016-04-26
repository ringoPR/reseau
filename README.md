
#Projet Protocol Ringo
=======================

Adresse IPv4 de multi-diffusion (qui servira pour signaler la panne du réseau) :  224.0.0.0 à 239.255.255.255

Integer.parseInt(args[4]) 		: port_tcp_serveur_entite
identifiant = Integer.parseInt(args[3]) : identifiant de l'entité
Integer.parseInt(args[2]) 		: port d ecoute udp de l entiré
addr_tcp = args[1] 			: adress tcp ou l entité va se connecter
port_tcp = Integer.parseInt(args[0]) 	: port tcp ou l entité va se connecter







java Entité_bis 	  			   : premier lancement par defaut sont port_udp = 9998 
						   : port_ecou_udp_mach_suivant = 9998 c'est le meme sinon ça creer pas d'anneau 
						   : port_multi_diff = 9980
					           : port_tcp_serveur_entite = 4242

 
java Entité_bis 4242 localhost 9997 4243 89888     : args[0] = port tcp de la machine a communiquer 
						   : args[1] = addr_tcp de la machine a communiquer 
						   : args[2] = son propre port udp d'écoute 
						   : args[3] = port_tcp_serveur_entite
						   : args[4] = identifiant  
						   : args[5] = dup si on veux dupliquer  	 

java Entité_bis 4243 localhost 9996 4244 1254


exemple de duplication  : 


|   ||         | 
java Entité_bis
java Entité_bis  4242 localhost 9997 4243 1
java Entité_bis  4242 localhost 9996 4244 2
java Entité_bis  4243 localhost 9995 4245 3
java Entité_bis  4243 localhost 9994 4256 4 dup
java Entité_bis  4243 localhost 9992 4257 5 dup
 

