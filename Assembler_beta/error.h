#ifndef ASSEMBLER_BETA_ERROR_H
#define ASSEMBLER_BETA_ERROR_H

#endif

#define INVALID_CHARACTER 1
#define BROKEN_STRUCTURE 2
#define INVALID_MARK 3
#define UNKNOWN_COMMAND 4
#define INVALID_ARGUMENT 5
#define ARGUMENT_SHOULD_NOT_BE 6
#define NO_ARGUMENT 7
#define DUPLICATE_MARK 8
#define NOT_ENOUGH_DATA_IN_THE_STACK 9
#define THIS_MARK_DOES_NOT_EXIST 10
#define ARGUMENT_SHOULD_BE_NUMBER 11
#define ARGUMENT_SHOULD_BE_MARK 12
#define COMMAND_IS_MISSING 13
#define ERROR_BR 14
#define EXTRANEOUS_WORD_IN_LINE 15

struct Error {
    int type;
    int numString;
    char* line;
};

struct Error* newError(int, int, char*);
void throwError(struct Error*);
void deleteError(struct Error*);
