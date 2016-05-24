Pour executer le programe faut taper la commande make puis rentrer dans le dossier bin est suivre les exemple ci dessou 

les commande utiliser :
	info      : donne les information de l'entité et les differente ip utiliser 
	clear     : efface le terminal 
	info protocole : donne les types des messages du protocole a utiliser
remaque : pour les commande il ne faut pas utiliser d'espace 
 

Adresse IPv4 de multi-diffusion (qui servira pour signaler la panne du réseau) : 224.0.0.0 à 239.255.255.255

Integer.parseInt(args[4]) : port_tcp_serveur_entite
identifiant = Integer.parseInt(args[3]) : identifiant de l'entité
Integer.parseInt(args[2]) : port d ecoute udp de l entiré
addr_tcp = args[1] : adress tcp ou l entité va se connecter
port_tcp = Integer.parseInt(args[0]) : port tcp ou l entité va se connecter


Remarque : 
Il faut ajouter lors de l'éxcution du programme le paramétre -Djava.net.preferIPv4Stack=true .

java Entité_bis 127.0.0.1 : premier lancement par defaut sont port_udp = 9998
			  : port_ecou_udp_mach_suivant = 9998 c'est le meme sinon ça creer pas d'anneau
			  : port_multi_diff = 9980
			  : port_tcp_serveur_entite = 4242

					   : args[0] = port tcp de la machine a communiquer
				           : args[1] = addr_tcp de la machine a communiquer
				           : args[2] = son propre port udp d'écoute
					   : args[3] = port_tcp_serveur_entite
					   : args[4] = identifiant
					   : args[5] = dup si on veux dupliquer
					   : args[6] = ip de la multi-diffusion
					   : args[7] = port de la multi-diffusion

java Entité_bis 4242 127.0.0.1 9997 4243 1
java Entité_bis 4243 127.0.0.1 9996 4244 2
java Entité_bis 4243 127.0.0.1 9995 4245 3
java Entité_bis 4243 127.0.0.1 9994 4246 4
java Entité_bis 4242 127.0.0.1 9993 4247 5

exemple de duplication :

java Entité_bis 127.000.000.001
java Entité_bis 4242 127.0.0.1 9997 4243 1
java Entité_bis 4242 127.0.0.1 9996 4244 2
java Entité_bis 4243 127.0.0.1 9995 4245 3 
java Entité_bis 4243 127.0.0.1 9994 4246 4 dup 225.6.2.3 9983
java Entité_bis 4243 127.0.0.1 9993 4240 5 // ici l entité va refusé parceque elle est deja dupliquer 
java Entité_bis 4246 127.0.0.1 9992 4247 6 

Remarque :
 remarque :
	pour le message  [GBYE␣idm␣ip␣port␣ip-succ␣port-succ] : faut juste taper GBYE idm car on concatener automatiquement le reste , et
	si l'entité et dupliquer un menu vous demandera  dans quelle anneau est qu elle ip-succ et port-succ

 Remarque au niveau du Transfert de fichier : 
 en fonction du l'endrois ou se trouve le programme a excuter faut changer le chemin ou se trouve le dossier shared dans mon cas il se trouve  dans ""/home/kira/workspace/projet_reseau_modifier2/shared" faut donc modifier dans les fonctions recu_SEN , taille_fichier et in_shared


