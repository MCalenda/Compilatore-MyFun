#include <string.h>

// Dichiarazione delle variabili locali

// Dichiarazione delle funzioni

// Funzione Main
int main() {
    char temp1[512] = "ciao";
    char temp2[512] = "a tutti";
    char *zx = strcat(temp1, temp2);
    printf("%s", zx);
    printf("\r\n");
    return 0;
}