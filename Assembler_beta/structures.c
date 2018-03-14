#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "structures.h"
#include "memory.h"
#include "string.h"

///* * * * * C O N S T R U C T O R S * * * * *
struct Code* newCode() {
    struct Code* tempCode;
    tempCode = (struct Code*) calloc(1, sizeof(struct Code));
    checkAllocate(tempCode);

    ///Create array of commands
    tempCode -> pCommands = (struct Command**) calloc(MAX_COMMANDS, sizeof(struct Command*));
    checkAllocate(tempCode -> pCommands);
    tempCode -> maxCommands = MAX_COMMANDS;

    ///Create array of marks
    tempCode -> pMarks = (struct Mark**) calloc(MAX_MARKS, sizeof(struct Mark*));
    checkAllocate(tempCode -> pMarks);
    tempCode -> maxMarks = MAX_MARKS;

    ///Create array of arguments
    tempCode -> pArguments = (struct Argument**) calloc(MAX_ARGUMENTS, sizeof(struct Argument*));
    checkAllocate(tempCode -> pArguments);
    tempCode -> maxArguments = MAX_ARGUMENTS;
    return tempCode;
}

struct Stack* newStack() {
    struct Stack* tempStack = (struct Stack*) calloc(1, sizeof(struct Stack));
    checkAllocate(tempStack);
    tempStack -> storage = (int*) calloc(MAX_STACK, sizeof(int));
    checkAllocate(tempStack -> storage);
    tempStack -> maxSize = MAX_STACK;
    tempStack -> top = -1;
    return tempStack;
}

int* newData() {
    int* tempData = (int*) calloc(MAX_DATA, sizeof(int));
    checkAllocate(tempData);
    return tempData;
}

struct Command* newCommand(int type, int numString, struct Argument* arg, struct Mark* mark) {
    struct Command* tempCommand = (struct Command*) calloc(1, sizeof(struct Command));
    checkAllocate(tempCommand);
    tempCommand -> type = type;
    tempCommand -> numString = numString;
    tempCommand -> arg = arg;
    tempCommand -> mark = mark;
    return tempCommand;
}

struct Mark* newMark(struct String* string, int numCommand) {
    struct Mark* tempMark = (struct Mark*) calloc(1, sizeof(struct Mark));
    checkAllocate(tempMark);
    tempMark -> numCommand = numCommand;
    tempMark -> inform = (struct String*) calloc(1, sizeof(struct String));
    checkAllocate(tempMark -> inform);
    copyString(tempMark -> inform, string);
    return tempMark;
}

struct Argument* newArgument(struct String* string, int valueArg, int numCommand) {
    struct Argument* tempArgument = (struct Argument*) calloc(1, sizeof(struct Argument));
    checkAllocate(tempArgument);
    tempArgument -> valueArg = valueArg;
    tempArgument -> numCommand = numCommand;
    tempArgument -> inform = (struct String*) calloc(1, sizeof(struct String));
    checkAllocate(tempArgument -> inform);
    copyString(tempArgument -> inform, string);
    return tempArgument;
}

///* * * * * D E S T R U C T O R S * * * * *
void deleteCode(struct Code* code) {
    for(int i = 0; i < code -> commands; i++) {
        deleteCommand(code -> pCommands[i]);
    }
    for(int i = 0; i < code -> marks; i++) {
        deleteMark(code -> pMarks[i]);
    }
    for(int i = 0; i < code -> arguments; i++) {
        deleteArgument(code -> pArguments[i]);
    }
    free(code -> pCommands);
    free(code -> pMarks);
    free(code -> pArguments);
    free(code);
}

void deleteStack(struct Stack* stack) {
    free(stack -> storage);
    free(stack);
}

void deleteData(int* data) {
    free(data);
}

void deleteCommand(struct Command* command) {
    free(command);
}

void deleteMark(struct Mark* mark) {
    free(mark -> inform -> s);
    free(mark -> inform);
    free(mark);
}

void deleteArgument(struct Argument* argument) {
    free(argument -> inform -> s);
    free(argument -> inform);
    free(argument);
}

