//Copied and improved from Firigion's nether portal display
//Don't use with too many poi's near u, game will crash.
//By: Ghoulboy

__config()->{'stay_loaded'->true,'scope'->'global'};

__command()->print(player(),'command');

import('math','_euclidean');

global_markers={};

global_show_radius=40;

global_refresh_rate=5;

global_pois={
    'unemployed'->['white_stained_glass',true],//unused in game
    'armorer'->['blast_furnace',false],
    'butcher'->['smoker',false],
    'cartographer'->['cartography_table',false],
    'cleric'->['brewing_stand',false],
    'farmer'->['composter',false],
    'fisherman'->['barrel',false],
    'fletcher'->['fletching_table',false],
    'leatherworker'->['cauldron',false],
    'librarian'->['lectern',false],
    'mason'->['stonecutter',false],
    'nitwit'->['green_stained_glass',true],//unused in game
    'shepherd'->['loom',false],
    'toolsmith'->['smithing_table',false],
    'weaponsmith'->['grindstone',false],
    'home'->['white_bed',true],
    'meeting'->['bell',true],
    'beehive'->['beehive',true],
    'bee_nest'->['bee_nest',true],
    'nether_portal'->['obsidian',true]
};

global_incorrect_poi_type='unemployed, armorer, butcher, cartographer, cleric, farmer, fisherman, fletcher, leatherworker, librarian, mason, nitwit, shepherd, toolsmith, weaponsmith, home, meeting, beehive, bee_nest, nether_portal';

__on_tick()->__do_on_tick();

__holds_eye(player)->return(
    query(player, 'holds', 'mainhand'):0 == 'ender_eye'||
    query(player, 'holds', 'offhand'):0 == 'ender_eye'
);

__do_on_tick()->(
    if(tick_time()%global_refresh_rate==0,
        if(global_markers,__remove_markers());
        game_tick(50);
        for(['nether','overworld'],
            in_dimension(_,
                dim=_;
                players_list = filter(player('*'), __holds_eye(_));

                for(players_list,
                    __update_markers(_,dim)
                )
            )
        )
    )
);

__update_markers(player,dim)->(
    in_dimension(dim,
        for(poi(pos(player),global_show_radius),
            poi=_:0;
            pos=_:2+[0.5,0.5,0.5];
            if(!global_markers:pos && global_pois:poi:1,
                m=create_marker('',pos, global_pois:poi:0,false);
                modify(m,'nbt_merge','{Glowing:1b, Fire:32767s, Marker:1b,Tags:["poi_show"]}');
                global_markers:pos=[m,poi]
            )
        )
    )
);

__remove_markers()->(
    for(global_markers,
        closest_player=player();
        [m,poi]=global_markers:_;
        if(!closest_player ||
            _euclidean(pos(m),pos(closest_player))>global_show_radius ||
            !__holds_eye(closest_player)||
            poi(pos(m)-[0.5,0.5,0.5]):0!=poi,
            delete(global_markers,global_markers~_);
            modify(m,'remove');
        )
    );
);

set_refresh_rate(rate)->global_refresh_rate=rate;

set_view_range(range)->global_show_radius=range;

toggle_poi_display(type)->(
    if(global_pois:type,
        global_pois:type:1=!global_pois:type:1,
        return(print(player(),str('Invalid poi type %s, must be one of:\n %s',type,global_incorrect_poi_type)))
    )
);

set_new_poi(type)->
    if(global_pois:type,
        set_poi( pos(query(player(), 'trace', 5, 'blocks')) , type),
        print(player(),str('Invalid poi type %s, must be one of:\n %s',type,global_incorrect_poi_type));
        return()
    );

remove_poi() -> set_poi( pos(query(player(), 'trace', 5, 'blocks')) , null);

remove_markers()->run('kill @e[tag=poi_show]')