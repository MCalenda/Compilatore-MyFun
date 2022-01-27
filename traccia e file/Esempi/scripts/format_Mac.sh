#! /bin/bash

# Formatto
clang-format --style=google -i $1

# Compilo l'input nel file di output 'main'
clang -o main $1

# Sposto il file .c om test_files/c_outputs/
mv $1 $2