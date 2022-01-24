#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione variabili globali
int x;
int y = 3 + 3;
float p;
char d[512] = "asd";
bool a;

// Proc gigiox
bool gigiox() { return true; }

// Proc funzione
struct struct_funzione {
  bool variable0;
  float variable1;
};

struct struct_funzione funzione(int x, int y, char* op) {
  int result = 0;
  if (strcmp(op, "+") == 0) {
    result = x + y;
  } else {
    result = x - y;
  }

  // Struttura di ritorno
  struct struct_funzione return_funzione;
  return_funzione.variable0 = true;
  return_funzione.variable1 = result;

  return return_funzione;
}

// Proc niam
void niam() {
  char polla[512];
  bool we;
  bool po;
  bool na = false;
  char lel[512] = "asd";
  if (!we) {
    we = true;
  }
  strcpy(polla, "abcdefg");
  we = true;
  printf("%s", polla);
  printf("\n");
  if (!we && !po && strcmp(polla, "provaaaaa") == 0) {
    while (strcmp(polla, "121212") >= 0) {
      strcpy(polla, "9895451");
      printf("Sono dentro ad un while");
      printf("%s", polla);
      scanf("%s", lel);
      scanf("%d", &we);
    }
  } else if (NULL) {
    we = po;
  } else {
    while (strcmp(polla, "test") == 0) {
      printf("%d", na);
      printf("aaaaaaa");
      scanf("%d", &po);
      scanf("%d", &na);
      printf("%d", po);
      printf("%d", na);
      printf("%d", we);
    }
  }
}

// Proc main
int main() {
  bool boolean;
  float y;
  int a = 12;
  int l = 3;
  char x[512] = "asd";
  a = 13;
  niam();

  // Struttura con i valori di ritorno di funzione
  struct struct_funzione funzione_return0 = funzione(a, l, x);
  boolean = funzione_return0.variable0;
  y = funzione_return0.variable1;

  if (true) {
    while (false) {
      scanf("%d", &l);
      scanf("%f", &p);
      printf("%s", x);
    }
  } else {
    gigiox();
  }

  return 0;
}
