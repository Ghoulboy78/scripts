//Tick command in scarpet
//By: Ghoulboy & gnembon
//I could only implement so many functions in scarpet. Soz.

__command()->print('Tick command.');

//Global Variables

global_tick_rate=20;

global_tick_warping_player=null;

//Command functions

warp(ticks)->(//Seems complex at first, then seems easy, but is really hard if you wanna do it quick
    if(!__check_type(ticks,'number'),return('Invalid argument type'));//Little extra something
    if(global_tick_warping_player,
        if(ticks,
            print('Player '+global_tick_warping_player+' is already advancing time at the moment. Try later or ask them.')
        ),
        print('Warp speed...');
        global_tick_warping_player=player();
        global_tick_rate=0//Stops the previous rate command from interfering and messing this up
    );
    total_ms=0;
    loop(ticks,
        if(global_tick_warping_player,total_ms+=profile_expr(game_tick()),break())
    );
    avg_ms=total_ms/ticks;
    global_tick_warping_player=null;
    print('Time warp successfully completed with '+(if(1000/avg_ms==NaN,0,1000/avg_ms))+' tps, or '+avg_ms+' mspt')
);

//Other functions

__check_type(arg,type)->return(type(arg)==type);