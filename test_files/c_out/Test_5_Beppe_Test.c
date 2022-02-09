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


// Funzione Main
int main() {
int n;
int value;
int weight;
float valueSum = 0;
int weightSum = 0;
printf("%s", "Quanti valori vuoi inserire ? ");
scanf("%d", &n);
while(n > 0) {
printf("%s", concatString( concatInt( "Inserisci il valore ", n), ": "));
scanf("%d", &value);
printf("%s", concatString( concatInt( "Inserisci il peso ", n), ": "));
scanf("%d", &weight);
valueSum = valueSum + value * weight;
weightSum = weightSum + weight;
n = n - 1;

}
printf("%s", "\n");
printf("%s", concatReal( "Media pesata: ", valueSum / weightSum));
return 0; 
}
