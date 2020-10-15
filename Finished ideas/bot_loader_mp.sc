__config()->{'scope'->'global','stay_loaded'->true};

global_bot_data={};

//Commented out until bots are able to trigger __on_player events
//
//
//global_tick_counter=0;
//
//__on_tick()->(
//    bots = filter(entity_selector('@a'),_~'player_type'=='fake');
//
//
//    this bit dont work cos no use function for bots
//    
//    if(global_tick_counter%20==0,//if it happens more than twice per second, imma assume it's use continuous
//        global_tick_counter=0;
//        for(filter(bots,global_bot_data:_!=null),//In case they havent right clicked
//            
//            right_clicks = global_bot_data:_:'right_clicks';
//    
//            global_bot_data:player:'continuous' = (right_clicks>1);//If not it's /player x use, not continuous
//        )
//    );
//    
//    global_tick_counter+=1;
//);
//
//Still gotta get this code to work, but it aint gonna trigger, so cant debug
//_register_usage(player)->(
//    print(player);
//    if(player~'player_type'!='fake',return());
//    if(global_bot_data:player==null,_setup_bot_data(player));
//    print(global_bot_data:player)
//    //global_bot_data:player:'right_clicks'+=1
//    
//);

//saving bot data


__on_close()->(
    delete_file('bots','nbt');//clearing previous data
    botmap={};
    bots = filter(entity_selector('@a'),_~'player_type'=='fake');
    for(bots,
        if(global_bot_data:_==null,_setup_bot_data(_))
    );

    print('Bots saved to disk with following data:');

    for(global_bot_data,
        bot=_;
        put(botmap,str(bot),{
            'using'->global_bot_data:_:'continuous',//useless for now
            'x'->_~'x',
            'y'->_~'y',
            'z'->_~'z',
            'dimension'->global_bot_data:_:'dimension'
        });
        print(str('Bot %s saved at position %s%s',
            bot,str(map(bot~'pos',round(_*100)/100)),if(global_bot_data:_:'continuous',', with right-click action','')
        ))
    );

    write_file('bots','nbt',botmap);
);

_read()->(
    values = parse_nbt(read_file('bots','nbt'));

    if(values=='null'||values=={},return(print('No bots to load!')));

    in_dimension(values:_:'dimension',
        for(values,
            run(str('/player %s spawn at %s %s %s',
                _,values:_:'x',values:_:'y',values:_:'z'
            ));
            if(values:_:'using',
                run('/player '+_+' use continuous')
            )
        )
    );
    print('Successfully loaded bots from save')
);

_setup_bot_data(bot)->(//practically useles for the time being
    put(global_bot_data,bot,{
            'right_clicks'->0,//to check no of rightclicks per second
            'continuous'->false,
            'dimension'->(bot~'dimension')
        }
    )
);

//all player right click actions. Eating is more complicated, so I didnt include

//fishing/eating ig?
//__on_player_uses_item(player, item_tuple, hand)->_register_usage(player);

//self explanatory
//__on_player_places_block(player, item_tuple, hand, block)->_register_usage(player);

//changing delay on repeaters? tuning noteblocks?
//__on_player_interacts_with_block(player, hand, block, face, hitvec)->_register_usage(player);

//umm... use it if you want ig
//__on_player_right_clicks_block(player, item_tuple, hand, block, face, hitvec)->_register_usage(player);

//healing golems... idk why u would wanna automate that
//__on_player_interacts_with_entity(player, entity, hand)->_register_usage('player');

_read();//loading pre-existing bot data