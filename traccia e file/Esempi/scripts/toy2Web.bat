::Comando che permette di spostarsi in una specifica current directory
cd "C:\Users\cerro\emsdk"
::Comando che esegue in background il file dato come argomento
call "emsdk_env.bat"
::Comando che permette di spostarsi in una specifica current directory
cd "C:\Users\cerro\IdeaProjects\tesauro-cerrone_es5_scg"
::Comando che esegue in background il comando emcc che dato un file C dato in input
::viene convertito in un file HTML "out.html"
call emcc %1 -o test_files\html_outputs_windows\out.html
::Comando che esegue in background il comando emrun che fa partire il server
::e rimanda l'utente sul browser eseguendo il file HTML "out.html"
call emrun test_files\html_outputs_windows\out.html
PAUSE