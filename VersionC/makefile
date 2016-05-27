CC = gcc
CFLAGS = -Wall
GFLAGS = -fpic
THFLAGS= -pthread
#LIB = libringo.so
EXEC=start
RM = rm
RMFLAGS = -f

#créations  des éxécutables
all :$(EXEC)

start: main.c entity.o error.o
	$(CC) $(THFLAGS) -o $@ main.c  entity.o error.o

#construction de la bibliotheque
#libringo.so: entity.o error.o
#	gcc -shared -o $(LIB) $^
#	cp libringo.so /lib

#depndance des fichiers objets et des fichiers d'en-tetes
entity.o: entity.c error.h
	$(CC) $(GFLAGS) $(CFLAGS) -c  entity.c
error.o: error.c error.h
	gcc $(GFLAGS) $(CFLAGS) -c error.c

#nettoyage
clean :
	$(RM) $(RMFLAGS) *.o
	$(RM) $(RMFLAGS) $(EXEC)
