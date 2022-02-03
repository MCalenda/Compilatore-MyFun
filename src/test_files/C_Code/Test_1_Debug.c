#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

// Funzioni di concatenazione
char *concatInt(char *string, int toConcat) {int length = snprintf(NULL, 0,"%d", toConcat);char *converted = (char *) malloc(length + 1);sprintf(converted, "%d", toConcat);char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));strcpy(concat, string);strcat(concat, converted);return concat;}char *concatReal(char *string, float toConcat) {int length = snprintf(NULL, 0,"%f", toConcat);char *converted = (char *) malloc(length + 1);sprintf(converted, "%f", toConcat);char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));strcpy(concat, string);strcat(concat, converted);return concat;}char *concatBool(char *string, int toConcat) {char *converted = (char *) malloc(6);if(toConcat == 1) strcpy(converted, "true");else strcpy(converted, "false");char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));strcpy(concat, string);strcat(concat, converted);return concat;}char *concatString(char *string, char *toConcat) {char *concat = (char *) malloc(1 + strlen(string)+ strlen(toConcat));strcpy(concat, string);strcat(concat, toConcat);return concat;}

// Dichiarazione delle funzioni
void prova(char **a, int *b, int c, char *d) {*a = concatString( *a, " e' cambiato");*b = 6;c = 7;d = concatString( d, " o forse si");}

// Funzione Main
int main() {char *a = "questa";int b = 5;int c = 6;char *d = "codesta non e' cambiata";prova(&a, &b, c, d);printf("%s", concatString( concatString( concatInt( concatString( concatInt( concatString( concatString( a, " "), " "), b), " "), c), " "), d));printf("\r\n"); return 0; }