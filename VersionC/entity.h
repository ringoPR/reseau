#ifndef __ENTITY__
#define __ENTITY__

#include "error.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>
#include <sys/socket.h>
#include <string.h>
#include <assert.h>

extern int id;
extern char* myIP;
extern int portUDP;
extern int portTCP;
extern int nextUDP;
extern int diff_port;
extern char* nextIP;
extern char* diff_ip;


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
int create_client(client* clt,int port,char* ip);
int traitemnentInsertion(int sock,server * serv,client* clt,int flag);
int traitemnentTrame(char* msg);
char** str_split(char* str);
void creationTrame(int flag,char res[]);
void information();
#endif
