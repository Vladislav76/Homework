#include <stdio.h>
#include <stdlib.h>
#include "memory.h"
#include "string.h"

int startLength = 16;

struct String* newString() {
    struct String* tempString = (struct String*) calloc(1, sizeof(struct String));
    checkAllocate(tempString);
    tempString -> s = (char*) calloc ((size_t) startLength + 1, sizeof(char));
    checkAllocate(tempString -> s);
    tempString -> maxLength = startLength;
    tempString -> s[0] = '\0';
    return tempString;
}

void deleteString(struct String* string) {
    free(string -> s);
    free(string);
}

int length(char* string) {
    int i = 0;
    while (string[i] != '\0') {
        i++;
    }
    return i;
}

void copyString(struct String* string1, struct String* string2) {
    if (string1 -> length < string2 -> length) {
        string1 -> maxLength = string2 -> length + 1;
        string1 -> s = (char*) realloc(string1 -> s, (string1 -> maxLength) * sizeof(char));
        checkAllocate(string1 -> s);
        string1 -> length = string2 -> length;
    }
    int i = 0;
    while (string2 -> s[i] != '\0') {
        string1 -> s[i] = string2 -> s[i];
        i++;
    }
    string1 -> s[i] = '\0';
}

void addCharacter(struct String* string, char c) {
    if (string -> length == string -> maxLength) {
        string -> maxLength = (int) (string -> maxLength * 1.4);
        string -> s = (char*) realloc(string -> s, (string -> maxLength + 1) * sizeof(char));
        checkAllocate(string -> s);
    }
    string -> s[string -> length] = c;
    string -> length++;
    string -> s[string -> length] = '\0';
}

int findCharacter(struct String* string, char c) {
    int i = 0;
    while (string -> s[i] != '\0' && string -> s[i] != c) {
        i++;
    }
    if (i == string -> length) {
        i = -1;
    }
    return i;
}

void deleteCharacter(struct String* string, int position) {
    if (position < string -> length) {
        for(int i = position; i < string -> length; i++) {
            string -> s[i] = string -> s[i + 1];
        }
        string -> length--;
    }
    else {
        printf("Error: Length of string is less than number of removable symbol!\n");
    }
}

int equals(struct String* string1, struct String* string2) {
    int i = 0;
    int max = string1 -> length;
    if (string2 -> length > max) {
        max = string2 -> length;
    }
    while (i < max) {
        if (string1 -> s[i] == '\0') {
            if (string2 -> s[i] == '\0') {
                return 0;
            }
            else {
                return 2;
            }
        }
        else if (string2 -> s[i] == '\0') {
            if (string1 -> s[i] == '\0') {
                return 0;
            }
            else {
                return 1;
            }
        }
        if (string1 -> s[i] < string2 -> s[i]) {
           return 2;
        }
        else if (string2 -> s[i] < string1 -> s[i]) {
            return 1;
        }
        i++;
    }
    return 0;
}

int stringToInt(struct String* string) {
    int size = string -> length;
    int x = 0;
    int pow = 1;
    int i = 0;
    int sign = 1;
    if (string -> s[0] == '-') {
        sign = -1;
        i++;
    }
    else if (string -> s[0] == '+') {
        i++;
    }
    int k = size - 1;
    for(; i < size; i++, k--) {
        x += (string -> s[k] - '0') * pow;
        pow *= 10;
    }
    return x * sign;
}