///* * * * * A D D E R S * * * * *
void addCommand(struct Code* code, struct Command* command) {
    if (code -> commands == code -> maxCommands) {
        code -> maxCommands = (int) (code -> maxCommands * COEF_CHANGE);
        code -> pCommands = (struct Command**)
                realloc(code -> pCommands, code -> maxCommands * sizeof(struct Command*));
        checkAllocate(code -> pCommands);
    }
    code -> pCommands[code -> commands] = command;
    code -> commands++;
}

void addMark(struct Code* code, struct Mark* mark) {
    if (code -> marks == code -> maxMarks) {
        code -> maxMarks = (int) (code -> maxMarks * COEF_CHANGE);
        code -> pMarks = (struct Mark**)
                realloc(code -> pMarks, code -> maxMarks * sizeof(struct Mark*));
        checkAllocate(code -> pMarks);
    }
    code -> pMarks[code -> marks] = mark;
    code -> marks++;
}

void addArgument(struct Code* code, struct Argument* argument) {
    if (code -> arguments == code -> maxArguments) {
        code -> maxArguments = (int) (code -> maxArguments * COEF_CHANGE);
        code -> pArguments = (struct Argument**)
                realloc(code -> pArguments, code -> maxArguments * sizeof(struct Argument*));
        checkAllocate(code -> pArguments);
    }
    code -> pArguments[code -> arguments] = argument;
    code -> arguments++;
}

int getElement(struct Stack* stack, int x) {
    return stack -> storage[x];
}

void push(struct Stack* stack, int x) {
    if (stack -> top == stack -> maxSize - 1) {
        stack -> maxSize = (int) (COEF_CHANGE * stack -> maxSize);
        stack -> storage = (int*) realloc(stack, stack -> maxSize * sizeof(int));
        checkAllocate(stack -> storage);
    }
    stack -> top++;
    stack -> storage[stack -> top] = x;
}

int pop(struct Stack* stack) {
    if (stack -> top == -1) {
        printf("Error: The stack is empty!\n");
        exit(0);
    }
    else {
        return stack -> storage[stack -> top--];
    }
}

///* * * * * P R I N T I N G * * * * *
void printStack(struct Stack* stack) {
    printf("* * * * * S T A C K * * * * *\n");
    for(int i = stack -> top; i >= 0; i--) {
        printf("[%d]: %d\n", i, stack -> storage[i]);
    }
    printf("* * * * * * * * * * * * * * *\n");
}

void printCode(struct Code* code) {
    printf("* * * * * * * * * * C O M M A N D S * * * * * * * * * *\n\n");
    int numCommands = code -> commands;
    for(int i = 0; i < numCommands; i++) {
        struct Command* tc = code -> pCommands[i];
        char* s;
        char* label;
        int value = 0;
        if (tc -> arg != NULL) {
            s = tc -> arg -> inform -> s;
            value = tc -> arg -> valueArg;
        }
        else {
            s = "None";
        }
        if (tc -> mark != NULL) {
            label = tc -> mark -> inform -> s;
        }
        else {
            label = "None";
        }
        printf("code[%d]: numString = %d, type = %d, mark = %s, arg = %s, value arg = %d\n",
               i, tc -> numString, tc -> type, label, s, value);
    }
    printf("\n");

    printf("* * * * * * * * * * * * M A R K S * * * * * * * * * * *\n\n");
    int numMarks = code -> marks;
    for(int i = 0; i < numMarks; i++) {
        struct Mark* tc = code -> pMarks[i];
        printf("mark[%d]: numCommand = %d, name = %s\n", i, tc -> numCommand, tc -> inform -> s);
    }
    printf("\n");

    printf("* * * * * * * * * * * * A R G S * * * * * * * * * * * *\n\n");
    int numArgs = code -> arguments;
    for(int i = 0; i < numArgs; i++) {
        struct Argument* tc = code -> pArguments[i];
        printf("mark[%d]: numCommand = %d, value = %d, name = %s\n", i, tc -> numCommand,
               tc -> valueArg, tc -> inform -> s);
    }
    printf("\n");
}

void printData(int* data) {
    printf("* * * * * * * * * * * * D A T A * * * * * * * * * * * *\n");
    for(int i = 0; i < 150; i++) {
        if (i % 10 == 0) {
            printf("\n");
        }
        printf("%3d", data[i]);
    }
    printf("\n");
    printf("\n");
}