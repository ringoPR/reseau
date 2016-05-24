/*------------------------------------------------------*/
/*                 API PROTOCOLE RINGO                  */
/*------------------------------------------------------*/
#ifndef __ENTITY__
#define __ENTITY__

#include "error.h"
#include <stdio.h>
#include <stdlib.h>
#include <net/if.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>
#include <sys/socket.h>
#include <string.h>
#include <assert.h>
#include <pthread.h>
#include <sys/types.h>
#include <ifaddrs.h>

extern int id;
extern char myIP[64];
extern int portUDP;
extern int portTCP;
extern int nextUDP;
extern int diff_port;
extern char nextIP[64];
extern char diff_ip[64];


typedef struct {
  int socket_UDP;
  int socket_TCP;
  char* recep;
  char* send;
} server;

typedef struct {
  int socket_UDP;
  int socket_TCP;
  char* recep;
  char* send;
} client;


int init_Entity(int argc,char* argv[]);
int create_serv(int port,server* serv);
void *comm(void *arg);
int create_client(client* clt,int port,char* ip);
int traitemnentInsertion(int sock,client* clt);
int traitemnentTrame(char* msg);
void creationTrame(int flag,char res[]);
char** str_split(char* s,const char *ct);
void information();
char* getIP();


#endif
