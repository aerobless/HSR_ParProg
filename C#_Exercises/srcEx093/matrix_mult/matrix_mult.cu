#include "cuda_helper.cuh"

// C = A * B

#define C_ROWS 1000
#define C_COLS 2000
#define A_COLS 1500

#define A_ROWS C_ROWS 
#define B_ROWS A_COLS 
#define B_COLS C_COLS 

void cudaMatrixMult(float *A, float *B, float *C) {
	clock_t start = clock();
	
	// TODO: Implement parallel matrix multiplication on CUDA (simple version)

	float diff = float(clock() - start) / CLOCKS_PER_SEC;
	printf("CUDA: %.3lf seconds\n", diff);
}

void fillRandomArray(float *A, int numElements) {
	for (int i = 0; i < numElements; i++) {
		A[i] = rand() / (float)RAND_MAX;
	}
}

void verifyResults(float *A, float *B, float *C) {
	for (int row = 0; row < C_ROWS; row++) {
		for (int col = 0; col < C_COLS; col++) {
			float sum = 0.0;
			for (int k = 0; k < A_COLS; k++) {
				sum += A[row * A_COLS + k] * B[k * B_COLS + col];
			}
			if (fabs(C[row * C_COLS + col] - sum) > 1e-3) {
				fprintf(stderr, "Result verification failed at element %d: %f vs. %f!\n", row, C[row * C_COLS + col], sum);
				exit(EXIT_FAILURE);
			}
		}
	}
}

void sequentialMatrixMult(float *A, float *B, float *C) {
	clock_t start = clock();

	for (int row = 0; row < C_ROWS; row++) {
		for (int col = 0; col < C_COLS; col++) {
			float sum = 0.0;
			for (int k = 0; k < A_COLS; k++) {
				sum += A[row * A_COLS + k] * B[k * B_COLS + col];
			}
			C[row * C_COLS + col] = sum;
		}
	}

	float diff = float(clock() - start) / CLOCKS_PER_SEC;
	printf("Sequential: %.3lf seconds\n", diff);
}

int main() {
	int nofElemA = A_ROWS * A_COLS;
	float *h_A = (float *)malloc(nofElemA * sizeof(float));
	handleAllocationError(h_A);
	fillRandomArray(h_A, nofElemA);

	int nofElemB = B_ROWS * B_COLS;
	float *h_B = (float *)malloc(nofElemB * sizeof(float));
	handleAllocationError(h_B);
	fillRandomArray(h_B, nofElemB);
	
	int nofElemC = C_ROWS * C_COLS;
	float *h_C = (float *)malloc(nofElemC * sizeof(float));
	handleAllocationError(h_C);

	cudaMatrixMult(h_A, h_B, h_C);

	verifyResults(h_A, h_B, h_C);
	
	sequentialMatrixMult(h_A, h_B, h_C);

	free(h_A);
	free(h_B);
	free(h_C);

	return 0;
}
