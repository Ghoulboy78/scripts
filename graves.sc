//Allows players to have graves when they die
//By: Ghoulboy

global_graves=m();
global_coords=m();

check_health(entity)->(
	print(entity);
	print(entity~'health')
);

get_stuff(entity, source)->(
	print(source);
	print(entity~'health');
	stuff=l();
	for(range(inventory_size(entity)),
		if(inventory_get(entity,_),print(inventory_get(entity,_));put(stuff,length(stuff),inventory_get(entity,_)));
	);
	schedule(0,'make_grave',entity,stuff)
);

make_grave(player,stuff)->(
	global_graves:player=stuff;
	global_coords:player=pos(player);
	print(global_graves);
	print(player+' died at '+pos(player)+'. Go get yo stuff!');
	coords=pos(player)+l(0,1,0);
	for(entity_selector(str('@e[type=item,x=%d,y=%d,z=%d,dx=1,dy=1,dz=1]',coords)),modify(_,'remove'));
);

__on_player_starts_sneaking(player)->(
	if(pos(player)!=global_coords:player-l(0,1,0),return());
	print(global_graves:player);
);

__on_tick() ->(
	e=entity_selector('@e[type=player]');
	for(e,
//		print(_);
		entity_event(_, 'on_death', 'get_stuff');
	);
)