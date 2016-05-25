Projet Protocole Ringo - version 1.0 - Mai 2016
===============================================
Réalisé dans le cadre du module Programmation Réseaux PR6<br>
Licence 3 informatique<br>
Université Paris Denis Diderot<br>

=============================================
Auteur : Belkacem OUALITEN & Bachir CHELBI & Tahi Mohand Tahar
=============================================


Ressources nécessaires :
=======================
Compilateur :<br>
	gcc<br>
	javac<br>
Serveur Local(ex :localhost "127.0.0.1")<br>

Compilation :
=============
 la commande : make ou make all permis de faire la compilation de tout le programme(VersionC et VersionJAVA)<br>

DESCRIPTION :
=============
c'est un programme (en version JAVA ou Version c)d'un protocole de communication basé sur une communication en
anneau (ring en anglais).<br> 
L’idée est que des entités communiquent entre elles par UDP et la topologie du réseau est un anneau.<br> 
La circulation des messages sur un anneau se fait de façon unidirectionnelle<br>

Exécution :
===========
	Version JAVA:
	=============

Pour executer le programe il faut rentrer dans le dossier bin est suivre les exemple ci dessou<br>

java Entité_bis 127.0.0.1 : premier lancement par defaut sont port_udp = 9998<br>
			  : port_ecou_udp_mach_suivant = 9998 c'est le meme sinon ça creer pas d'anneau<br>
			  : port_multi_diff = 9980<br>
			  : port_tcp_serveur_entite = 4242<br>

					   : args[0] = port tcp de la machine a communiquer<br>
				           : args[1] = addr_tcp de la machine a communiquer<br>
				           : args[2] = son propre port udp d'écoute<br>
					   : args[3] = port_tcp_serveur_entite<br>
					   : args[4] = identifiant<br>
					   : args[5] = dup si on veux dupliquer<br>
					   : args[6] = ip de la multi-diffusion<br>
					   : args[7] = port de la multi-diffusion<br>

java Entité_bis 4242 127.0.0.1 9997 4243 1<br>
java Entité_bis 4243 127.0.0.1 9996 4244 2<br>
java Entité_bis 4243 127.0.0.1 9995 4245 3<br>
java Entité_bis 4243 127.0.0.1 9994 4246 4<br>
java Entité_bis 4242 127.0.0.1 9993 4247 5<br>

Exemple de duplication :<br>

java Entité_bis 127.000.000.001<br>
java Entité_bis 4242 127.0.0.1 9997 4243 1<br>
java Entité_bis 4242 127.0.0.1 9996 4244 2<br>
java Entité_bis 4243 127.0.0.1 9995 4245 3<br>
java Entité_bis 4243 127.0.0.1 9994 4246 4 dup 225.6.2.3 9983<br>
java Entité_bis 4243 127.0.0.1 9993 4240 5 // ici l entité va refusé parceque elle est deja dupliquer<br>
java Entité_bis 4246 127.0.0.1 9992 4247 6<br>

Les commandes utiliser :
========================
	info      : donne les information de l'entité et les differente ip utiliser<br>
	clear     : efface le terminal<br>
	info protocole : donne les types des messages du protocole à utiliser<br>
remaque : pour les commande il ne faut pas utiliser d'espace<br>


Adresse IPv4 de multi-diffusion (qui servira pour signaler la panne du réseau) : 224.0.0.0 à 239.255.255.255<br>

Integer.parseInt(args[4]) : port_tcp_serveur_entite<br>
identifiant = Integer.parseInt(args[3]) : identifiant de l'entité<br>
Integer.parseInt(args[2]) : port d ecoute udp de l entiré<br>
addr_tcp = args[1] : adress tcp ou l entité va se connecter<br>
port_tcp = Integer.parseInt(args[0]) : port tcp ou l entité va se connecter<br>


Remarque 1:
==========
Il faut ajouter lors de l'éxcution du programme le paramétre -Djava.net.preferIPv4Stack=true.<br>

Remarque 2:
===========
	pour le message  [GBYE␣idm␣ip␣port␣ip-succ␣port-succ] : faut juste taper GBYE idm car on concatener automatiquement le reste <br>Et si l'entité et dupliquer un menu vous demandera  dans quelle anneau est qu elle ip-succ et port-succ<br>

Remarque au niveau du Transfert de fichier 3:
=============================================
 en fonction du l'endrois ou se trouve le programme à excuter faut changer le chemin ou se trouve le dossier shared dans mon cas il se trouve  dans ""/home/kira/workspace/projet_reseau_modifier2/shared" faut donc modifier dans les fonctions suivante dans le fichier Serveur_udp.java :<br>
 	recu_SEN  <br>
	taille_fichier<br>
	in_shared<br>

Version C:
==========
	Lancer la commande :<br>
		./start =>pour initialiser l'anneau<br>
	 pour s'inserer il faut se connecter :<br>
	 	./start 4242 addr_ip port_udp port_tcp num_entite<br>
