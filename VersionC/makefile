CC = gcc
CFLAGS = -Wall
GFLAGS = -fpic
LIB = libringo.so
EXEC=$(LIB) start
RM = rm
RMFLAGS = -f

#créations  des éxécutables
all :$(EXEC)

start: main.c  $(LIB)
	$(CC) -o $@ main.c $(LIB)

#construction de la bibliotheque dynamique bas niveau
libringo.so: entity.o error.o
	gcc -shared -o $(LIB) $^
	cp libringo.so /lib

#depndance des fichiers objets et des fichiers d'en-tetes
entity.o: entity.c error.h
	$(CC) $(GFLAGS) $(CFLAGS) -c  entity.c
error.o: error.c error.h
	gcc $(GFLAGS) $(CFLAGS) -c error.c

#nettoyage
clean :
	$(RM) $(RMFLAGS) *.o
mrproper: clean
	$(RM) $(RMFLAGS) $(EXEC)
