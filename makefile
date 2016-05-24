RM = rm
RMFLAGS = -f


#créations  des éxécutables
all :
			make -C VersionJava
			make -C VersionC
#nettoyage
clean :
	make -C VersionJava clean
	make -C VersionC clean
