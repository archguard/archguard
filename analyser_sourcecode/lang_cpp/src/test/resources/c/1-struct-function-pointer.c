// https://stackoverflow.com/questions/12642830/can-i-define-a-function-inside-a-c-structure
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct point
{
    int x;
    int y;
    void (*print)(const struct point*);
};

void print_x(const struct point* p)
{
    printf("x=%d\n", p->x);
}

void print_y(const struct point* p)
{
    printf("y=%d\n", p->y);
}

int main(void)
{
    struct point p1 = { 2, 4, print_x };
    struct point p2 = { 7, 1, print_y };

    p1.print(&p1);
    p2.print(&p2);

    return 0;
}
