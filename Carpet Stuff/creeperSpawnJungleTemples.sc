//Carpet rule
//By: Ghoulboy

__on_tick()->(
    for(filter(entity_selector('@e'),_~'category'=='monster'),
        print('1');
        if(_~'age'==0,continue());//Age dont work, use tags
        print('2');
        l(x,y,z)=pos(_);
        if(structure_references(x,y,z,'jungle_temple'),continue());//For players with spawn eggs:||entity_area('players',x,y,z,24,24,24)
        spawn('creeper',pos(_));
        modify(_,'remove')
    )
)