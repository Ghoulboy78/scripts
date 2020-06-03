__command()->(print('Welcome to Minesweeper!'));

global_firstclick=true;
global_pos=l(0,0,0);

create_board(cx,cy,cz)->(
    global_pos=l(cx,cy,cz);
    scan(cx,cy,cz,14,0,6,
        set(_,'stone');
        if(!rand(10),set(_,'infested_stone'))
    )
);

__on_player_clicks_block(player, block, face)->(
    __label_spots(block,global_firstclick)
);

__on_player_right_clicks_block(player, item_tuple, hand, block, face, hitvec)->(
    if(block=='stone'||block=='infested_stone',set(pos(block)+l(0,1,0),'red_carpet'));
    if(block=='red_carpet',set(pos(block),'air'))
);

__label_spots(block,firstclick)->(
    if(firstclick&&block(_)=='infested_stone',
        print('You blew up');
        l(cx,cy,cz)=global_pos;
        scan(cx,cy,cz,14,0,6,if(block(_)=='stone',set(pos(_),'air');remove_all_markers())),
        global_firstclick=false
    );
    set(pos(block),'air');
    count=0;
    for(neighbours(block),
        if(block(_)=='infested_stone',count+=1);
        if(block(_)=='stone',__label_spots(_))
    );
);