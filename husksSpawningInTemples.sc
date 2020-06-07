//Carpet rule
//By: Ghoulboy

import('func_lib','__in_volume','__euclidean','__block_pos');

__config()->m(l('scope','global'));

__on_tick()->(
    for(filter(entity_selector('@e'),_~'category'=='monster'&&_~'type'!='husk'),
        m=_;
        l(x,y,z)=pos(m);
        if(query(m,'has_tag','player_nearby'),
            if(p=filter(entity_area('players',x,y,z,24,24,24),__euclidean(pos(_),pos(m))<=24),
                modify(m,'tag','player_nearby');
                return()
            );
            box_1 = structures(x,y,z):'desert_temple':0;
            box_2 = structures(x,y,z):'desert_temple':1;
            if(__in_volume(box_1,box_2,__block_pos(m)),
                spawn('husk',pos(m));
                modify(m,'remove')
            )
        )
    )
)