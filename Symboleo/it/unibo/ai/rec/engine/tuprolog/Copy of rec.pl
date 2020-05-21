:-unknown(_,fail).

ordered_insert([],H,[H]).
ordered_insert([happens(Ev1,T1)|Trace],happens(Ev,T),[happens(Ev,T),happens(Ev1,T1)|Trace]):-
	T=<T1,!.
ordered_insert([H1|Trace],H,[H1|NewTrace]):-
	ordered_insert(Trace,H,NewTrace).
	
order_events(Trace,OTrace):-
	order_events(Trace,[],OTrace).
	
order_events([],OTrace,OTrace).
order_events([H|T],OPTrace,OTrace):-
	ordered_insert(OPTrace,H,OPTraceNew),
	order_events(T,OPTraceNew,OTrace).



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


initiates(start, F, T):-
	initially(F).
	
declip(F,T):-
	happens(E,T),
	initiates(E, F, T),
	\+ holds_at(F, T).

clip(F,T):-
	happens(E,T),
	terminates(E, F, T),
	holds_at(F, T).

start:-
	update([happens(start,-1)]).

update(Trace):-
	order_events(Trace,OTrace),
	update_events(OTrace).

update_events([]).
update_events([H|T]):-
	update_events(H),
	update_events(T).	
	
update_events(happens(E,T)):-
	happens(E,T),!.

update_events(happens(E,T)):-
	future_trace(T,FTrace),
	order_events(FTrace,FTraceO),
	roll_back(T),
	calculate_effects([happens(E,T)|FTraceO]).



happens_after(happens(E,T),Tref):-
	happens(E,T),
	T > Tref.

future_trace(T,FTrace):-
	my_setof(H,happens_after(H,T),FTrace).


roll_back(T):-
	truncate_trace(T),
	remove_future_MVIs(T),
	open_current_MVIs(T).

truncate_trace(T):-
	future_trace(T,FTrace),
	remove_events(FTrace).
	
remove_events([]).
remove_events([H|Trace]):-
	retract(H),
	remove_events(Trace).

	
future_MVI(mholds_for(F,[Ts,Te]),Tref):-
	mholds_for(F,[Ts,Te]),
	gt(Ts,Tref).

remove_future_MVIs(T):-
	my_setof(MVI,future_MVI(MVI,T),MVIs),
	remove_MVIs(MVIs).

remove_MVIs([]).
remove_MVIs([MVI|MVIs]):-
	retract(MVI),
	remove_MVIs(MVIs).

current_MVI(mholds_for(F,[Ts,Te]),Tref):-
	mholds_for(F,[Ts,Te]),
	gt(Tref,Ts),
	le(Tref,Te).

	
open_current_MVIs(T):-
	my_setof(MVI,current_MVI(MVI,T),MVIs),
	open_MVIs(MVIs).

open_MVIs([]).
open_MVIs([mholds_for(F,[Ts,Te])|MVIs]):-
	retract(mholds_for(F,[Ts,Te])),
	assert(mholds_for(F,[Ts,inf])),
	open_MVIs(MVIs).
			


calculate_effects([]).
calculate_effects([H|T]):-
	calculate_effects_event(H),
	calculate_effects(T).

calculate_effects_event(happens(E,T)):-
	assert(happens(E,T)),
	my_setof(F, clip(F,T),S),
	close_mvis(S,T),
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
	
	
manage_concurrency([],[]).
manage_concurrency(L,[H|T]):-
	separate(L,H,TL),
	manage_concurrency(TL,T).
	
separate([happens(E,T)|Tail],HF,TF):-
	separate(Tail,[happens(E,T)],HF,TF).

separate([happens(E2,T)|Tail],[happens(E,T)|Tail2],HF,TF):-
		!,
		separate(Tail,[happens(E2,T),happens(E,T)|Tail2],HF,TF).
	
separate(L,HF,HF,L).



			
