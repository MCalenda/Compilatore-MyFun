#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc somma
int somma(int x, int y) {
  int z = x;
  int i = 1;
  while (i <= y) {
    z = z + 1;
    i = i + 1;
  }

  return z;
}

// Proc main
int main(int argc, char* argv[]) {
  int number1 = atoi(argv[1]);
  int number2 = atoi(argv[2]);
  int x;
  int y;
  int z;
  x = number1;
  y = number2;
  z = somma(x, y);
  printf("%d", x);
  printf(" + ");
  printf("%d", y);
  printf(" = ");
  printf("%d", z);

  return 0;
}
