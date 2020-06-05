//Right clicking at a villager with blaze rod will show a line from you to villager, villager to workstation
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
                draw_shape('line',20,'from',pos(entity)+l(0,entity~'eye_height',0),'to',pos(player)+l(0,player~'eye_height'/2,0),'color',0xFF0000FF,'width',10);
                draw_shape('line',20,'from',pos(entity)+l(0,entity~'eye_height',0),'to',l(x+0.5,y+0.5,z+0.5),'color',0xFF0000FF,'width',10)
            )
        )
    )
)