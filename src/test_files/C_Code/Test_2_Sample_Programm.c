#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Dichiarazione delle variabili locali
int c = 1;

// Dichiarazione delle funzioni
doublesommac(int a, double b, char *size) {	double result; 	result = a + b + c;	if (result > 100) {char valore[512] = "grande";*size = valore;}else {char valore[512] = "piccola";*size = valore;} 	return result; }void stampa(char *messaggio) {	int i = 1;	while (i <= 4) {int incremento = 1;printf("%s", ""); printf("\r\n"); i = i + incremento;}	printf("%s", *messaggio); printf("\r\n"); }

// Funzione Main
int main() {int a = 1;double b = 2.2;char *taglia= malloc(512 * sizeof(char));strcpy(taglia, "");char ans[512] = "no";double risultato = sommac(a, b, taglia); printf("%s", "vuoi continuare? (si/no)"); printf("\t"); scanf("%s", ans);while (strcmp(ans, "si") == 1) {printf("inserisci un intero:");scanf("%d", a);printf("inserisci un reale:");scanf("%f", b);risultato = sommac(a, b, taglia)stampa("la somma di "a" e "b" incrementata di "c" è "taglia)stampa(" ed è pari a "risultato)printf("vuoi continuare? (si/no):\t");scanf("%s", ans);}printf("%s", ""); printf("\r\n"); printf("%s", "ciao"); return 0; }