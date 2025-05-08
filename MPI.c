#include <mpi.h> 
#include <stdio.h> 
#include <stdlib.h>

int main(int argc, char* argv[]) { int rank, size;
int N = 16; // Total number of elements int array[N];
int local_sum = 0, total_sum = 0;


MPI_Init(Cargc, Cargv);
MPI_Comm_rank(MPI_COMM_WORLD, Crank); 
MPI_Comm_size(MPI_COMM_WORLD, Csize);

int elements_per_proc = N / size; 
int remaining_elements = N % size;

if (rank == 0) {
for (int i = 0; i < N; i++) { 
    array[i] = i + 1;
}
printf("Original array: "); 
for (int i = 0; i < N; i++) { 
    printf("%d ", array[i]);
}
printf("\n");
}


int local_size = elements_per_proc + (rank < remaining_elements ? 1 : 0);

int* local_array = (int*)malloc(local_size * sizeof(int));


int displs[size], send_counts[size]; if (rank == 0) {
int offset = 0;
for (int i = 0; i < size; i++) {
send_counts[i] = elements_per_proc + (i < remaining_elements ? 1 : 0); displs[i] = offset;
offset += send_counts[i];
}
}


MPI_Scatterv(array, send_counts, displs, MPI_INT, local_array, local_size, MPI_INT, 0, MPI_COMM_WORLD);
for (int i = 0; i < local_size; i++) { 
    local_sum += local_array[i];
}
free(local_array);
printf("Processor %d calculated local sum: %d\n", rank, local_sum);


MPI_Reduce(Clocal_sum, Ctotal_sum, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);


if (rank == 0) {
printf("Total sum of array: %d\n", total_sum);
}


MPI_Finalize(); return 0;
}
