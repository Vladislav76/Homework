#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "string.h"
#include "error.h"
#include "structures.h"
#include "memory.h"
#include "interpreter.h"

#define MAX_BUFFER 256

struct String namesCommands[NUM_COMMANDS] = {
        {"ld",  2},
        {"st",  2},
        {"ldc", 3},
        {"add", 3},
        {"sub", 3},
        {"cmp", 3},
        {"jmp", 3},
        {"br",  2},
        {"ret", 3}
};

int numString = 0;

int findMarkInList(struct Mark** pMarks, struct String* string, int numMarks) {
    for(int i = 0; i < numMarks; i++) {
        if (equals(pMarks[i] -> inform, string) == 0) {
            return pMarks[i] -> numCommand;
        }
    }
    return -1;
}

int isDuplicateMark(struct Mark** pMarks, struct Mark* mark, int numMarks) {
    for(int i = 0; i < numMarks; i++) {
        if (equals(pMarks[i] -> inform, mark -> inform) == 0) {
            return 1;
        }
    }
    return 0;
}

int isValidCharacter(char c) {
    return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9'
            || c == ':' || c == '_' || c == '+' || c == '-');
}

int isMark(struct String* string) {
    int k = findCharacter(string, ':');
    if (k != -1) {
        if (string -> length == 1) {
            return -1;
        }
        int plus = findCharacter(string, '+');
        int minus = findCharacter(string, '-');
        if (plus < 0 && minus < 0 && k == string -> length - 1 &&
                (string -> s[0] < '0' || string -> s[0] > '9')) {
            deleteCharacter(string, k);
            return 1;
        }
        else {
            return -1;
        }
    }
    else {
        return 0;
    }
}

int isCommand(struct String* string, int* type) {
    struct String string2;
    string2.s = "";
    if (equals(string, &string2) == 0) {
        return 0;
    }
    for(int i = 0; i < NUM_COMMANDS; i++) {
        if (equals(string, &namesCommands[i]) == 0) {
            *type = i;
            return 1;
        }
    }
    return -1;
}

int isArgument(struct String* string, int* typeArg) {
    if (string -> length == 0) {
        return 0;
    }
    int i = 0;
    int type = NUMBER;
    char symbol = string -> s[0];
    if (symbol == '-' || symbol == '+') {
        i++;
    }
    else if (symbol < '0' || symbol > '9') {
        type = MARK;
        i++;
    }
    if (type == NUMBER) {
        for(; i < string -> length; i++) {
            if (string -> s[i] < '0' || string -> s[i] > '9') {
                return -1;
            }
        }
    }
    else {
        for(; i < string -> length; i++) {
            if (string -> s[i] == '-' || string -> s[i] == '+' || string -> s[i] == ':') {
                return -1;
            }
        }
    }
    *typeArg = type;
    return 1;
}

void checkArguments(struct Code* code) {
    int numErrors = 0;
    int n = code -> arguments;
    for(int i = 0; i < n; i++) {
        struct Argument* pArg = code -> pArguments[i];
        int typeCommand = code -> pCommands[pArg -> numCommand] -> type;
        int numString = code -> pCommands[pArg -> numCommand] -> numString;
        if (typeCommand == JMP || typeCommand == BR) {
            int k = findMarkInList(code -> pMarks, pArg -> inform, code -> marks);
            if (k < 0) {
                throwError(newError(THIS_MARK_DOES_NOT_EXIST, numString, pArg -> inform -> s));
                numErrors++;
            }
            else {
                pArg -> valueArg = k;
            }
        }
        else if (typeCommand == LD || typeCommand == ST) {
            int value = stringToInt(pArg -> inform);
            value %= MAX_DATA;
            if (value < 0) {
                value += MAX_DATA;
            }
            pArg -> valueArg = value;
        }
        else if (typeCommand == LDC) {
            pArg -> valueArg = stringToInt(pArg -> inform);
        }
    }
    if (numErrors > 0) {
        exit(0);
    }
}

