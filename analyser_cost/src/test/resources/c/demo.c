#include <stdio.h>
int main() {
	int rollNo[10];

	for(int i=0;i<10;i++)
	    scanf("%d",&rollNo[i]);

	for(int i=0;i<10;i++)
	    printf("%d ",rollNo[i]);

	return 0;
}