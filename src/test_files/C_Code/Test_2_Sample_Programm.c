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
// Dichiarazione delle variabili locali
int c = 1;

// Dichiarazione delle funzioni
float sommac(int a, float b, char *size) {
    float result;
    result = a + b + c;
    if (result > 100) {
        char *valore = malloc(512 * sizeof(char));
        strcpy(valore, "grande");
        size = valore;
    } else {
        char *valore = malloc(512 * sizeof(char));
        strcpy(valore, "piccola");
        size = valore;
    }
    return result;
}
void stampa(char *messaggio) {
    int i = 1;
    while (i <= 4) {
        int incremento = 1;
        printf("%s", "");
        printf("\r\n");
        i = i + incremento;
    }
    printf("%s", messaggio);
    printf("\r\n");
}

// Funzione Main
int main() {
    int a = 1;
    float b = 2.2;
    char *taglia = malloc(512 * sizeof(char));
    strcpy(taglia, "");
    char *ans = malloc(512 * sizeof(char));
    strcpy(ans, "no");
    float risultato = sommac(a, b, taglia);
    printf("%s", "vuoi continuare? (si/no)");
    printf("\t");
    scanf("%s", ans);
    while (strcmp(ans, "si") == 0) {
        printf("inserisci un intero:");
        scanf("%d", &a);
        printf("inserisci un reale:");
        scanf("%f", &b);
        risultato = sommac(a, b, taglia);
        char *temp_0 = malloc(512 * sizeof(char));
        strcpy(temp_0, "");
        strcat(temp_0, "la somma di ");
        strcat(temp_0, int_to_string(a));
        strcat(temp_0, " e ");
        strcat(temp_0, double_to_string(b));
        strcat(temp_0, " incrementata di ");
        strcat(temp_0, int_to_string(c));
        strcat(temp_0, " è ");
        strcat(temp_0, taglia);
        stampa(temp_0);
        char *temp_1 = malloc(512 * sizeof(char));
        strcpy(temp_1, "");
        strcat(temp_1, " ed è pari a ");
        strcat(temp_1, double_to_string(risultato));
        stampa(temp_1);
        printf("vuoi continuare? (si/no):\t");
        scanf("%s", ans);
    }
    printf("%s", "");
    printf("\r\n");
    printf("%s", "ciao");
    return 0;
}