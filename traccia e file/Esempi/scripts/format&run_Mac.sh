#! /bin/bash

# Formatto
clang-format --style=google -i $1

# Compilo l'input nel file di output 'main'
clang -o main $1

# Sposto il file .c om test_files/c_outputs/
mv $1 $2

# Array di parametri in input (ciao,1,2,test,...) che splitto su ogni ','
arrIN=(${3//,/ })

# Costruisco la stringa './main arg1 arg2 arg3...'
mainString="./main"
for i in ${arrIN[@]}; do
  mainString="${mainString} $i"
done

# Lancio l'eseguibile 'main' in un nuovo terminale in foreground (comando 'activate' -> foreground)
osascript -e "
    tell application \"Terminal\"
        activate
        do script \"cd $2\" in window 1
        do script \"../../${mainString}\" in window 1
    end tell"
