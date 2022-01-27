#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc mcd
int mcd(int x, int y) {
  while (x != y) {
    if (x > y) {
      x = x - y;
    } else {
      y = y - x;
    }
  }

  return x;
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
  z = mcd(x, y);
  printf("Il massimo comune divisore di ");
  printf("%d", x);
  printf(" e ");
  printf("%d", y);
  printf(" è ");
  printf("%d", z);

  return 0;
}
