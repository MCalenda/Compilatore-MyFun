# Creo lo script per:
# - activate: portare il terminal in foreground
# - source path/to/emsdk_env.sh: settare l'ambiente (altrimenti emcc e emrun non funzionerebbero)
# - emcc source.c -o output.js: creare l'output .js
# - node file.js: fa partire l'esecuzione
osascript -e "
    tell application \"Terminal\"
        activate
        do script \"source /Users/emmanueltesauro/Desktop/emsdk/emsdk_env.sh\" in window 1
        do script \"cd /Users/emmanueltesauro/IdeaProjects/tesauro-cerrone_es5_scg/\" in window 1
        do script \"emcc $1 -o test_files/html_outputs/out.js\" in window 1
        do script \"node test_files/html_outputs/out.js\" in window 1
    end tell"
