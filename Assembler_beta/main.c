#include <stdio.h>
#include <string.h>
#include "structures.h"
#include "interpreter.h"

int main() {
    while (1) {
        printf("\n_ _ _ _ _ _ _ Assembler 1.0 _ _ _ _ _ _ _ \n");
        printf("Enter name of the program: (\"q\" for quit) \n");
        char string[255];
        scanf("%s", string);
        if (strcmp(string, "q") == 0) {
            break;
        }
        struct Code* myCode = newCode();
        if (loadProgram(string, myCode)) {
            run(myCode);
            //printCode(myCode);
        }
        deleteCode(myCode);
    }
    return 0;
}