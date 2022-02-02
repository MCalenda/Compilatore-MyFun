#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

int main() {
    printf("size: %s \n", int_to_string(45));
    printf("size: %s \n", double_to_string(45.56));
    return 0;
}