#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Dichiarazione delle variabili locali

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
    scanf("%d", choose);
    return choose;
}
void do_sum() {
    double op1;
    double op2;
    printf("%s", "\n(1) SOMMA\n");
    printf("\r\n");
    printf("Inserisci il primo operando: ");
    scanf("%f", op1);
    printf("Inserisci il secondo operando: ");
    scanf("%f", op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s", "La somma tra " op1 " e " op2 " vale " op1 + op2);
    printf("\r\n");
}
void do_mul() {
    double op1;
    double op2;
    printf("%s", "\n(2) MOLTIPLICAZIONE");
    printf("\r\n");
    printf("\nInserisci il primo operando: ");
    scanf("%f", op1);
    printf("Inserisci il secondo operando: ");
    scanf("%f", op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s", "La moltiplicazione tra " op1 " e " op2 " vale " op1 * op2);
    printf("\r\n");
}
void do_div_int() {
    int op1;
    int op2;
    printf("%s", "\n(3) DIVISIONE INTERA");
    printf("\r\n");
    printf("\nInserisci il primo operando: ");
    scanf("%d", op1);
    printf("Inserisci il secondo operando: ");
    scanf("%d", op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s", "La divisione intera tra " op1 " e " op2 " vale " op1 / op2);
    printf("\r\n");
}
void do_pow() {
    double op1;
    double op2;
    printf("%s", "\n(4) POTENZA");
    printf("\r\n");
    printf("\nInserisci la base: ");
    scanf("%f", op1);
    printf("Inserisci l'esponente: ");
    scanf("%f", op2);
    printf("%s", "");
    printf("\r\n");
    printf("%s", "La potenza di " op1 " elevato a " op2 " vale " pow(op1, op2));
    printf("\r\n");
}
int recursive_fib(int n) {
    if (n == 1) {
        return 0;
    }
    if (n == 2) {
        return 1;
    }
    return recursive_fib(n - 1);
    +recursive_fib(n - 2);
    ;
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
    scanf("%d", n);
    printf("%s", "");
    printf("\r\n");
    message = "Il numero di Fibonacci in posizione " n " vale ";
    if (recursive) {
        message = messagerecursive_fib(n);
        ;
    } else {
        message = messageiterative_fib(n);
        ;
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
    if (strcmp(in, "s") == 1) {
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