int fileNotFound(FILE *file) {
    if (file == NULL) {
        printf("Error: Don't open this file! \n");
        return 1;
    }
    return 0;
}

int loadProgram(char* name, struct Code* code) {
    char* buffer;
    numString = 0;

    int analyzer(char* string, int numCommand) {
        numString++;
        int maxWords = 3;
        struct String* st[maxWords];
        for(int i = 0; i < maxWords; i++) {
            st[i] = newString();
        }
        int isWord = 0;
        int curSt = 0;
        int i = 0;
        int error = -1;

        ///checking string
        while (string[i] != '\0' && string[i] != '\n') {
            if (isValidCharacter(string[i])) {
                if (curSt >= maxWords) {
                    throwError(newError(BROKEN_STRUCTURE, numString, buffer));
                    error = BROKEN_STRUCTURE;
                    goto exit;
                }
                addCharacter(st[curSt], string[i]);
                isWord = 1;
            }
            else if (string[i] == ' ' || string[i] == '\t' || string[i] == ';') {
                if (string[i] == ';') {
                    break;
                }
                if (isWord) {
                    isWord = 0;
                    curSt++;
                }
            }
            else {
                throwError(newError(INVALID_CHARACTER, numString, buffer));
                error =  INVALID_CHARACTER;
                goto exit;
            }
            i++;
        }

        ///checking mark
        int num = 0;
        int this = isMark(st[0]);
        struct Mark* currentMark = NULL;
        if (this > 0) {
            num = 1;
            currentMark = newMark(st[0], numCommand);
            if (isDuplicateMark(code -> pMarks, currentMark, code -> marks)) {
                throwError(newError(DUPLICATE_MARK, numString, buffer));
                deleteMark(currentMark);
                error = DUPLICATE_MARK;
                goto exit;
            }
            else {
                addMark(code, currentMark);
            }
        }
        else if (this < 0) {
            throwError(newError(INVALID_MARK, numString, buffer));
            error = INVALID_MARK;
            goto exit;
        }

        ///checking command and argument
        int typeCommand;
        int typeArg;
        this = isCommand(st[num], &typeCommand);
        if (this > 0) {
            struct Argument* currentArgument = NULL;
            this = isArgument(st[num + 1], &typeArg);
            if (this > 0) {
                if (typeCommand >= ADD && typeCommand <= CMP || typeCommand == RET) {
                    throwError(newError(ARGUMENT_SHOULD_NOT_BE, numString, buffer));
                    error = ARGUMENT_SHOULD_NOT_BE;
                    goto exit;
                }
                else {
                    if (typeCommand == JMP || typeCommand == BR) {
                        if (typeArg == NUMBER) {
                            throwError(newError(ARGUMENT_SHOULD_BE_MARK, numString, buffer));
                            error = ARGUMENT_SHOULD_BE_MARK;
                            goto exit;
                        }
                    }
                    else if (typeArg == MARK) {
                        throwError(newError(ARGUMENT_SHOULD_BE_NUMBER, numString, buffer));
                        error = ARGUMENT_SHOULD_BE_NUMBER;
                        goto exit;
                    }
                    if (num + 2 == maxWords - 1 && st[num + 2] -> length != 0) {
                        throwError(newError(EXTRANEOUS_WORD_IN_LINE, numString, buffer));
                        error = EXTRANEOUS_WORD_IN_LINE;
                        goto exit;
                    }
                    currentArgument = newArgument(st[num + 1], 0, numCommand);
                    addArgument(code, currentArgument);
                }
            }
            else if (this < 0) {
                throwError(newError(INVALID_ARGUMENT, numString, buffer));
                error = INVALID_ARGUMENT;
                goto exit;
            }
            else {
                if (!(typeCommand >= ADD && typeCommand <= CMP || typeCommand == RET)) {
                    throwError(newError(NO_ARGUMENT, numString, buffer));
                    error = NO_ARGUMENT;
                    goto exit;
                }
            }
            addCommand(code, newCommand(typeCommand, numString, currentArgument, currentMark));
        }
        else if (this < 0){
            throwError(newError(UNKNOWN_COMMAND, numString, buffer));
            error = UNKNOWN_COMMAND;
            goto exit;
        }
        else {
            if (num > 0) {
                throwError(newError(COMMAND_IS_MISSING, numString, buffer));
                error = COMMAND_IS_MISSING;
                goto exit;
            }
            else {
                error = 0;
                goto exit;
            }
        }

        exit:
        for(int i = 0; i < maxWords; i++) {
            deleteString(st[i]);
        }
        return error;
    }

    ///beginning
    FILE *in = fopen(name, "r");
    if (fileNotFound(in)) {
        return 0;
    }
    buffer = (char*) calloc(MAX_BUFFER, sizeof(char));
    checkAllocate(buffer);
    int incorrectStrings = 0;
    int numCommands = 0;
    while (!feof(in)) {
        if (fgets(buffer, MAX_BUFFER, in) != NULL) {
            int isError = analyzer(buffer, numCommands);
            if (isError < 0) {
                numCommands++;
                if (code -> pCommands[numCommands - 1] -> type == RET) {
                    break;
                }
            }
            else if (isError > 0) {
                incorrectStrings++;
            }
        }
    }
    free(buffer);
    fclose(in);
    if (code -> commands == 0 || code -> pCommands[code -> commands - 1] -> type != RET) {
        printf("ERROR. You forgot to put \"RET\" in the end of the program.\n");
        exit(0);
    }
    checkArguments(code);
    return !incorrectStrings;
}

