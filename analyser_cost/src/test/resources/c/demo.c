#include <stdio.h>

int main()
{
  int marks[20], i, n, sum = 0, avg;

  printf("Enter the number of elements: ");
  scanf("%d", &n);

  for (i=0; i < n; ++i) {
    printf("Enter number%d: ",i+1);
    scanf("%d", &marks[i]);
    sum += marks[i];
  }

  avg = sum / n;
  printf("Avg = %d", avg);
  return 0;

}