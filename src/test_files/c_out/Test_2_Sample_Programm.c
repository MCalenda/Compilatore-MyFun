#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Funzioni di concatenazione
char *concatInt(char *string, int toConcat) {
    int length = snprintf(NULL, 0, "%d", toConcat);
    char *converted = (char *)malloc(length + 1);
    sprintf(converted, "%d", toConcat);
    char *concat = (char *)malloc(1 + strlen(string) + strlen(converted));
    strcpy(concat, string);
    strcat(concat, converted);
    return concat;
}
char *concatReal(char *string, float toConcat) {
    int length = snprintf(NULL, 0, "%f", toConcat);
    char *converted = (char *)malloc(length + 1);
    sprintf(converted, "%f", toConcat);
    char *concat = (char *)malloc(1 + strlen(string) + strlen(converted));
    strcpy(concat, string);
    strcat(concat, converted);
    return concat;
}
char *concatBool(char *string, int toConcat) {
    char *converted = (char *)malloc(6);
    if (toConcat == 1)
        strcpy(converted, "true");
    else
        strcpy(converted, "false");
    char *concat = (char *)malloc(1 + strlen(string) + strlen(converted));
    strcpy(concat, string);
    strcat(concat, converted);
    return concat;
}
char *concatString(char *string, char *toConcat) {
    char *concat = (char *)malloc(1 + strlen(string) + strlen(toConcat));
    strcpy(concat, string);
    strcat(concat, toConcat);
    return concat;
}

// Dichiarazione delle variabili locali
int c = 1;

// Dichiarazione delle funzioni
float sommac(int a, float b, char **size) {
    float result;
    result = a + b + c;
    if (result > 100) {
        char *valore = malloc(512 * sizeof(char));
        strcpy(valore, "grande");
        *size = valore;

    } else {
        char *valore = malloc(512 * sizeof(char));
        strcpy(valore, "piccola");
        *size = valore;
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
    char *ans = malloc(512 * sizeof(char));
    strcpy(ans, "no");
    float risultato = sommac(a, b, &taglia);
    printf("%s", "vuoi continuare? (si/no)");
    printf("\t");
    scanf("%s", ans);
    while (strcmp(ans, "si") == 0) {
        printf("inserisci un intero:");
        scanf("%d", &a);
        printf("inserisci un reale:");
        scanf("%f", &b);
        risultato = sommac(a, b, &taglia);
        stampa(concatString(
            concatString(
                concatInt(
                    concatString(
                        concatReal(
                            concatString(concatInt("la somma di ", a), " e "),
                            b),
                        " incrementata di "),
                    c),
                " è "),
            taglia));
        stampa(concatReal(" ed è pari a ", risultato));
        printf("vuoi continuare? (si/no):\t");
        scanf("%s", ans);
    }
    printf("%s", "");
    printf("\r\n");
    printf("%s", "ciao");
    return 0;
}
