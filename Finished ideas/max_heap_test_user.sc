print('Began loading max_heap_test_user.sc');
__config()->{
    'libraries'->[
        {
            'source'->'https://raw.githubusercontent.com/Ghoulboy78/scripts/master/Finished%20ideas/max_heap.scl'
        }
    ]
};
print('Loaded config for max_heap_test_user.sc');
import('max_heap', 'max_heap');
print('Loaded max_heap.scl for max_heap_test_user.sc');

import('classes', 'new_class', 'new_object', 'call_function');
print('Loaded classes.scl for max_heap_test_user.sc');
print('Successfully loaded max_heap_test_user.sc');
