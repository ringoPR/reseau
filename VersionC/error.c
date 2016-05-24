/*-------------------------------------------------------------*/
/*                   TYPE ERROR PROTOCOLE RINGO                */
/*-------------------------------------------------------------*/



#include "error.h"

void ringo_perror(error e){
  char* res;

  switch(e){
  case OK : res="No error.";break;
  case ERROR_ARG : res="argument error \n regarder la discription dans le Manuel";break;
  case ERROR_CONNECT : res="connexion error";break;
  case ERROR_BIND : res="bind error";break;
  case ERROR_TRAME:res="Message mal fomate error";break;
  case ERROR_FLAG:res="Le flag existe pas error";break;
  default: res="Unknown error";break;
  }

  fprintf(stderr, "%s\n",res);
}
