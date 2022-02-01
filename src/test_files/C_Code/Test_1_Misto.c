#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni

// Funzione Main
int main() {
    char *test = malloc(512 * sizeof(char));
    char *test_two = malloc(512 * sizeof(char));
    strcpy(test_two, test);
    teststrcat(test_two, " ancora");
    printf("%s", test);
    printf("\r\n");
    printf("%s", test_two);
    printf("\r\n");
    return 0;
}