//Carpet rule
//By: Ghoulboy

import('math','_euclidean');

__config()->m(l('scope','global'));

__on_tick()->(
    for(filter(entity_selector('@e'),_~'category'=='monster'&&_~'type'!='husk'),
        m=_;
        l(x,y,z)=pos(m);
        if(query(m,'has_tag','player_nearby'),
            if(p=filter(entity_area('players',x,y,z,24,24,24),_euclidean(pos(_),pos(m))<=24),
                modify(m,'tag','player_nearby');
                return()
            );
            box_1 = structures(x,y,z):'desert_temple':0;
            box_2 = structures(x,y,z):'desert_temple':1;
            if(__in_volume(box_1,box_2,pos(m)),
                spawn('husk',pos(m));
                modify(m,'remove')
            )
        )
    )
);

__in_volume(box_1,box_2,pos)->(//Checks if pos is in volume defined by box_1 and box_2
    l(bx1,by1,bz1)=box_1;
    l(bx2,by2,bz2)=box_2;
    l(x,y,z)=pos;
    l(min_x,min_y,min_z)=l(min(bx1,bx2),min(by1,by2),min(bz1,bz2));
    l(max_x,max_y,max_z)=l(max(bx1,bx2),max(by1,by2),max(bz1,bz2));
    return((min_x<=x)&&(x<=max_x)&&(min_y<=y)&&(y<=max_y)&&(min_z<=z)&&(z<=max_z))
);