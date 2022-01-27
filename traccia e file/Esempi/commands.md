### Windows
<b>Compile flex</b>: C:/JFLEX/bin/jflex -d src srcjflexcup/toy.flex <br>
<b>Compile CUP</b>: java -jar C:/CUP/java-cup-11b.jar -destdir src srcjflexcup/toy.cup <br>
<b>Create dump file</b>: java -jar C:/CUP/java-cup-11b.jar -dump -destdir src srcjflexcup/toy.cup 2>dump <br>

### MacOS / LINUX
<b>Compile flex</b>: ../jflex-1.8.2/bin/jflex -d src srcjflexcup/toy.flex <br>
<b>Compile CUP</b>: java -jar ../CUP/java-cup-11b.jar -destdir src srcjflexcup/toy.cup <br>
<b>Create dump file</b>: java -jar ../CUP/java-cup-11b.jar -dump -destdir src srcjflexcup/toy.cup 2>dump.txt <br>