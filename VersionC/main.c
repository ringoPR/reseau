#include "entity.h"

int main(int argc,char*argv[]){
  //init_Entity(argc,argv);
  char**a=str_split("WELC AAAA");
  printf("%s\n",a[0]);
  /*
  char str[512] = "WELC 127.0.0.1 4242 255.255.255.255 9980\n";
   const char s[2] = " ";
   char *token;


   token = strtok(str, s);
   int i=0;
   char** res=malloc(10*sizeof(char*));

   while( token != NULL )
   {
     res[i]=token;
      printf( " %s %s\n", token,res[i] );
      i++;
      token = strtok(NULL, s);
   }
   printf("%d\n",i );
*/
   return(0);
}
