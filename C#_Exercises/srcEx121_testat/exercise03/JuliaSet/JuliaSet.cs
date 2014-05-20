using System;
using MPI;
using System.Diagnostics;
using System.Numerics;

namespace JuliaSet {
  public class JuliaSet {
    const int MessageTag = 1;
    private const string OutputFile = "juliaset.jpg";
    private const int ImageWidth = 4 * 1024;
    private const int ImageHeight = 4 * 1024;
    private const double ImageScale = 1.5;
    
    public static void Main(string[] args) {
      // TODO: Parallelize with MPI
      // Perform a row-wise partitioning of the image where each MPI process computes an independent partition.
      // Processes send their computed partitions to a common process that combines the partitions to the final image.
        using (new MPI.Environment(ref args))
        {
            int rank = Communicator.world.Rank;
            int size = Communicator.world.Size;

            if (rank == 0)
            {
                Stopwatch computing = Stopwatch.StartNew();
                int[,] pixels = new int[ImageHeight, ImageWidth];

                
                for (int target = 1; target < size; target++)
                {
                    int widthEnd = ImageWidth / size * target;
                    int widthStart = widthEnd - (ImageWidth / size);

                    int heightEnd = ImageHeight / size * target;
                    int heightStart = heightEnd - (ImageHeight / size);

                    Communicator.world.Send(new int[heightEnd, heightStart, widthEnd, widthStart], target, MessageTag);
                }

                for (int target = 1; target < size; target++)
                {
                    int[,] computedPixels;
                    Communicator.world.Receive(target, 2, out computedPixels);
                    BitmapHelper.WriteBitmap(computedPixels, OutputFile);
                }
                Console.WriteLine("Computed {0} ms", computing.ElapsedMilliseconds);

              //  BitmapHelper.WriteBitmap(pixels, OutputFile);
            }
            else
            {
                int[] value;
                Communicator.world.Receive(0, MessageTag, out value);
                int[,] pixels = new int[value[1], value[3]];
                for (int y = 0; y < value[0]; y++)
                {
                    for (int x = 0; x < value[2]; x++)
                    {
                        pixels[y, x] = JuliaValue(x, y);
                    }
                }
                Communicator.world.Send(pixels, 0, 2);
            }
        }
    }

    private static int JuliaValue(int x, int y) {
	    double jx = ImageScale * (double)(ImageWidth / 2 - x) / (ImageWidth / 2);
	    double jy = ImageScale * (double)(ImageHeight  / 2 - y) / (ImageHeight  / 2);

	    Complex c = new Complex(-.8f, .156f);
	    Complex a = new Complex(jx, jy);

	    int i;
	    for (i = 0; i < 200 && a.Magnitude * a.Magnitude <= 1000; i++) {
		    a = a * a + c;
	    }
	    return i;
    }
  }
}
