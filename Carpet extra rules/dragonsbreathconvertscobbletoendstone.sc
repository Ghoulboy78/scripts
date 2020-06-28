//carpet-extra rule
//By: Ghoulboy
__on_tick()->(
    for(entity_selector('@e[type=item,nbt={Item:{id:"minecraft:dragon_breath",Count:1b}}]'),
        if(block(pos(_))=='cobblestone'&&for(neighbours(pos(_)),block(pos(_))=='dispenser'),
            set(pos(_),'end_stone');
            modify(_,'remove')
        )
    )
)