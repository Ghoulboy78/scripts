//Useful library of functions/global variables which my apps use. Like malilib fo masa's mods, but this is mostly decorative
//By: Ghoulboy

//Currently importable functions:
//Getting enchantment level on an item
//Checking is a number is prime (Just cos)

//Examples at the bottom
//When adding your own function, don't forget to add an explanation how to import/use at the bottom.

__config()->m(l('stay_loaded','true'),l('scope','global'));

//Functions

__global_new_players()->return(global_new_players);

__global_left_players()->return(global_left_players);

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

//Global variables
global_players=l();

global_new_players=l();

global_left_players=l();

//Events

//Examples:
//Getting the enchantment level of a player's item:

//This is much simpler, just import the function to your app:
//import('useful_lib','__holds');
//And then use it to return a number regarding the level of an enchantment on an item.
//Read code to figure out specifics.

//Checking for a prime number
//import('useful_lib','check_prime');
//Use it just like the one above.