#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc primo
int primo(int x) {
  int z = 1;
  int i = 2;
  int j;
  if (x == 1) {
    z = 0;
  }
  while (i < x && z == 1) {
    j = x / i;
    j = x - j * i;
    if (j == 0) {
      z = 0;
    } else {
      i = i + 1;
    }
  }

  return z;
}

// Proc main
int main(int argc, char* argv[]) {
  int number1 = atoi(argv[1]);
  int x;
  x = number1;
  if (primo(x)) {
    printf("%d", x);
    printf(" è un numero primo\n");
  } else {
    printf("%d", x);
    printf(" non è un numero primo\n");
  }

  return 0;
}
