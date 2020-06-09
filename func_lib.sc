//Useful library of functions/global variables which my apps use. Like malilib fo masa's mods, but this is mostly decorative
//By: Ghoulboy

__config()->m(l('stay_loaded','true'),l('scope','global'));

//Functions

__block_pos(entity)->return(pos(block(pos(entity))));

__distance(pos1,pos2,type)->(
    l(x1,y1,z1) = pos1;
    l(x2,y2,z2) = pos2;
    l(dx,dy,dz) = l(abs(x1-x2),abs(y1-y2),abs(z1-z2));
    if(type=='euclidean',
        return(sqrt(dx*dx+dy*dy+dy*dy)),
        type=='manhattan',
        return(dx+dy+dz),
        type=='cylindrical',
        return(sqrt(dx*dx+dz*dz))
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

__block_types(pos1,pos2)->(
    l(x1,y1,z1)=pos1;
    l(x2,y2,z2)=pos2;
    blocks=m();
    volume(x1,y1,z1,x2,y2,z2,
        blocks:str(block(_))+=1;
    );
    return(blocks)
);

__holds(entity, item_type, enchantment) -> (
	if (entity~'gamemode_id'==3, return(0));
	for(l('mainhand','offhand'),
		holds = query(entity, 'holds', _);
		if( holds,
			l(what, count, nbt) = holds;
			if ((what ~ item_type) && (enchants = get(nbt,'Enchantments[]')),
				// nbt query returns a scalar for lists of size one
				if (type(enchants)!='list', enchants = l(enchants));
				for (enchants,
					if ( get(_,'id') == 'minecraft:'+enchantment,
						level = max(level, get(_,'lvl'))
					)
				)
			)
		)
	);
	level
);

__check_prime(n) -> !first( range(2, sqrt(n)+1), !(n % _) );