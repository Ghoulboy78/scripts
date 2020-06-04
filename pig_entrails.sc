//A jokey suggestion on gnembon/fabric-carpet github.
//By: Ghoulboy

import('func_lib','__holds');

//Global variables

global_rewards=l(//l('item_name',min_weight,max_weight,count,'phrase to finish sentence')
    l('netherite_scrap',0,0,1,'netherite scraps, to make the finest armour!'),
    l('diamond',1,3,2,'diamonds, to bedeck your gleaming throne!'),
    l('redstone',4,10,6,'redstone dust, to show your technical genius!'),
    l('lapis_lazuli',11,39,25,'lapis lazuli, to enchant your weapons!?'),
    l('gold_nugget',40,60,54,'gold nuggets, to, umm, offer your ancestors?'),
    l('iron_nugget',61,99,81,'iron nuggets, to, err, I don\'t really know...')
);

global_blessings=l(//l('enchant_name',min_weight,max_weight,max_level,'phrase to finish sentence')
    l('infinity',0,0,1,'incessant arrows to give your enemies no rest!'),
    l('flame',1,4,1,'arrows of flame, in his image!'),
    l('punch',5,7,2,'arrows to knock thine enemies to distant lands!'),
    l('power',8,9,5,'arrows that will pierce your enemies!')
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

//Command: /script in pig_entrails run _give_entrails(count) to get 1 entrails (Cos singular of entrails is entrails). Ofc creative only, but idc
_give_entrails(count)->spawn('item',pos(player()),str('{Item:{id:"minecraft:string",Count:%db,tag:{display:{Name:\'{"text":"Pig Entrails","italic":false}\'}}}}',count));

_spawn_entrails(player, entity)->(
    if(entity~'health'==0 && round(rand(10-__holds(player,'sword','looting')))==0,
        spawn('item',pos(entity),'{Item:{id:"minecraft:string",Count:'+(1+round(rand(__holds(player,'sword','looting'))))+'b,tag:{display:{Name:\'{"text":"Pig Entrails","italic":false}\'}}}}')
    )
);

_check_drop_item(player)->(
    item=first(entity_area('items',pos(player):0,pos(player):1+player~'eye_height',pos(player):2,0.1,0.1,0.1),_~'nbt':'Item'=='{id:"minecraft:string",Count:1b,tag:{display:{Name:\'{"text":"Pig Entrails","italic":false}\'}}}'&& !_~'nbt':'Age');
    if(item,entity_event(item,'on_tick','_check_sacrifice',player,round(rand(15))))
);

_check_sacrifice(entrails,player,init_sacrifice)->(
    l(ex,ey,ez)=pos(entrails);
    block=block(l(ex,ey-0.1,ez));

    if(block!='obsidian'&&block!='air'&&block!='fire',
        print('How dare you disrespect the offering! You shall be punished!');
        if(query(player,'holds','offhand')==l('flint',query(player,'holds','offhand'):1,'{RepairCost:0,display:{Name:\'{"text":"Sacrificial knife"}\'}}'),
            print('A priest should know better!');
            inventory_set(player,40,query(player,'holds','offhand'):1-1),//Remove a sacrificial knife, cos you're unworthy of being a priest
            modify(player,'kill')
        );
        modify(entrails,'remove');
        return()
    );

    if(init_sacrifice==0&&block=='obsidian',//If you're not holding sacrificial knife, you die
        if(query(player,'holds','offhand')!=l('flint',query(player,'holds','offhand'):1,'{RepairCost:0,display:{Name:\'{"text":"Sacrificial knife"}\'}}'),
            print('You have been cursed by Hades, the god of the Underworld!');
            modify(player,'kill'),
            print('The gods have held off their anger for you, priest. They may not do so again!')
        );
        modify(entrails,'remove');
        return()
    );

    if(block=='obsidian',//Proper altar required, of course
        if(block(pos(entrails))=='fire'&&init_sacrifice>12,//Burn to get goodies
            print('The god Hades has accepted your offering, mortal!');
            sacrifice=round(rand(150));
            offering=first(global_rewards,sacrifice>=_:1 && sacrifice<=_:2);
            if(!offering,modify(entrails,'remove');return(print('Or not!')));
            spawn('item',pos(player),str('{Item:{id:"minecraft:%s",Count:%db}}',offering:0,offering:3));
            print('For your offering, you shall be rewarded with '+offering:4);
            modify(entrails,'remove')
        );

        if(bow=first(filter(entity_area('items',ex,ey,ez,0.5,0.5,0.5),_~'item':0=='bow'),_~'item':2:'Damage'==0),
            print('The god Apollo is happy with your offering, mortal, and shall bless your bow!');
            blessing_val=round(rand(15));
            blessing=first(global_blessings,blessing_val>=_:1 && blessing_val<=_:2);
            if(!blessing,modify(entrails,'remove');return(print('Nah, kidding, he won\'t!')));
            run(
                str(//Broke it up so you can actually see. Thanks to Aeldrion in McCommands Discord
                    'execute as %s unless data entity @s Item.tag.Enchantments[{id: "minecraft:%s"}] run '+
                    'data modify entity @s Item.tag.Enchantments append value {id:"minecraft:%s",lvl:%ds}',
                    bow~'command_name',
                    blessing:0,
                    blessing:0,
                    round(rand(blessing:3 -1))+1;
                )
            );
            print('You bow shall now shoot '+blessing:4);//data merge entity @e[type=item,limit=1] {Item:{id:"minecraft:bow",Count:1b,tag:{Enchantments:[{id:"minecraft:power",lvl:1s}]}}}
            modify(entrails,'remove')
        )
    )
)