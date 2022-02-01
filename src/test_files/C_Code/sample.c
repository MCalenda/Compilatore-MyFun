#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define space_conc(str1, str2) #str1 #str2

char *str_concat(char *a, char *b) {
    strcat(a, b);
    return a;
}

int main() {
    char *test = str_concat("ciao", "ciao");
    printf("size: %s\n", test);
    return 0;
}