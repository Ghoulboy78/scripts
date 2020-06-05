//Issue #268 on gnembon/fabric-carpet
//By: Ghoulboy

__on_player_uses_item(player, item_tuple, hand)->(
    if(item_tuple:0=='blaze_rod',
        entity=query(player,'trace',20,'entities')||null;
        if(type(entity)=='entity'&&entity~'type'=='villager',
            brain= entity~'nbt':'Brain';
            mem=brain:'memories';
            pos=mem:'"minecraft:job_site"':'value':'pos'||mem:'"minecraft:job_site"':'pos'||null;
            if(pos,
                l(x,y,z)=nbt(pos):'[]';
                print('click');
                draw_shape('line','happy_villager',pos(entity),pos(player));
                draw_shape('line','happy_villager',pos(entity),l(x+0.5,y+0.5,z+0.5),0.1)
            )
        )
    )
)