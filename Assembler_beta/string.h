#ifndef ASSEMBLER_BETA_STRING_H
#define ASSEMBLER_BETA_STRING_H

#endif

struct String {
    char* s;
    int length;
    int maxLength;
};

struct String* newString();
void deleteString(struct String*);
int length(char*);
void copyString(struct String*, struct String*);
void addCharacter(struct String*, char);
int findCharacter(struct String*, char);
void deleteCharacter(struct String*, int);
int equals(struct String*, struct String*);
int stringToInt(struct String*);