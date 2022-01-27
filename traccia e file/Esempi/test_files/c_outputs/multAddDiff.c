#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione variabili globali
char nome[512] = "Michele";

// Proc multAddDiff
struct struct_multAddDiff {
  int variable0;
  int variable1;
  int variable2;
  char variable3[512];
};

struct struct_multAddDiff multAddDiff() {
  int primo;
  int secondo;
  int mul;
  int add;
  int diff;
  printf("Inserire il primo argomento:");
  scanf("%d", &primo);
  printf("Inserire il secondo argomento:");
  scanf("%d", &secondo);
  mul = primo * secondo;
  add = primo + secondo;
  diff = primo - secondo;

  // Struttura di ritorno
  struct struct_multAddDiff return_multAddDiff;
  return_multAddDiff.variable0 = mul;
  return_multAddDiff.variable1 = add;
  return_multAddDiff.variable2 = diff;
  strcpy(return_multAddDiff.variable3, nome);
  return return_multAddDiff;
}

// Proc writeNewLines
void writeNewLines(int n) {
  while (n > 0) {
    printf("\n");
    n = n - 1;
  }
}

// Proc main
int main() {
  int a;
  int b;
  int c = 0;
  char asd[512] = "asd";

  // Struttura con i valori di ritorno di multAddDiff
  struct struct_multAddDiff multAddDiff_return0 = multAddDiff();
  a = multAddDiff_return0.variable0;
  b = multAddDiff_return0.variable1;
  c = multAddDiff_return0.variable2;
  strcpy(asd, multAddDiff_return0.variable3);

  printf("Ciao ");
  printf("%s", nome);
  writeNewLines(2);
  printf("I tuoi valori sono:\n");
  printf("%d", a);
  printf(" per la moltiplicazione\n");
  printf("%d", b);
  printf(" per la somma,\n");
  printf("%d", c);
  printf(" per la differenza\n");

  return 0;
}
