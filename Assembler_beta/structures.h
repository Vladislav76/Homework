#ifndef ASSEMBLER_BETA_STRUCTURES_H
#define ASSEMBLER_BETA_STRUCTURES_H

#endif

#define MAX_STACK 1024
#define MAX_DATA 262144
#define MAX_COMMANDS 128
#define MAX_MARKS 10
#define MAX_ARGUMENTS 20
#define NUMBER 1
#define MARK 2
#define COEF_CHANGE 1.4

#define NUM_COMMANDS 9
#define LD 0
#define ST 1
#define LDC 2
#define ADD 3
#define SUB 4
#define CMP 5
#define JMP 6
#define BR 7
#define RET 8

struct Command {
    int numString;
    int type;
    struct Argument* arg;
    struct Mark* mark;
};
struct Mark {
    struct String* inform;
    int numCommand;
};
struct Argument {
    struct String* inform;
    int valueArg;
    int numCommand;
};
struct Code {
    struct Command** pCommands;
    struct Mark** pMarks;
    struct Argument** pArguments;
    int commands;
    int maxCommands;
    int marks;
    int maxMarks;
    int arguments;
    int maxArguments;
};
struct Stack {
    int* storage;
    int top;
    int maxSize;
};

struct Code* newCode();
struct Stack* newStack();
int* newData();
struct Command* newCommand(int, int, struct Argument*, struct Mark*);
struct Mark* newMark(struct String*, int);
struct Argument* newArgument(struct String*, int, int);

void deleteCode(struct Code*);
void deleteStack(struct Stack*);
void deleteData(int*);
void deleteCommand(struct Command*);
void deleteMark(struct Mark*);
void deleteArgument(struct Argument*);

void addCommand(struct Code*, struct Command*);
void addMark(struct Code*, struct Mark*);
void addArgument(struct Code*, struct Argument*);

int getElement(struct Stack*, int);
void push(struct Stack*, int);
int pop(struct Stack*);

void printStack(struct Stack*);
void printCode(struct Code*);
void printData(int*);

