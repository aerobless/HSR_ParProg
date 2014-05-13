using System;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Imaging;

namespace JuliaSet {
  public class BitmapHelper {
    // No changes needed here for MPI parallelization
    public static void WriteBitmap(int[,] pixels, string outputFile) {
      Stopwatch writing = Stopwatch.StartNew();
      int height = pixels.GetLength(0);
      int width = pixels.GetLength(1);
      Bitmap outputBitmap = new Bitmap(width, height, PixelFormat.Format32bppArgb);
      BitmapData outputData = outputBitmap.LockBits(new Rectangle(0, 0, width, height), ImageLockMode.WriteOnly, outputBitmap.PixelFormat);
      const int PixelSize = 4;
      unsafe {
        for (int y = 0; y < height; y++) {
          byte* row = (byte*)outputData.Scan0 + y * outputData.Stride;
          for (int x = 0; x < width; x++) {
            int value = pixels[y, x];
            row[x * PixelSize + 0] = (byte)(value % 63); // blue
            row[x * PixelSize + 1] = (byte)(value % 127); // green
            row[x * PixelSize + 2] = (byte)(value % 255); // red
            row[x * PixelSize + 3] = 255; // alpha
          }
        }
      }
      outputBitmap.UnlockBits(outputData);
      outputBitmap.Save(outputFile);
      Console.WriteLine("Written {0} ms", writing.ElapsedMilliseconds);
    }
  }
}
