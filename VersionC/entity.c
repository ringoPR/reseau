/*------------------------------------------------------------------------*/
/*                  CODE SOURCE DE L'API PROTOCOLE RINGO                  */
/*------------------------------------------------------------------------*/
#include "entity.h"


int id =0;
int portUDP=9998;
char myIP[64]="";
int portTCP=4242;
int nextUDP=0;
char nextIP[64]="";
int diff_port=9980;
char diff_ip[64]="225.006.002.004";



int init_Entity(int argc ,char * argv[]){
  if(argc >1){
      printf("Insertion de l'entity au port  %d \n",atoi(argv[1]));
      id= atoi(argv[5]);
      portUDP=atoi(argv[3]);
      portTCP=atoi(argv[4]);
      char* p=getIP();
      strcpy(myIP,p);
      client* clt=NULL;
      clt=malloc(sizeof(client));
      int r =create_client(clt,atoi(argv[1]),argv[2]);
      return r;
  }
  else{
    printf("Creation de L'anneau\n");
    id=1;
    char* p=getIP();
    strcpy(myIP,p);
    nextUDP=portUDP;
    strcpy(nextIP,myIP);
    server* serv=NULL;
    serv=malloc(sizeof(server));
    int r =create_serv(portTCP,serv);
    return r;
  }


}

int create_serv(int port,server* serv){
   serv->socket_TCP =socket(PF_INET,SOCK_STREAM,0);
   struct sockaddr_in adress_sock;
   adress_sock.sin_family = AF_INET;
   adress_sock.sin_port = htons(port);
   adress_sock.sin_addr.s_addr=htonl(INADDR_ANY);
   int r=bind(serv->socket_TCP,(struct sockaddr *)&adress_sock,sizeof(struct sockaddr_in));
    if(r==0){
      listen(serv->socket_TCP,0);
      information();
      while(1){

        struct sockaddr_in caller;
        socklen_t size= sizeof(caller);
        int *sock2=(int *)malloc(sizeof(int));
        *sock2= accept(serv->socket_TCP,(struct sockaddr *)&caller,&size);
        if(*sock2>=0){
          pthread_t th;
          if(pthread_create(&th,NULL,comm,sock2)==0){
            continue;
          }
          else{

            close(*sock2);
          }
        }
        close(*sock2);
        return 0;
     }
    }
    else{
      return ERROR_CONNECT;
    }
}

void *comm(void *arg){
  
 int sock=*((int *)arg);
 char*res=malloc(512*sizeof(char));
 creationTrame(0,res);
 write(sock,res,strlen(res));
 char*ress=malloc(512*sizeof(char));
 read(sock,ress,512*sizeof(char));
 traitemnentTrame(ress);
 char* res1=malloc(512*sizeof(char));
 creationTrame(2,res1);
 write(sock,res1,strlen(res1));
 close(sock);
 return NULL;
}

int create_client(client* clt,int port,char* ip){
  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  int r1 = inet_aton(ip,&adress_sock.sin_addr);
  adress_sock.sin_port = htons(port);
  if(r1==-1){
     return ERROR_CONNECT;
  }
  clt->socket_TCP=socket(AF_INET,SOCK_STREAM,0);
  int r=connect(clt->socket_TCP,(struct sockaddr *)&adress_sock,sizeof(struct sockaddr_in));
  if(r!=-1){
    traitemnentInsertion(clt->socket_TCP,clt);
    return 0;
  }
  else{
    return ERROR_CONNECT ;
  }
}

int traitemnentInsertion(int sock,client *clt){
    clt->recep=malloc(512*sizeof(char));
    read(sock,clt->recep,512*sizeof(char));
    traitemnentTrame(clt->recep);

    char*res=malloc(25*sizeof(char));
    creationTrame(1,res);

    write(sock,res,strlen(res));
    char*ress=malloc(512*sizeof(char));
    read(sock,ress,512*sizeof(char));
    traitemnentTrame(ress);

    return 0;
}

