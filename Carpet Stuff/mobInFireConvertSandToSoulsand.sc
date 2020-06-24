//Inspired by carpet-extra rule of the same name
//By: Ghoulboy

//Events

__on_tick()->(
    for(entity_selector('@e'),
        if(!query(_,'has_tag','SandtoSoulsand'),
            modify(_,'tag','SandtoSoulsand');
            entity_event(_,'on_death','__sand_to_soul_sand')
        )
    )
);

//Other functions

__sand_to_soul_sand(entity,cause)->if(entity~'category'!='misc'&&(cause=='inFire'||'onFire')&&block(pos(entity)-l(0,1,0))=='sand',set(pos(entity)-l(0,1,0),'soul_sand'));//All the checks in one