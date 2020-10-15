__config()->{'scope'->'global','stay_loaded'->true};

global_bot_data={};

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

_setup_bot_data(bot)->(
    put(global_bot_data,bot,{
            'dimension'->(bot~'dimension')
        }
    )
);

_read();//loading pre-existing bot data