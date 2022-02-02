#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

// Funzioni di conversione
char *int_to_string(int x)
{
    char *str = malloc(512 * sizeof(char));
    sprintf(str, "%d", x);
    return str;
}
char *double_to_string(double x)
{
    char *str = malloc(512 * sizeof(char));
    sprintf(str, "%f", x);
    return str;
}

// Funzione Main
int main()
{
    int x = 5;
    int y = 10;
    int z = x + y;
    printf("%d", z);
    printf("\r\n");
    return 0;
}