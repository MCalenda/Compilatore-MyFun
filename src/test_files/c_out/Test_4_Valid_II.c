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

// Dichiarazione delle funzioni
int print_menu() {
    int choose;
    printf("%s", "Scegli l'operazione da svolgere per continuare");
    printf("\r\n");
    printf("%s", "\t(1) Somma di due numeri");
    printf("\r\n");
    printf("%s", "\t(2) Moltiplicazione di due numeri");
    printf("\r\n");
    printf("%s", "\t(3) Divisione intera fra due numeri positivi");
    printf("\r\n");
    printf("%s", "\t(4) Elevamento a potenza");
    printf("\r\n");
    printf("%s", "\t(5) Successione di Fibonacci (ricorsiva)");
    printf("\r\n");
    printf("%s", "\t(6) Successione di Fibonacci (iterativa)");
    printf("\r\n");
    printf("%s", "\t(0) Esci");
    printf("\r\n");
    printf("--> ");
    scanf("%d", &choose);
    return choose;
}
void do_sum() {
    float op1;
    float op2;
    printf("%s", "\n(1) SOMMA\n");
    printf("\r\n");
    printf("Inserisci il primo operando: ");
    scanf("%f", &op1);
    printf("Inserisci il secondo operando: ");
    scanf("%f", &op2);
    printf("%s", "");
    printf("\r\n");
    printf(
        "%s",
        concatReal(
            concatString(
                concatReal(
                    concatString(concatReal("La somma tra ", op1), " e "), op2),
                " vale "),
            op1 + op2));
    printf("\r\n");
}
void do_mul() {
    float op1;
    float op2;
    printf("%s", "\n(2) MOLTIPLICAZIONE");
    printf("\r\n");
    printf("\nInserisci il primo operando: ");
    scanf("%f", &op1);
    printf("Inserisci il secondo operando: ");
    scanf("%f", &op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s",
           concatReal(
               concatString(
                   concatReal(
                       concatString(concatReal("La moltiplicazione tra ", op1),
                                    " e "),
                       op2),
                   " vale "),
               op1 * op2));
    printf("\r\n");
}
void do_div_int() {
    int op1;
    int op2;
    printf("%s", "\n(3) DIVISIONE INTERA");
    printf("\r\n");
    printf("\nInserisci il primo operando: ");
    scanf("%d", &op1);
    printf("Inserisci il secondo operando: ");
    scanf("%d", &op2);
    printf("%s", "");
    printf("\r\n");
    printf(
        "%s",
        concatInt(concatString(
                      concatInt(concatString(
                                    concatInt("La divisione intera tra ", op1),
                                    " e "),
                                op2),
                      " vale "),
                  op1 / op2));
    printf("\r\n");
}
void do_pow() {
    float op1;
    float op2;
    printf("%s", "\n(4) POTENZA");
    printf("\r\n");
    printf("\nInserisci la base: ");
    scanf("%f", &op1);
    printf("Inserisci l'esponente: ");
    scanf("%f", &op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s",
           concatReal(
               concatString(
                   concatReal(concatString(concatReal("La potenza di ", op1),
                                           " elevato a "),
                              op2),
                   " vale "),
               pow(op1, op2)));
    printf("\r\n");
}
int recursive_fib(int n) {
    if (n == 1) {
        return 0;
    }
    if (n == 2) {
        return 1;
    }
    return recursive_fib(n - 1) + recursive_fib(n - 2);
}
int iterative_fib(int n) {
    if (n == 1) {
        return 0;
    }
    if (n == 2) {
        return 1;
    }
    if (n > 2) {
        int i = 3;
        int res = 1;
        int prev = 0;
        while (i <= n) {
            int tmp = res;
            res = res + prev;
            prev = tmp;
            i = i + 1;
        }
        return res;
    }
    return -1;
}
void do_fib(bool recursive) {
    int n;
    char *message = malloc(512 * sizeof(char));
    printf("%s", "\n(5) FIBONACCI");
    printf("\r\n");
    printf("\nInserisci n: ");
    scanf("%d", &n);
    printf("%s", "");
    printf("\r\n");
    message = concatString(concatInt("Il numero di Fibonacci in posizione ", n),
                           " vale ");
    if (recursive) {
        message = concatInt(message, recursive_fib(n));

    } else {
        message = concatInt(message, iterative_fib(n));
    }
    printf("%s", message);
    printf("\r\n");
}
void do_operation(int choose) {
    if (choose == 1) {
        do_sum();

    } else {
        if (choose == 2) {
            do_mul();

        } else {
            if (choose == 3) {
                do_div_int();

            } else {
                if (choose == 4) {
                    do_pow();

                } else {
                    if (choose == 5) {
                        do_fib(true);

                    } else {
                        if (choose == 6) {
                            do_fib(false);
                        }
                    }
                }
            }
        }
    }
}
void print_continue(bool *conti) {
    char *in = malloc(512 * sizeof(char));
    printf("Vuoi continuare? (s/n) --> ");
    scanf("%s", in);
    if (strcmp(in, "s") == 0) {
        *conti = true;

    } else {
        *conti = false;
    }
}

// Funzione Main
int main() {
    int choose = 0;
    bool conti = true;
    while (conti) {
        choose = print_menu();
        if (choose == 0) {
            conti = false;

        } else {
            do_operation(choose);
            print_continue(&conti);
        }
    }
    return 0;
}
