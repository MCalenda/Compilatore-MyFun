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

// Dichiarazione delle variabili globali
bool lp = true;

// Dichiarazione delle funzioni
void menu() {
    printf("%s",
           "Benvenuto in questo programma, quale operazione vuoi svolgere ?");
    printf("\r\n");
    printf("%s", "1: Somma");
    printf("\r\n");
    printf("%s", "2: Sottrazione");
    printf("\r\n");
    printf("%s", "3: Divisione");
    printf("\r\n");
    printf("%s", "4: Moltiplicazione");
    printf("\r\n");
    printf("%s", "5: Exit");
    printf("\r\n");
}
float sum(int a, float b) {
    float result;
    result = a + b;
    return result;
}
float sub(int a, float b) {
    float result;
    result = a - b;
    return result;
}
float divid(int a, float b) {
    float result;
    result = a / b;
    return result;
}
float mol(int a, float b) {
    float result;
    result = a * b;
    return result;
}

// Funzione Main
int main() {
    int x;
    float y;
    int ans = 0;
    float result;
    while (lp == true) {
        menu();
        printf("%s", "Fai la tua scelta:");
        printf("\t");
        scanf("%d", &ans);
        if (ans == 1) {
            printf("%s", "Inserisci primo numero da sommare:");
            scanf("%d", &x);
            printf("%s", "Inserisci secondo numero da sommare:");
            scanf("%f", &y);
            result = sum(x, y);
            printf("%s",
                   concatString(concatReal("Il risultato è: ", result), "."));
            printf("\r\n");
        }
        if (ans == 2) {
            printf("%s", "Inserisci primo numero:");
            scanf("%d", &x);
            printf("%s", "Inserisci numero da sottrarre al primo:");
            scanf("%f", &y);
            result = sub(x, y);
            printf("%s",
                   concatString(concatReal("Il risultato è: ", result), "."));
            printf("\r\n");
        }
        if (ans == 3) {
            printf("%s", "Inserisci primo numero da dividere:");
            scanf("%d", &x);
            printf("%s", "Inserisci secondo numero da dividere al primo:");
            scanf("%f", &y);
            result = divid(x, y);
            printf("%s",
                   concatString(concatReal("Il risultato è: ", result), "."));
            printf("\r\n");
        }
        if (ans == 4) {
            printf("%s", "Inserisci primo numero da moltiplicare:");
            scanf("%d", &x);
            printf("%s", "Inserisci secondo numero da moltiplicare:");
            scanf("%f", &y);
            result = mol(x, y);
            printf("%s",
                   concatString(concatReal("Il risultato è: ", result), "."));
            printf("\r\n");
        }
        if (ans == 5) {
            lp = false;
        }
    }
    printf("%s", "");
    printf("\r\n");
    printf("%s", "Grazie di aver utilizzato questo programma :>");
    return 0;
}
