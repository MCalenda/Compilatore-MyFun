#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Funzioni di conversione
char *int_to_string(int x) {
    char *str = malloc(512 * sizeof(char));
    sprintf(str, "%d", x);
    return str;
}
char *double_to_string(double x) {
    char *str = malloc(512 * sizeof(char));
    sprintf(str, "%f", x);
    return str;
}

// Dichiarazione delle funzioni
int sommac(int a, int b) { return a + b; }

// Funzione Main
int main() {
    char *str = malloc(512 * sizeof(char));
    strcpy(str, "");
    int x;
    strcat(str, str);
    strcat(str, sommac(5, 4));
    x = sommac(5, 4);
    printf("%s", str);
    printf("\r\n");
    return 0;
}