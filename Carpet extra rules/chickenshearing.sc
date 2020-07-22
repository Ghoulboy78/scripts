//Replaces chickenShearing
//By: Ghoulboy

__on_player_interacts_with_entity(player, entity, hand)->(
    if(entity!='Chicken'||entity~'health'<=0||player~'holds':0!='shears'||hand!='mainhand',return());
    if(player~'gamemode'=='survival',inventory_set(player,player~'selected_slot',1,'shears','{Damage:'+(get(player~'holds':2,'Damage')+1)+'}'));
    spawn('item',pos(entity),'{Motion:[0.0,0.5,0.0],Item:{id:"minecraft:feather",Count:1b}}');
    modify(entity,'health',entity~'health'-(rand(4)/2+0.5))//can remove between 0.5 and 2 health
)
