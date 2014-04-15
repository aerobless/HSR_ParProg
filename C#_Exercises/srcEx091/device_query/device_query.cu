#include "cuda_helper.cuh"

// See CUDA specification
int numberOfCoresPerMP(int majorVersion, int minorVersion) {
	if (majorVersion == 1) {
		return 8;
	} else if (majorVersion == 2 && minorVersion == 0) {
		return 32;
	} else if (majorVersion == 2 && minorVersion == 1) {
		return 48;
	} else if (majorVersion == 3) {
		return 192;
	} else {
		return 0; // unsupported version
	}
}

int main() {
	int deviceCount;
	handleCudaError(cudaGetDeviceCount(&deviceCount));
	if (deviceCount == 0) {
		printf("No CUDA devices");
	} 
	for (int deviceNumber = 0; deviceNumber < deviceCount; deviceNumber++) {
		cudaSetDevice(deviceNumber);
		
		cudaDeviceProp properties;
		handleCudaError(cudaGetDeviceProperties(&properties, deviceNumber));
		printf("CUDA Device: %d %s\n", deviceNumber, properties.name);

		int driverVersion, runtimeVersion;
		handleCudaError(cudaDriverGetVersion(&driverVersion));
		handleCudaError(cudaRuntimeGetVersion(&runtimeVersion));
		printf("  CUDA driver version			%i.%i\n", driverVersion / 1000, (driverVersion % 100) / 10);
        printf("  CUDA runtime version			%i.%i\n", runtimeVersion / 1000, (runtimeVersion % 100) / 10);
        printf("  CUDA capability major / minor		%i.%i\n", properties.major, properties.minor);
		printf("  Global memory				%i MB\n", (int)(properties.totalGlobalMem / 1024 / 1024));
		printf("  Cores per multiprocessor		%i\n", numberOfCoresPerMP(properties.major, properties.minor));
		printf("  Multiprocessor count			%i\n", properties.multiProcessorCount);
		printf("  Number of cores			%i\n", numberOfCoresPerMP(properties.major, properties.minor) * properties.multiProcessorCount);
		printf("  Clock rate				%i MHz\n", properties.clockRate / 1000);	
        printf("  Memory clock rate			%i MHz\n", properties.memoryClockRate / 1000);
        printf("  Memory bus width			%i bits\n",   properties.memoryBusWidth);
		printf("  L2 cache size				%lu bytes\n", properties.l2CacheSize);
        printf("  Constant memory			%lu bytes\n", properties.totalConstMem);
        printf("  Shared memory per block		%lu bytes\n", properties.sharedMemPerBlock);
        printf("  Registers per block			%i\n", properties.regsPerBlock);
        printf("  Warp size				%i\n", properties.warpSize);
        printf("  Maximum threads per multiprocessor	%i\n", properties.maxThreadsPerMultiProcessor);
        printf("  Maximum threads per block		%i\n", properties.maxThreadsPerBlock);
        printf("  Max thread dimension per block	(%i, %i, %i)\n", properties.maxThreadsDim[0], properties.maxThreadsDim[1], properties.maxThreadsDim[2]);
        printf("  Max grid size				(%i, %i, %i)\n", properties.maxGridSize[0], properties.maxGridSize[1], properties.maxGridSize[2]);
	}
	return 0;
}