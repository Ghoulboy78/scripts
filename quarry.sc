//Allows player to make quarries
//By:Ghoulboy

__config()->(
	m(
		l('stay_loaded',true),
		l('scope','global')
	)
);
//l(x,y,z,progress,tickspeed,)
global_quarries=l(l(null,null,null)
	
);

documentation()->(
	print('');
	print('Welcome to Ghoulboy\'s quarry app\datapack!');
	print('To start, please place a shulker box renamed to \'Quarry\'');
	print('In it you must put: a name tag in the first slot renamed to the name of your quarry,');
	print('Followed by six nametags(x1,y1,z1,x2,y2,z2)');
	print('These are the corners of the two corners of the quarry\'s range. Fill the last two slots of the row with any old dummy item.');
	print('On the second row, please place a tribute of at least one coal and at most 1 stack of diamonds in each slot');
	print('1 coal= +1 item/hour,1 iron= +10 items/hour,1 gold= +30 items/hour,1 diamond= +60 items/hour');
	print('You can also use block versions which are obviously 9 times faster');
	print('Minimum is 9 coal, so 9 items/hour, and max is 9 stacks of diamond blocks ,which is 311040 items/hour, or 86.4 items/second.');
	print('On the third row, please enter the coordinates to which you want the items to be teleported with three name tags(x,y,z)');
	print('You must add three coordinates(x1,y1,z1,x2,y2,z2,x3,y3,z3)');
	print('Items wil alternate between these three storage points, useful to handle full 9 stacks of diamond blocks of speed.');
	print('');
	print('There is a hard limit of 10 quarries per world, and it is hard to change that number');
	print('');
	
);

__on_player_right_clicks_block(player, item_tuple, hand, block, face, hitvec) ->(
	l(item, count, nbt) = item_tuple || l('None', 0, null);
	if (item_tuple && item_tuple:0 ~ 'shulker_box',
		newb = pos_offset(block,face);
		schedule(0, '__add_quarry', newb)
	);
	if (item=='name_tag',
        data = nbt;
        if(has(data:'display':'Name') && nbt(data:'display':'Name'):'text',print(number(nbt(data:'display':'Name'):'text')-4));
    )
);

__add_quarry(position)->(
	block=block(position);
	if (block-'shulker_box',
		data = block_data(position);
		if(has(data:'CustomName') && nbt(data:'CustomName'):'text' == 'Quarry',
			__quarry(block)
		);
	)
);

__quarry(block)->(
	saved=true
	loop(10,
		if(global_quarries:_:1==null&&saved==false,
			global_quarries:_=pos(block);
			saved=true
		);
	);
	contentnames=l('','','','','','','','','','','','','','','','','','','','','','','','','','','');
	myprecious=l('coal','iron','gold','diamond');
	preciouscounts=l(0,0,0,0);
	preciousvalues=l(1,10,30,60);
	speed=0;
	loop(27,
		l(item,count,nbt)=inventory_get(block,_) || l('None', 0, null);;
		if(item=='None',print('Invalid quarry');return(),
			if(item=='name_tag',
				data = nbt;
				if(has(data:'display':'Name') && nbt(data:'display':'Name'):'text',	
					contentnames:_=nbt(data:'display':'Name'):'text'
				)
			);
			if(has(myprecious,item),
				preciouscounts:get(myprecious,item)+=count
			);
			if(has(myprecious,item-'_block'),//was using lapis instead of gold, then changed here cos naming is a pain in the ass
				preciouscounts:get(myprecious,item)+=count*9
			)
		)
	);
	create_marker(contentnames:0,pos(block));
	pos1=l(number(contentnames:1),number(contentnames:2),number(contentnames:3));
	pos2=l(number(contentnames:4),number(contentnames:5),number(contentnames:6));
	loop(5,
		speed+=preciousvalues:_*preciouscounts:_//number of times precious items appear x their value in speed
	);
	drop1=l(number(contentnames:18),number(contentnames:19),number(contentnames:20));
	drop2=l(number(contentnames:21),number(contentnames:22),number(contentnames:23));
	drop3=l(number(contentnames:24),number(contentnames:25),number(contentnames:26));
	
	tickspeed=speed/72000
	
);