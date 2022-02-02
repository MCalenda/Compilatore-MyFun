#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni

// Funzione Main
int main() {
    char *temp_0 = malloc(512 * sizeof(char));
    strcpy(temp_0, "");
    strcat(temp_0, "ciaone");
    strcat(temp_0, " a tutti");
    printf("%s", temp_0);
    printf("\r\n");
    return 0;
}