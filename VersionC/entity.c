#include "entity.h"

int id =0;
int portUDP=9998;//par default
int portTCP=0;
int nextUDP=9998;// par default
int diff_port=9980;
char* nextIP="";
char* diff_ip="";
char* myIP="127.0.0.1";


int init_Entity(int argc ,char * argv[]){
  if(argc >1){
    int a=atoi(argv[1]);
    if(a==1){
      // cas de la premiere connexion initialisation de l'anneau
      id= atoi(argv[2]);
      portTCP=atoi(argv[3]);
      nextIP=argv[4];
      diff_ip="255.255.255.255";
      server* serv=NULL;
      serv=malloc(sizeof(server));
      int r =create_serv(portTCP,serv);
      return r;
    }
    else{
      id= atoi(argv[2]);
      diff_port=atoi(argv[3]);
      client* clt=NULL;
      clt=malloc(sizeof(client));
      int r =create_client(clt,atoi(argv[5]),argv[4]);
      return r;
    }

  }
  else{

    return ERROR_ARG;
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
      while(1){
        information();
        struct sockaddr_in caller;
        socklen_t size= sizeof(caller);

        int sock2= accept(serv->socket_TCP,(struct sockaddr *)&caller,&size);
        if(sock2>=0){
          traitemnentInsertion(sock2,serv,NULL,0);
        }
        close(sock2);
        return 0;
     }
    }
    else{
      return ERROR_CONNECT;
    }
}

int traitemnentInsertion(int sock,server * serv,client *clt,int flag){
  if(flag==0){
    serv->send=malloc(512*sizeof(char));
    char res[512]="";
    creationTrame(0,res);
    serv->send=(char*)res;
    printf("j'ai envoie :%s\n",serv->send);
    write(sock,serv->send,strlen(serv->send));

    serv->recep=malloc(512*sizeof(char));
    read(sock,serv->recep,512*sizeof(char));
    traitemnentTrame(serv->recep);

    serv->send=malloc(512*sizeof(char));
    char res1[512]="";
    creationTrame(2,res1);
    serv->send=(char*)res1;
    write(sock,serv->send,strlen(serv->send));
  }
  else{
    printf("je suis dans le client \n");
    clt->recep=malloc(512*sizeof(char));
    int r=0;
    r=read(sock,clt->recep,512*sizeof(char));
    printf("j'ai reçu %s \net nombr:%d\n",clt->recep,r);
    traitemnentTrame(clt->recep);

    clt->send=malloc(512*sizeof(char));
    char res[512]="";
    creationTrame(1,res);
    clt->send=(char*)res;
    write(sock,clt->send,strlen(clt->send));

    clt->recep=malloc(512*sizeof(char));
    read(sock,clt->recep,512*sizeof(char));
    traitemnentTrame(clt->recep);
  }
  return 0;

}





int traitemnentTrame(char* msg){
    char** res=str_split(msg);
    if(strcmp(res[0],"WELC")==0){
      /*myIP=res[1];
      portTCP=atoi(res[2]);
      diff_ip=res[3];
      diff_port=atoi(res[4]);
      */
      return 0;

    }
    if(strcmp(res[0],"NEWC")==0){
      nextIP=res[1];
      nextUDP=atoi(res[2]);
      return 0;
    }
    if(strcmp(res[0],"ACKC\n")==0){
      printf("insertion reussite\n");
      // je lance le serveur_tcp et l'emetteur udp
      return 0;
    }
    else{
      return ERROR_TRAME;
    }

}

void creationTrame(int flag,char res[]){
    if(flag==0){
      strcat(res,"WELC ");
      strcat(res,myIP);// refaire
      strcat(res," ");
      char p[10];
      sprintf(p,"%d",portTCP);//refaire
      strcat(res,p);
      strcat(res," ");
      strcat(res,diff_ip);
      strcat(res," ");
      sprintf(p,"%d",diff_port);
      strcat(res,p);
      strcat(res,"\n");
    }
    if(flag==1){
      strcat(res,"NEWC ");
      strcat(res,myIP);
      strcat(res," ");
      char p[10];
      sprintf(p,"%d",portUDP);
      strcat(res,p);
      strcat(res,"\n");
    }
    if(flag==2){
      res="ACKC\n";
    }

}

void information(){
  printf("Bienvenue dans Protocole ringo\nEntité N° :%d\nAdresse IP :%s\nNuméro de port TCP :%d\n",id,myIP,portTCP);
  printf("Numéro de port UDP :%d\nAdresse IP de l'entité suivante :%s\nNuméro de port UDP de l'entite suivante :%d\n",portUDP,nextIP,nextUDP);
  printf("Adresse Multidiffision :%s\nNuméro de port Multidiffision de l'entite :%d\n",diff_ip,diff_port);
}

char** str_split(char* str)
{
  //char str[512] = "WELC 127.0.0.1 4242 255.255.255.255 9980\n";
   const char s[2] = " ";
   char *token;


   token = strtok(str, s);
   int i=0;
   //char** res=malloc(10*sizeof(char*));

   while( token != NULL )
   {
     //res[i]=token;
      printf( " %s\n", token);
      i++;
      token = strtok(NULL, s);
   }
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
    traitemnentInsertion(clt->socket_TCP,NULL,clt,1);
    return 0;
  }
  else{
    return ERROR_CONNECT ;
  }
}
