#include "cuda_helper.cuh"

__global__
	void VectorAddKernel(float *a, float *b, float *c, int numElements){
		//kernel definition
	int i = blockIdx.x * blockDim.x + threadIdx.x;
	if(i < numElements){
		c[i] = a[i] + b[i];
	}
}

void cudaVectorAdd(float *A, float *B, float *C, int numElements) {
	clock_t start = clock();
	// TODO: Implement a parallel vector addition on CUDA
	size_t size = numElements*sizeof(float);
	float *d_A, *d_B, *d_C;

	cudaMalloc(&d_A, size);
	cudaMalloc(&d_B, size);
	cudaMalloc(&d_C, size);

	cudaMemcpy(d_A, A, size, cudaMemcpyHostToDevice);
	cudaMemcpy(d_B, B, size, cudaMemcpyHostToDevice);

	int blockDim = 512;
	int gridDim = (numElements+blockDim-1)/blockDim;
	VectorAddKernel<<<gridDim,blockDim>>>(d_A,d_B,d_C,numElements);

	cudaMemcpy(C, d_C, size, cudaMemcpyDeviceToHost);

	cudaFree(d_A); cudaFree(d_B); cudaFree(d_C);

	float diff = float(clock() - start) / CLOCKS_PER_SEC;
	printf("CUDA: %.3lf seconds\n", diff);
}

void fillRandomArray(float *a, int numElements) {
	for (int i = 0; i < numElements; i++) {
		a[i] = rand() / (float)RAND_MAX;
	}
}

void verifyResults(float *a, float *b, float *c, int numElements) {
	for (int i = 0; i < numElements; i++) {
        if (fabs(a[i] + b[i] - c[i]) > 1e-5) {
            fprintf(stderr, "Result verification failed at element %d!\n", i);
            exit(EXIT_FAILURE);
        }
    }
}

void sequentialVectorAdd(float *a, float *b, float *c, int numElements) {
	clock_t start = clock();

	for (int i = 0; i < numElements; i++) {
		c[i] = a[i] + b[i];
	}

	float diff = float(clock() - start) / CLOCKS_PER_SEC;
	printf("Sequential: %.3lf seconds\n", diff);
}

int main() {
	int N = 10000000;
	size_t size = N * sizeof(float);

	float *h_a = (float *)malloc(size);
	handleAllocationError(h_a);
	fillRandomArray(h_a, N);
	
	float *h_b = (float *)malloc(size);
	handleAllocationError(h_b);
	fillRandomArray(h_b, N);
	
	float *h_c = (float *)malloc(size);
	handleAllocationError(h_c);

	cudaVectorAdd(h_a, h_b, h_c, N);
	verifyResults(h_a, h_b, h_c, N);

	sequentialVectorAdd(h_a, h_b, h_c, N);

	free(h_a);
	free(h_b);
	free(h_c);

	return 0;
}
