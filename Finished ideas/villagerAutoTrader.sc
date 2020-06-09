//For gnembon
//By: Ghoulboy

__config()->m(l('scope','global'));

global_trade_speed=120;//Trade every 120 ticks, so u have time to retrieve item. Same as piglin in 1.16+, so seemed fair

__on_tick()->(
    if(tick_time()%global_trade_speed==0,
        for(filter(entity_selector('@e[type=zombie_villager]'),nbt(_~'nbt':'HandItems'):'[]':0:'id'=='minecraft:emerald'),
            hand_count=nbt(_~'nbt':'HandItems'):'[]':0:'Count';
            trade=first(nbt(_~'nbt':'Offers':'Recipes'):'[]',
                _:'buy':'id'=='minecraft:emerald' &&//only emerald trades
                _:'buyB':'id'=='minecraft:air' &&//don't want second trade item
                _:'maxUses'!=0 &&//Cos if not it's cheaty, dont work rn
                cost=_:'buy':'Count'<=hand_count//Need to have enough emeralds
            );
            run(str('data merge entity %s {HandItems:[{id:"minecraft:emerald",Count:%db}]}',_~'command_name',hand_count-cost));
            spawn('item',pos(_),str('{Item:{id:"%s",Count:%db,PickupDelay:10}}',trade:'sell':'id',trade:'sell':'Count'));
        )
    )
)