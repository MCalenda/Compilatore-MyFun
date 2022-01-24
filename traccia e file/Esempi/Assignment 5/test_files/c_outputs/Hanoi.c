#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
  int n = 0;
  printf("How many pegs? ");
  scanf("%d", &n);
  hanoi(n, 1, 2, 3);

  return 0;
}
