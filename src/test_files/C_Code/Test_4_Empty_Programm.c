#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni

// Funzione Main
int main() {
    char *test = malloc(512 * sizeof(char));
    strcpy(test, "");
    strcat(test, "ciao");
    strcat(test, "cacscas");
    strcat(test, "adsadasdas");
    char *cia = malloc(512 * sizeof(char));
    printf("%s", cia);
    printf("\r\n");
    printf("%s", test);
    printf("\r\n");
    printf("%s", "ciaone");
    printf("\r\n");
    return 0;
}