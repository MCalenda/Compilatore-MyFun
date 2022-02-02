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
            printf("Inserisci primo numero da sommare:");
            scanf("%d", &x);
            printf("Inserisci secondo numero da sommare:");
            scanf("%f", &y);
            result = sum(x, y);
            char *temp_0 = malloc(512 * sizeof(char));
            strcpy(temp_0, "");
            strcat(temp_0, "Il risultato è: ");
            strcat(temp_0, double_to_string(result));
            strcat(temp_0, ".");
            printf("%s", temp_0);
            printf("\r\n");
        }
        if (ans == 2) {
            printf("Inserisci primo numero:");
            scanf("%d", &x);
            printf("Inserisci numero da sottrarre al primo:");
            scanf("%f", &y);
            result = sub(x, y);
            char *temp_1 = malloc(512 * sizeof(char));
            strcpy(temp_1, "");
            strcat(temp_1, "Il risultato è: ");
            strcat(temp_1, double_to_string(result));
            strcat(temp_1, ".");
            printf("%s", temp_1);
            printf("\r\n");
        }
        if (ans == 3) {
            printf("Inserisci primo numero da dividere:");
            scanf("%d", &x);
            printf("Inserisci secondo numero da dividere al primo:");
            scanf("%f", &y);
            result = divid(x, y);
            char *temp_2 = malloc(512 * sizeof(char));
            strcpy(temp_2, "");
            strcat(temp_2, "Il risultato è: ");
            strcat(temp_2, double_to_string(result));
            strcat(temp_2, ".");
            printf("%s", temp_2);
            printf("\r\n");
        }
        if (ans == 4) {
            printf("Inserisci primo numero da moltiplicare:");
            scanf("%d", &x);
            printf("Inserisci secondo numero da moltiplicare:");
            scanf("%f", &y);
            result = mol(x, y);
            char *temp_3 = malloc(512 * sizeof(char));
            strcpy(temp_3, "");
            strcat(temp_3, "Il risultato è: ");
            strcat(temp_3, double_to_string(result));
            strcat(temp_3, ".");
            printf("%s", temp_3);
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