void run(struct Code* code) {
    struct Stack* stack = newStack();
    int* data = newData();
    int i = 0;
    int work = 1;
    clock_t startTime = clock();
    while (work) {
        struct Command* tc = code -> pCommands[i];
        int arg = 0;
        if (tc -> arg) {
            arg = tc -> arg -> valueArg;
        }
        int numString = tc -> numString;
        int x, y;
        switch (tc -> type) {
            case LD:
                push(stack, data[arg]);
                break;
            case ST:
                data[arg] = pop(stack);
                break;
            case LDC:
                push(stack, arg);
                break;
            case ADD:
                if (stack -> top < 1) {
                    throwError(newError(NOT_ENOUGH_DATA_IN_THE_STACK, numString, NULL));
                    exit(0);
                }
                x = getElement(stack, stack -> top);
                y = getElement(stack, stack -> top - 1);
                push(stack, x + y);
                break;
            case SUB:
                if (stack -> top < 1) {
                    throwError(newError(NOT_ENOUGH_DATA_IN_THE_STACK, numString, NULL));
                    exit(0);
                }
                x = getElement(stack, stack -> top);
                y = getElement(stack, stack -> top - 1);
                push(stack, x - y);
                break;
            case CMP:
                if (stack -> top < 1) {
                    throwError(newError(NOT_ENOUGH_DATA_IN_THE_STACK, numString, NULL));
                    exit(0);
                }
                x = getElement(stack, stack -> top);
                y = getElement(stack, stack -> top - 1);
                if (x == y) {
                    push(stack, 0);
                }
                else if (x > y) {
                    push(stack, 1);
                }
                else {
                    push(stack, -1);
                }
                break;
            case JMP:
                i = arg;
                continue;
            case BR:
                if (stack -> top == -1) {
                    throwError(newError(ERROR_BR, numString, NULL));
                    exit(0);
                }
                x = getElement(stack, stack -> top);
                if (x != 0) {
                    i = arg;
                    continue;
                }
                break;
            case RET:
                work = 0;
                continue;
        }
        i++;
    }
    clock_t elapsedTime = clock() - startTime;
    double ns = (double) (elapsedTime * 1000000 / CLOCKS_PER_SEC);
    char* str = "mcs";
    if (ns >= 1000) {
        ns /= 1000;
        str = "ms";
        if (ns >= 1000) {
            ns /= 1000;
            str = "sec";
        }
    }
    printf("Program is completed.\n");
    printf("Time of running: %.2f %s\n\n", ns, str);
    printStack(stack);
    deleteData(data);
    deleteStack(stack);
}