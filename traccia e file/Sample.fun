#*
 Programma esemplificativo del linguaggio MyFun a volte volutamente ridondante. 

 Questo programma calcola e stampa la somma di due numeri, incrementata di 1.
 Inoltre la definisce 'grande' se è superiore a 100 altrimenti 'piccola'

#

var c := 1;


# fa somma e restituisce anche la taglia del numero, oltre che il risultato
fun sommac(integer a, real b, out string size): real

	real result;
	
	result  :=  a + b + c;

	if result > 100 then
		var valore := 'grande';
 		size := valore;
	else 	
		var valore := 'piccola';
 		size := valore;
	end if;

	return result;

end fun;

# stampa il messaggio dopo 4 ritorni a capo
fun stampa(string messaggio)

	var i := 1;
	while i <= 4 loop
		var incremento := 1;
		?. '';
		i := i + incremento;
	end loop;

	?. messaggio;

end fun;

#programma main
main
	var a := 1, b := 2.2;
	string taglia;
	var ans := 'no';
	real risultato := sommac(a, b, @taglia);

	stampa('la somma di ' & a & ' e ' & b & ' incrementata di ' & c & ' è ' & taglia);
	stampa('ed è pari a ' & risultato);

	?: 'vuoi continuare? (si/no)';
	% ans;
	while ans = 'si' loop
		% a 'inserisci un intero:';
		% b 'inserisci un reale:';
		risultato := sommac(a, b, @taglia);
		stampa('la somma di ' & a & ' e ' & b &  ' incrementata di ' & c & ' è ' & taglia);
		stampa(' ed è pari a ' & risultato);
		% ans 'vuoi continuare? (si/no):\t';
	end loop;
	
	?. '';
	? 'ciao';

end main;

****** OUTPUT ATTESO ********




la somma di 1 e 2.2 incrementata di 1 è piccola
ed è pari a 3.2
vuoi continuare? (si/no)	si
inserisci un intero: 50
inserisci un reale: 60




la somma di 50 e 60.0 incrementata di 1 è grande
ed è pari a 111.0
vuoi continuare? (si/no)	no

ciao