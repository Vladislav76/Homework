#include <stdio.h>
#include <stdlib.h>

void checkAllocate(void* array) {
    if (array == NULL) {
        printf("Error: The memory didn't allocate!");
        exit(0);
    }
}

void allocateMemoryInt(int* array, int size) {
    array = (int*) calloc((size_t) size, sizeof(int));
    checkAllocate(array);
}


