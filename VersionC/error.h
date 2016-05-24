/*------------------------------------------------------------------*/
/*                   header TYPE ERROR PROTOCLE RINGO               */
/*------------------------------------------------------------------*/


#include <stdio.h>

typedef enum
{
  OK,
  ERROR_CONNECT,
  ERROR_BIND,
  ERROR_ARG,
  ERROR_TRAME,
  ERROR_FLAG,
}
error;

void ringo_perror(error e);
