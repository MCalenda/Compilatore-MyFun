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

// Funzione Main
int main() {
    int x;
    char *y = malloc(512 * sizeof(char));
    scanf("%d", &x);
    scanf("%s", y);
    printf("%d", x);
    printf("%s", y);
    return 0;
}
