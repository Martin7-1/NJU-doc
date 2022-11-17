MAINFILE = $(shell find . -name "main.cpp")
PRINTFILE = $(shell find . -name "print.asm")

compile: $(MAINFILE) $(PRINTFILE)
	nasm -f elf32 -g -o print.o print.asm
	g++ -c -std=c++11 -m32 src/util.cpp -o util.o
	g++ -c -std=c++11 -m32 src/FAT12.cpp -o FAT12.o
	g++ -c -std=c++11 -m32 src/Operation.cpp -o Operation.o
	g++ -c -std=c++11 -m32 src/LSOperation.cpp -o LSOperation.o
	g++ -c -std=c++11 -m32 src/LS_LOperation.cpp -o LS_LOperation.o
	g++ -c -std=c++11 -m32 src/CatOperation.cpp -o CatOperation.o
	g++ -std=c++11 -m32 main.cpp print.o util.o FAT12.o Operation.o LSOperation.o LS_LOperation.o CatOperation.o -o main
	rm -f print.o
	rm -f util.o
	rm -f FAT12.o
	rm -f Operation.o
	rm -f LS_LOperation.o
	rm -f LSOperation.o
	rm -f CatOperation.o

run: compile
	bash -c ./main

clean:
	rm -f main