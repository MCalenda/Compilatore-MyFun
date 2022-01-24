#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Proc swap
struct struct_swap {
  int variable0;
  int variable1;
};

struct struct_swap swap(int a, int b) {
  // Struttura di ritorno
  struct struct_swap return_swap;
  return_swap.variable0 = b;
  return_swap.variable1 = a;

  return return_swap;
}

// Proc sort
struct struct_sort {
  int variable0;
  int variable1;
  int variable2;
  int variable3;
};

struct struct_sort sort(int a, int b, int c, int d) {
  if (a < b) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return0 = swap(a, b);
    a = swap_return0.variable0;
    b = swap_return0.variable1;
  }
  if (b < c) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return1 = swap(b, c);
    b = swap_return1.variable0;
    c = swap_return1.variable1;
  }
  if (c < d) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return2 = swap(c, d);
    c = swap_return2.variable0;
    d = swap_return2.variable1;
  }
  if (a < b) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return3 = swap(a, b);
    a = swap_return3.variable0;
    b = swap_return3.variable1;
  }
  if (b < c) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return4 = swap(b, c);
    b = swap_return4.variable0;
    c = swap_return4.variable1;
  }
  if (c < d) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return5 = swap(c, d);
    c = swap_return5.variable0;
    d = swap_return5.variable1;
  }
  if (a < b) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return6 = swap(a, b);
    a = swap_return6.variable0;
    b = swap_return6.variable1;
  }
  if (b < c) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return7 = swap(b, c);
    b = swap_return7.variable0;
    c = swap_return7.variable1;
  }
  if (c < d) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return8 = swap(c, d);
    c = swap_return8.variable0;
    d = swap_return8.variable1;
  }
  if (a < b) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return9 = swap(a, b);
    a = swap_return9.variable0;
    b = swap_return9.variable1;
  }
  if (b < c) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return10 = swap(b, c);
    b = swap_return10.variable0;
    c = swap_return10.variable1;
  }
  if (c < d) {
    // Struttura con i valori di ritorno di swap
    struct struct_swap swap_return11 = swap(c, d);
    c = swap_return11.variable0;
    d = swap_return11.variable1;
  }

  // Struttura di ritorno
  struct struct_sort return_sort;
  return_sort.variable0 = d;
  return_sort.variable1 = c;
  return_sort.variable2 = b;
  return_sort.variable3 = a;

  return return_sort;
}

// Proc main
int main(int argc, char* argv[]) {
  int a = atoi(argv[1]);
  int b = atoi(argv[2]);
  int c = atoi(argv[3]);
  int d = atoi(argv[4]);
  printf("I numeri inseriti sono: [");
  printf("%d", a);
  printf(" ");
  printf("%d", b);
  printf(" ");
  printf("%d", c);
  printf(" ");
  printf("%d", d);
  printf("]\n");
  printf("Comincio l'ordinamento...\n");

  // Struttura con i valori di ritorno di sort
  struct struct_sort sort_return12 = sort(a, b, c, d);
  a = sort_return12.variable0;
  b = sort_return12.variable1;
  c = sort_return12.variable2;
  d = sort_return12.variable3;

  printf("Ordinamento completato.\n");
  printf("Result: [");
  printf("%d", a);
  printf(" ");
  printf("%d", b);
  printf(" ");
  printf("%d", c);
  printf(" ");
  printf("%d", d);
  printf("]\n");

  return 0;
}
