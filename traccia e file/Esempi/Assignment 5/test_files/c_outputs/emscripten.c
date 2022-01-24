#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc somma
int somma(int x, int y) { return x + y; }

// Proc diff
int diff(int x, int y) { return x - y; }

// Proc mult
int mult(int x, int y) { return x * y; }

// Proc division
float division(int x, int y) {
  float result = 0.0;
  if (y != 0) {
    result = x / y;
  } else {
    printf("Impossibile\n");
    result = 0.0;
  }

  return result;
}

// Proc fibonacci
int fibonacci(int n) {
  int toReturn = 0;
  if (n == 0) {
    toReturn = 0;
  } else if (n == 1) {
    toReturn = 1;
  } else {
    toReturn = fibonacci(n - 1) + fibonacci(n - 2);
  }

  return toReturn;
}

// Proc exponential
int exponential(int x, int y) {
  int z = 1;
  int i = 1;
  while (i <= y) {
    z = z * x;
    i = i + 1;
  }

  return z;
}

// Proc printPeg
void printPeg(int peg) {
  if (peg == 1) {
    printf("left");
  } else if (peg == 2) {
    printf("center");
  } else {
    printf("right");
  }
}

// Proc hanoi
void hanoi(int n, int fromPeg, int usingPeg, int toPeg) {
  if (n != 0) {
    hanoi(n - 1, fromPeg, toPeg, usingPeg);
    printf("Move disk from ");
    printPeg(fromPeg);
    printf(" peg to ");
    printPeg(toPeg);
    printf(" peg.\n");
    hanoi(n - 1, usingPeg, fromPeg, toPeg);
  }
}

// Proc main
int main() {
  int input_num = 5;
  char name[512] = "Marco";
  printf("********* Operazioni *********\n");
  printf("Ciao ");
  printf("%s", name);
  printf("! Il numero scelto è il ");
  printf("%d", input_num);
  printf(".\n\n");
  printf("Sommo 10 al numero scelto -> il risultato è ");
  printf("%d", somma(input_num, 10));
  printf(".\n");
  printf("Sottraggo 10 al numero scelto -> il risultato è ");
  printf("%d", diff(input_num, 10));
  printf(".\n");
  printf("Moltiplico per 10 il numero scelto -> il risultato è ");
  printf("%d", mult(input_num, 10));
  printf(".\n");
  printf("Divido per 10 il numero scelto -> il risultato è ");
  printf("%f", division(input_num, 10));
  printf(".\n");
  printf("Il valore del numero scelto elevato alla 5 è ");
  printf("%d", exponential(input_num, 5));
  printf(".\n");
  printf("Il valore di Fibonacci per il numero scelto è ");
  printf("%d", fibonacci(input_num));
  printf(".\n\n");
  printf("********* Hanoi *********\n");
  printf("Risolvo il rompicato della torre di Hanoi con 5 pioli");
  hanoi(input_num, 1, 2, 3);
  printf("\n");

  return 0;
}
