//See https://www.youtube.com/watch?v=mXLvl4dWLyg a branch mining bot by Earthcomputer
//By: Ghoulboy


//Breaks a stright line of blocks
//Max length of tunnel is : (number of torches prestacked in offhand) * 6 + (initial light level at the opening of the tunnel-7) 
//cos calculations are easier that way, and torhces are placed in such a way that lightlevel only ever goes down to 8
__command()->(print('Welcome to the BranchMiner program inspired by Earthcomputer\'s branchminer!'));

global_niceblocks=l('coal_ore','iron_ore','emerald_ore','gold_ore','redstone_ore','diamond_ore','diorite');//I'll ignore all the rest, and throw out of inventory

branch_mine()->(
    player=player();
    print(pos(block(pos(player))));//Remind me to remove this line and the one below
    print(query(player,'facing',0));
    if(!player~'holds':0~'diamond_pickaxe',return());//If you want you can make it for any pick, by removing the 'diamond_' in front of 'pickaxe'

    if(inventory_get(player,40):0!='torch'&&inventory_get(player,40):0!='soul_torch',
        print(inventory_get(player,40):0+' is an invalid offhand item; it must be a torch or (in 1.16+) a soul torch');
        return()
    );//soul torch for 1.16 and above

    torchtype=if(inventory_get(player,40):0=='torch','torch','soul_torch')

    if(block(pos_offset(pos(block(pos(player))),query(player,'facing',0))+l(0,1,0))=='air'||block(pos_offset(pos(block(pos(player))),query(player,'facing',0)))=='air',return());//checks that youre looking at non-air blocks to start mining
    while(true,inventory_get(player,40):1*6+light(pos(player))-7,
        if(inventory_get(player,40):0!='torch'&&inventory_get(player,40):0!='soul_torch'&&light(pos(player))==8,return());
        if(light(pos(player)==2,
            inventory_remove(player,torchtype,1,40));

        );
    );
);
