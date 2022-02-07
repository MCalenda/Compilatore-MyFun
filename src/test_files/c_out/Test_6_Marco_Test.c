#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

// Funzioni di concatenazione
char *concatInt(char *string, int toConcat) {
int length = snprintf(NULL, 0,"%d", toConcat);
char *converted = (char *) malloc(length + 1);
sprintf(converted, "%d", toConcat);
char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));
strcpy(concat, string);
strcat(concat, converted);
return concat;
}
char *concatReal(char *string, float toConcat) {
int length = snprintf(NULL, 0,"%f", toConcat);
char *converted = (char *) malloc(length + 1);
sprintf(converted, "%f", toConcat);
char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));
strcpy(concat, string);
strcat(concat, converted);
return concat;
}
char *concatBool(char *string, int toConcat) {
char *converted = (char *) malloc(6);
if(toConcat == 1) strcpy(converted, "true");
else strcpy(converted, "false");
char *concat = (char *) malloc(1 + strlen(string)+ strlen(converted));
strcpy(concat, string);
strcat(concat, converted);
return concat;
}
char *concatString(char *string, char *toConcat) {
char *concat = (char *) malloc(1 + strlen(string)+ strlen(toConcat));
strcpy(concat, string);
strcat(concat, toConcat);
return concat;
}


// Dichiarazione delle funzioni
bool isPrimo(int num, int div_cor) {
if(div_cor < num) {
int divisione = num / div_cor;
int rest = num - divisione * div_cor;
if(rest != 0) {
return isPrimo(num, div_cor + 1);

}
else {
return false;
} 
}
else {
return true;
} 
}


// Funzione Main
int main() {
int num;
char *nome_programma = malloc(512 * sizeof(char));
strcpy(nome_programma, "'Calcolo numeri primi'");
printf("%s", concatString( "Benvenuto in ", nome_programma));
printf("\r\n"); printf("%s", "Inserire un numero positivo: ");
scanf("%d", &num);
while(num < 0) {
printf("%s", concatString( concatInt( "Il numero ", num), " non e' positivo, riprova"));
printf("\r\n"); printf("%s", "Inserire numero: ");
scanf("%d", &num);

}
if(num > 1) {
if(isPrimo(num, 2)) {
printf("%d", num);
printf("%s", " e' primo");
printf("\r\n"); 
}
else {
printf("%d", num);
printf("%s", " non e' primo");
printf("\r\n"); } 
}
return 0; 
}
