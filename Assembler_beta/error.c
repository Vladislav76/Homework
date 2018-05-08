#include <stdio.h>
#include <stdlib.h>
#include "error.h"
#include "memory.h"

char* nameErrors[] = {"Zero element",
                      "Invalid characters in the line.",
                      "Structure is broken in the line.",
                      "Invalid mark.",
                      "Unknown command.",
                      "Invalid argument.",
                      "There should not be an argument.",
                      "No argument.",
                      "Duplicate mark.",
                      "Not enough values in the stack.",
                      "This mark doesn't exist.",
                      "Argument should be a number.",
                      "Argument should be a mark.",
                      "Command is missing.",
                      "To go to the label, need to have at the top of "
                              "the stack not zero, but the stack is empty.",
                      "Extraneous word in the line."};

struct Error* newError(int type, int numString, char* line) {
    struct Error* tempError = (struct Error*) calloc(1, sizeof(struct Error));
    checkAllocate(tempError);
    tempError -> type = type;
    tempError -> numString = numString;
    tempError -> line = line;
    return tempError;
}

void throwError(struct Error* error) {
    printf("<line %d> ERROR. %s\n", error -> numString, nameErrors[error -> type]);
    if (error -> line != NULL) {
        printf("%s", error -> line);
    }
    printf("\n");
    deleteError(error);
}

void deleteError(struct Error* error) {
    free(error);
}
