#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione variabili globali
int n = 0;

// Proc factorial
int factorial(int n) {
  int result = 0;
  float x = 1.0;
  if (n == 0) {
    result = 1;
  } else if (n == 0) {
    result = 3;
  } else {
    result = n * factorial(n - 1);
  }

  return result;
}

// Proc main
int main(int argc, char* argv[]) {
  int x = atoi(argv[1]);
  char y[512];
  strcpy(y, argv[2]);
  float j = atof(argv[3]);
  printf("%d", x);
  printf("\n");
  printf("%s", y);
  printf("\n");
  printf("%f", j);
  printf("\n");
  printf("Enter n, or >= 10 to exit: ");
  scanf("%d", &n);
  while (n < 10) {
    printf("Factorial of ");
    printf("%d", n);
    printf(" is ");
    printf("%d", factorial(n));
    printf("\n");
    printf("Enter n, or >= 10 to exit: ");
    scanf("%d", &n);
  }

  return 0;
}