int traitemnentTrame(char* msg){
    char path1[strlen(msg)];
    strcpy(path1,msg);
    char** res=str_split(path1," ");
    if(strcmp(res[0],"WELC")==0){
      strcpy(nextIP,res[1]);
      nextUDP=atoi(res[2]);
      strcpy(diff_ip,res[3]);
      diff_port=atoi(res[4]);
      return 0;
    }
    if(strcmp(res[0],"NEWC")==0){
      strcpy(nextIP,res[1]);
      nextUDP=atoi(res[2]);
      return 0;
    }
    if(strcmp(res[0],"ACKC")==0){
      printf("insertion reussite\n");
      // je lance le serveur_tcp et l'emetteur udp
      server* serv=NULL;
      serv=malloc(sizeof(server));
      create_serv(portTCP,serv);
      return 0;
    }
    else{
      return ERROR_TRAME;
    }

}

void creationTrame(int flag,char res[]){

    if(flag==0){
      strcat(res,"WELC ");
      strcat(res,nextIP);
      strcat(res," ");
      char p[10];
      sprintf(p,"%d",portUDP);
      strcat(res,p);
      strcat(res," ");
      strcat(res,diff_ip);
      strcat(res," ");
      sprintf(p,"%d",diff_port);
      strcat(res,p);
      strcat(res,"\n");
    }
    if(flag==1){
      strcpy(res,"NEWC ");
      strcat(res,myIP);
      strcat(res," ");
      char p[10];
      sprintf(p,"%d",portUDP);
      strcat(res,p);
      strcat(res,"\n");
    }
    if(flag==2){
      strcat(res,"ACKC ");
    }

}

void information(){
  printf("Bienvenue dans Protocole ringo\nEntité N° :%d\nAdresse IP :%s\nNuméro de port TCP :%d\n",id,myIP,portTCP);
  printf("Numéro de port UDP :%d\nAdresse IP de l'entité suivante :%s\nNuméro de port UDP de l'entite suivante :%d\n",portUDP,nextIP,nextUDP);
  printf("Adresse Multidiffision :%s\nNuméro de port Multidiffision de l'entite :%d\n",diff_ip,diff_port);
}


char **str_split(char *s, const char *ct){
   char **tab = NULL;
   if (s && ct)
   {
      int i;
      char *cs = NULL;
      size_t size = 1;
      for (i = 0; (cs = strtok (s, ct)); i++)
      {
         if (size <= i + 1)
         {
            void *tmp = NULL;
            size <<= 1;
            tmp = realloc (tab, sizeof (*tab) * size);
            if (tmp)
            {
               tab = tmp;
            }
            else
            {
               fprintf (stderr, "Memoire insuffisante\n");
               free (tab);
               tab = NULL;
               exit (EXIT_FAILURE);
            }
         }
         tab[i] = cs;
         s = NULL;
      }
      tab[i] = NULL;
   }
   return tab;
}







char* getIP(){
  struct ifaddrs *myaddrs, *ifa;
  struct sockaddr_in *s4;
  int status;
  char *ip=(char *)malloc(64*sizeof(char));
  status = getifaddrs(&myaddrs);
  if (status != 0){
    perror("Probleme de recuperation d'adresse IP");
    exit(1);
  }
  for (ifa = myaddrs; ifa != NULL; ifa = ifa->ifa_next){
    if (ifa->ifa_addr == NULL) continue;
    if ((ifa->ifa_flags & IFF_UP) == 0) continue;
    if ((ifa->ifa_flags & IFF_LOOPBACK) != 0) continue;
    if (ifa->ifa_addr->sa_family == AF_INET){
      s4 = (struct sockaddr_in *)(ifa->ifa_addr);
      if (inet_ntop(ifa->ifa_addr->sa_family, (void *)&(s4->sin_addr),ip,64*sizeof(char)) != NULL){
        return ip;
      }
    }
  }
  freeifaddrs(myaddrs);
  return NULL;
}
