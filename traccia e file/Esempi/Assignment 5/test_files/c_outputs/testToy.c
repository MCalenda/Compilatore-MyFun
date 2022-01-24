#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc calc
float calc(int scelta, float a, float b) {
  float result = 0.0;
  if (scelta == 1) {
    result = a + b;
  } else if (scelta == 2) {
    result = a - b;
  } else if (scelta == 3) {
    result = a * b;
  } else if (scelta == 4) {
    if (b != 0) {
      result = a / b;
    } else {
      printf("Impossibile\n");
      result = 0.0;
    }
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

// Proc exp
int exp(int x, int y) {
  int z = 1;
  int i = 1;
  while (i <= y) {
    z = z * x;
    i = i + 1;
  }

  return z;
}

// Proc main
int main() {
  float a = 0.0;
  float b = 0.0;
  int i = 0;
  int scelta;
  printf("MENU\nSeleziona una operazione:\n");
  printf("1) Somma\n");
  printf("2) Sottrazione\n");
  printf("3) Motiplicazione\n");
  printf("4) Divisione\n");
  printf("5) Elevamento a potenza\n");
  printf("6) Successione di Fibonacci\n");
  printf("\nScelta:");
  scanf("%d", &scelta);
  while (scelta > 0 && scelta < 7) {
    if (scelta > 0 && scelta < 5) {
      printf("Inserire il primo argomento:\n");
      scanf("%f", &a);
      printf("Inserire il secondo argomento:\n");
      scanf("%f", &b);
      printf("Risultato: ");
      printf("%f", calc(scelta, a, b));
      printf("\n\n");
    } else if (scelta == 5) {
      printf("Inserire il primo argomento:\n");
      scanf("%f", &a);
      printf("Inserire il secondo argomento:\n");
      scanf("%f", &b);
      printf("Risultato: ");
      printf("%d", exp(a, b));
      printf("\n\n");
    } else if (scelta == 6) {
      printf("Inserire il numero:\n");
      scanf("%f", &a);
      while (i <= a) {
        printf("numero : ");
        printf("%d", i);
        printf(". Valore fibonacci: ");
        printf("%d", fibonacci(i));
        printf("\n");
        i = i + 1;
      }
      printf("\n");
    }
    printf("MENU\nSeleziona una operazione:\n");
    printf("1) Somma\n");
    printf("2) Sottrazione\n");
    printf("3) Motiplicazione\n");
    printf("4) Divisione\n");
    printf("5) Elevamento a potenza\n");
    printf("6) Successione di Fibonacci\n");
    printf("\nScelta:");
    scanf("%d", &scelta);
  }

  return 0;
}
