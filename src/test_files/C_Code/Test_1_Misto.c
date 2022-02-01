#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni
char *str() { return "ciao"; }

// Funzione Main
int main() {
    char *test_three = malloc(512 * sizeof(char));
    strcpy(test_three, "");
    strcat(test_three, "ciao");
    strcat(test_three, " a tutti");
    strcat(test_three, " ancora");
    strcat(test_three, " per sempre");
    char *test = malloc(512 * sizeof(char));
    strcpy(test, "");
    strcat(test, str());
    strcat(test, "ciao");
    printf("%s", test);
    printf("\r\n");
    return 0;
}