
__config()->{
    'libraries'->[
        {
            'source'->'https://raw.githubusercontent.com/Ghoulboy78/scripts/master/Finished%20ideas/classes.scl'
        },
        {
            'source'->'https://raw.githubusercontent.com/Ghoulboy78/scripts/master/Finished%20ideas/max_heap.scl'
        }
    ]
};

import('max_heap', 'max_heap');

import('classes', 'new_class', 'new_object', 'call_function');
print('Successfully loaded max_heap_test_user.sc');
