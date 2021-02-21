//Mandelbulb script by Ghoulboy
//Inspired by EDDXample's video: https://www.youtube.com/watch?v=-h41T4x3QkY

import('math','_vec_length', '_round');

__config() ->{
    'commands'->{
        'full <start_pos> <end_pos>' -> ['fractal', 0],
        'full <start_pos> <end_pos> <zoom>' -> 'fractal',
        'mand <float> <float> <float>' -> _(x, y, z)-> mandfunc([x, y, z]),
        'limit <int>' -> _(int) -> global_limit = int,
        'material <material>' -> _(m) ->global_material = m
    },
    'arguments'->{
        'zoom'->{'type'->'int'},
        'material' -> {'type'->'term', 'options'->['stained_glass', 'concrete', 'wool', 'terracotta', 'glazed_terracotta']}//concrete powder for melted effect?
    }
};

global_colours = ['air', 'white_', 'pink_', 'red_', 'purple_', 'cyan_', 'blue_', 'gray_', 'black_'];

global_material = 'stained_glass';

global_limit = 2000;

global_completeness = 0; // percentage to display to player while waiting for result

mandfunc(pos)->(
    [x, y, z] = pos;
    n = 8;
    r = _vec_length(pos);
    theta = atan2(_vec_length([x, y]), z);
    phi = atan2(y, z);
    [r ^ n * sin(theta * n) * cos(phi * n), r ^ n * sin(theta * n) * sin(phi * n), r^n * cos(theta * n)]
);

mandelbulb(pos_diff, avg_pos, zoom) ->(
    ret = [];

    pos_diff = map(pos_diff/2, round(_));

    volume = reduce(pos_diff*2, (_+1)*_a, 1);

    c_for(x = -pos_diff:0, x<=pos_diff:0, x +=1,
        c_for(y = -pos_diff:1, y<=pos_diff:1, y +=1,
            c_for(z = -pos_diff:2, z<=pos_diff:2, z +=1,
                pos = ([x, y, z])/pos_diff;
                newpos = pos;
                op = 0;
                while( newpos:0^2 + newpos:1^2 + newpos:2^2 > 4, global_limit, //doing it this way to be more computationally efficient
                    newpos = mandfunc(newpos) + pos; //as showcased in EDDXample's video
                    op+=1
                );
                global_completeness += 1/volume; // could update percentage with calculation, but this is faster  and same efficiency tbh

                if((global_completeness*volume)%10, sleep(10)); //sleeping a bit every 10th position, to allow to handle server stopping/crashing, at the cost of being slower for small operations

                material = global_colours:round(ln(op));

                ret += [[x, y, z] +avg_pos, material + if(material != 'air', global_material, '')] //cos if its air we dont wanna add suffix
            );
        );
    );
    ret
);

fractal(start, end, zoom)->(

    min_pos = map(start, min(_, end:_i));
    max_pos = map(start, max(_, end:_i));
    pos_diff = max_pos-min_pos;
    avg_pos = (start+end)/2;

    start_time = unix_time();

    mandfunc_task = task('mandelbulb', pos_diff, avg_pos, zoom);

    while(!task_completed(mandfunc_task), 10^10, //unlikely to reach this point, if you do then u have major problems and stopping is best
        if(system_info('world_gamerules'):'commandBlockOutput', // to toggle feedback
            print(format('gi Operation '+ _round(global_completeness*100, 0.1) + '% completed'))
        );
        sleep(1000) // so that you get periodic message, and so that task has time to complete before running out. Also cos cant do task_dock on main thread :-(
    );

    if(task_completed(mandfunc_task), // in case we exited form while loop due to timeout, in which case we say oops
        blocks = task_value(mandfunc_task); //still gotta set blocks ofc

        for(blocks,
            set(_:0, _:1);
        );
        global_completeness = 0;
        if(system_info('world_gamerules'):'commandBlockOutput', // to toggle feedback
            print(format('gi Completed operation in ' + (unix_time()-start_time) + ' ms'))
        )
        ,
        if(system_info('world_gamerules'):'commandBlockOutput',
            print(format('gi Oops, it failed... try again with less volume or complexity...'))
        )
    )
);
