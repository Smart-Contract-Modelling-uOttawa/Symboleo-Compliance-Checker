:-unknown(_,fail).

lt(A,B):-gt(B,A).
le(A,B):-ge(B,A).

gt(inf,B):- B\==inf,!.
gt(_,inf):-!,fail.
gt(A,B):- A>B.

ge(A,A):-!.
ge(A,B):-gt(A,B).

holds_at(F, T):-
	holds_from(F, T, _).
	
holds_from(F, T, T1):-
	mholds_for(F, [T1, T2]),
	gt(T, T1),
	le(T, T2).


initiates(start, F, 0):-
	initially(F).
	
declip(F,T):-
	happens(E,T),
	initiates(E, F, T),
	\+ holds_at(F, T).

clip(F,T):-
	happens(E,T),
	terminates(E, F, T),
	holds_at(F, T).

update([]).
update([H|T]):-
	update(H),
	update(T).	
	
update(happens(E,T)):-
	happens(E,T),!.

update(happens(E,T)):-
	assert(happens(E,T)),
	assert(happens(current_time,T)),
	%findall(F,clip(F,T),L),
	%list_to_set(L,S),
	my_setof(F, clip(F,T),S),
	close_mvis(S,T),
	%findall(F,declip(F,T),L2),
	%list_to_set(L2,S2),
	my_setof(F, declip(F,T),S2),
	open_mvis(S2,T).

my_setof(A,B,C):-
	setof(A,B,C),!.
my_setof(_,_,[]).

close_mvis([],_).
close_mvis([F|Fs],T):-
	close_mvi(F,T),
	close_mvis(Fs,T).
	
close_mvi(F,T):-
	holds_from(F,T,T1),
	retract(mholds_for(F,[T1,inf])),
	assert(mholds_for(F,[T1,T])).

open_mvis([],_).
open_mvis([F|Fs],T):-
	open_mvi(F,T),
	open_mvis(Fs,T).

open_mvi(F,T):-
	assert(mholds_for(F,[T,inf])).


status(MVIs):-
	findall(mholds_for(F,[T1,T2]),mholds_for(F,[T1,T2]),MVIs).


reset:-
	retractall(happens(_,_)),
	retractall(mholds_for(_,_)).
	
	
list_to_set([],[]).
list_to_set([H|T],Set):-
	member(H,T),!,
	list_to_set(T,Set).	
list_to_set([H|T],[H|Set]):-
	list_to_set(T,Set).	
	
