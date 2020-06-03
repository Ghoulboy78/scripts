//Draw command in scarpet
//By: Ghoulboy & gnembon
//The reason I used c_for loops is cos I copied directly from java and am too lazy to replace it all myself.
//Also may serve as an example to newcomers to how c_for loops work

__command()->print('Draw command.');

//Command functions

ball(radius, block)-> __drawSphere(radius,block,true);

sphere(radius, block)-> __drawSphere(radius, block,false);

cone(x, y, z, radius, height, pointup, orientation, block)->__drawPyramid(x, y, z, radius, height, pointup, orientation, block, false);

pyramid(x, y, z, radius, height, pointup, orientation, block)->__drawPyramid(x, y, z, radius, height, pointup, orientation, block, true);

diamond_shape(cx,cy,cz,radius,block)->(//If I do diamond, it conflicts with diamond iterator
    affected=0;
    for(range(radius),
        y=_-radius+1;
        r=_;
        for(range(-r,r+1),
            x=_;
            z=r-abs(x);
            affected+=__set_block(x+cx,cy+y,cz-z,block);
            affected+=__set_block(x+cx,y+cy,z+cz,block);
            affected+=__set_block(x+cx,cy-y,cz-z,block);
            affected+=__set_block(x+cx,cy-y,cz+z,block)
        );
    );
    print('Successfully filled '+affected+' blocks')
);

//Other functions

__set_block(pos,block)->(
    if(block(pos)!=block,
        set(pos,block);
        return(1)
    );
    return(0)
);

__lengthSq(x,y,z)->return((x * x) + (y * y) + (z * z));

__fillFlat(x,y,z, offset,radius,orientation,block,square)->(
    success=0;
    if(orientation=='x',
        scan(x + offset,y,z, 0, radius, radius,
            if((square||sqrt(__lengthSq(x + offset -_x,y -_y,z -_z))<1),
                success+=__set_block(pos(_),block)
            )
        )
    );
    if(orientation=='y',
        scan(x,y+ offset,z, radius, 0, radius,
            if((square||sqrt(__lengthSq(x -_x,y + offset -_y,z -_z))<1),
                success+=__set_block(pos(_),block)
            )
        )
    );
    if(orientation=='z',
        scan(x,y,z+ offset, radius, radius, 0,
            if((square||sqrt(__lengthSq(x -_x,y -_y,z + offset -_z))<1),
                success+=__set_block(pos(_),block)
            )
        )
    );
    return(success)
);

__drawPyramid(x, y, z, radius, height, pointup, orientation, block, isSquare)->(
    affected=0;
    c_for(i =0, i<height, i+=1,
        r = if(pointup, radius - radius * i / height - 1 , radius * i / height);
        affected+=__fillFlat(x,y,z,i,r,orientation,block,isSquare);
    );

    print('Successfully filled '+affected+' blocks');
);

__drawSphere(radius,block,solid)->(

	pos=pos(player());
    affected = 0;

    radiusX = radius+0.5;
    radiusY = radius+0.5;
    radiusZ = radius+0.5;

    invRadiusX = 1 / radiusX;
    invRadiusY = 1 / radiusY;
    invRadiusZ = 1 / radiusZ;

    ceilRadiusX = ceil(radiusX);
    ceilRadiusY = ceil(radiusY);
    ceilRadiusZ = ceil(radiusZ);


    nextXn = 0;

    c_for (x = 0, x <= ceilRadiusX, x+=1,
        xn = nextXn;
        nextXn = (x + 1) * invRadiusX;
        nextYn = 0;
        c_for (y = 0, y <= ceilRadiusY, y+=1,

            yn = nextYn;
            nextYn = (y + 1) * invRadiusY;
            nextZn = 0;
            c_for (z = 0, z <= ceilRadiusZ, z+=1,

                zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;

                distanceSq = __lengthSq(xn, yn, zn);
                if (distanceSq > 1,break());

                if (!solid && __lengthSq(nextXn, yn, zn) <= 1 && __lengthSq(xn, nextYn, zn) <= 1 && __lengthSq(xn, yn, nextZn) <= 1,
                    continue();
                );

                c_for (xmod = -1, xmod < 2, xmod += 2,

                    c_for (ymod = -1, ymod < 2, ymod += 2,

                        c_for (zmod = -1, zmod < 2, zmod += 2,

                            affected+=__set_block(pos+l(xmod * x,ymod * y,zmod * z),block);
                        )
                    )
                )
            )
        )
    );
    return('Successfully sent '+affected+' blocks')
);

