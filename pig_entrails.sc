//A jokey suggestion on gnembon/fabric-carpet github.
//By: Ghoulboy

import('func_lib','__holds');

//Global variables

global_debug=true;

global_rewards=l(//l('item_name',min_weight,max_weight,count,'phrase to finish sentence')
    l('netherite_scrap',0,0,1,'netherite scraps, to make the finest armour!'),
    l('diamond',1,3,2,'diamonds, to bedeck your gleaming throne!'),
    l('redstone',4,10,6,'redstone dust, to show your technical genius!'),
    l('lapis_lazuli',11,39,25,'lapis lazuli, to enchant your weapons!?'),
    l('gold_nugget',40,60,54,'gold nuggets, to, umm, offer your ancestors?'),
    l('iron_nugget',61,99,81,'iron nuggets, to, err, I don\'t really know...')
);

//Events

__on_player_attacks_entity(player, entity)->(
    if(entity=='Pig',//reminds me of my first scarpet app. So much has changed
        schedule(0,'_spawn_entrails',player,entity)
    )
);

__on_player_drops_item(player)->(
    schedule(0,'_check_drop_item',player);
);

//Other functions

_spawn_entrails(player, entity)->(
    if(entity~'health'==0 && round(rand(10-__holds(player,'sword','looting')))==0,
        spawn('item',pos(entity),'{Item:{id:"minecraft:string",Count:'+(1+round(rand(__holds(player,'sword','looting'))))+'b,tag:{display:{Name:\'{"text":"Pig Entrails","italic":false}\'}}}}')
    )
);

_check_drop_item(player)->(
    item=first(entity_area('items',pos(player):0,pos(player):1+player~'eye_height',pos(player):2,0.1,0.1,0.1),_~'nbt':'Item'=='{id:"minecraft:string",Count:1b,tag:{display:{Name:\'{"text":"Pig Entrails","italic":false}\'}}}'&& !_~'nbt':'Age');
    if(item,entity_event(item,'on_tick','_check_sacrifice',player,round(rand(10))))
);

_check_sacrifice(entrails,player,init_sacrifice)->(
    l(ex,ey,ez)=pos(entrails);
    block=block(l(ex,ey-0.1,ez));

    if(block!='obsidian'&&block!='air'&&block!='fire',
        print('How dare you disrespect the offering! You shall be punished!');
        modify(player,'kill');
        modify(entrails,'remove');
        return()
    );
    modify(entrails,'health',10e10);

    if(init_sacrifice==0&&block=='obsidian',//If you're not holding sacrificial knife, you die
        if(query(player,'holds','offhand')!=l('flint',1,'{RepairCost:0,display:{Name:\'{"text":"Sacrificial knife"}\'}}'),
            print('You have been cursed by Hades, the god of the Underworld!');
            modify(player,'kill'),
            print('The gods have held off their anger for you, priest. They may not do so again!')
        );
        modify(entrails,'remove')
    );

    if(block=='obsidian',//Proper altar required, of course
        if(block(pos(entrails))=='fire'&&init_sacrifice>7,//Burn to get goodies, todo combine with bow to make it stronger
            print('The god Hades has accepted your offering, mortal!');
            sacrifice=round(rand(100));
            offering=first(global_rewards,sacrifice>_:1 && sacrifice<_:2);
            spawn('item',pos(player),str('{Item:{id:"minecraft:%s",Count:%db}}',offering:0,offering:3));
            print('For your offering, you shall be rewarded with '+offering:4);
            modify(entrails,'remove')
        );
        bows=entity_area('items',ex,ey,ez,0.5,0.5,0.5);
        print(bows);
    )
)