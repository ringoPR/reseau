
#Projet Protocol Ringo
=======================

Adresse IPv4 de multi-diffusion (qui servira pour signaler la panne du réseau) :  224.0.0.0 à 239.255.255.255 <br>
<p>
Integer.parseInt(args[4]) 		: port_tcp_serveur_entite<br>
identifiant = Integer.parseInt(args[3]) : identifiant de l'entité<br>
Integer.parseInt(args[2]) 		: port d ecoute udp de l entiré<br>
addr_tcp = args[1] 			: adress tcp ou l entité va se connecter<br>
port_tcp = Integer.parseInt(args[0]) 	: port tcp ou l entité va se connecter<br>
</p>



<p>


java Entité_bis 127.0.0.1	  			   : premier lancement par defaut sont port_udp = 9998<br> 
						   : port_ecou_udp_mach_suivant = 9998 c'est le meme sinon ça creer pas d'anneau<br> 
						   : port_multi_diff = 9980<br>
					           : port_tcp_serveur_entite = 4242<br>
</p>

<p> 
java Entité_bis 4242 127.0.0.1 9997 4243 1     : args[0] = port tcp de la machine a communiquer <br>
						   : args[1] = addr_tcp de la machine a communiquer<br> 
						   : args[2] = son propre port udp d'écoute<br> 
						   : args[3] = port_tcp_serveur_entite <br>
						   : args[4] = identifiant  <br>
						   : args[5] = dup si on veux dupliquer  	<br> 

</p>

java Entité_bis 4243 127.0.0.1 9996 4244 2 <br>
java Entité_bis 4243 127.0.0.1 9995 4245 3 <br>
java Entité_bis 4243 127.0.0.1 9994 4246 4 <br>


exemple de duplication  : <br>

<p>
|   ||         | <br>
java Entité_bis<br>
java Entité_bis  4242 localhost 9997 4243 1<br>
java Entité_bis  4242 localhost 9996 4244 2<br>
java Entité_bis  4243 localhost 9995 4245 3
java Entité_bis  4243 localhost 9994 4256 4 dup
java Entité_bis  4243 localhost 9992 4257 5 dup
</p> 

