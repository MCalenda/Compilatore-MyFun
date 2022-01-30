#include <string.h>
#include <stdio.h>
#include <stdlib.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni

// Funzione Main
int main() {
    char *str1 = malloc(512 * sizeof(char));
    strcpy(str1, "");
    strcat(str1, "sssss");
    strcat(str1, "kkkk");
    strcat(str1, "<<<<");

    printf("%s", str1);
    return 0;
}