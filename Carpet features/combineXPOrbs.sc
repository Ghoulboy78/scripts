//Inspired by carpet rule
//By: Ghoulboy

__on_tick()->(
    for(entity_selector('@e[type=experience_orb]'),
        if(_~'age'<50,continue(),orb1=_);
        l(x1,y1,z1)=pos(orb1);
        orb2=first(filter(entity_area('*',x1,y1,z1,0.5,0.5,0.5),_~'type'=='experience_orb'&&_!=orb1),_~'age'>=50);
        if(!orb2,continue());
        l(x2,y2,z2)=pos(orb2);
        s=spawn('experience_orb',(x1+x2)/2,(y1+y2)/2,(z1+z2)/2,str('{Age:0,Value:%s}',orb1~'nbt':'Value'+orb2~'nbt':'Value'));
        modify(orb1,'remove');
        modify(orb2,'remove');
    )
)