#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

// Proc main
int main(int argc, char* argv[]) {
  int number = atoi(argv[1]);
  int n;
  int i = 0;
  n = number;
  while (i <= n) {
    printf("numero i: ");
    printf("%d", i);
    printf(". Valore fibonacci: ");
    printf("%d", fibonacci(i));
    printf("\n");
    i = i + 1;
  }
  printf("\n");

  return 0;
}
