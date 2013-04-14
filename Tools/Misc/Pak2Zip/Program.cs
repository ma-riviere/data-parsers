using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Pak2Zip.IO;

namespace Pak2Zip
{
    class Program
    {
        public static List<PakBase> pbList = new List<PakBase>();
        public static short[] table = { 134, 250, 26, 28, 7, 189, 216, 100, 206, 238, 89, 136, 205, 169, 29, 6, 247, 61, 49, 88, 131, 161, 92, 126, 223, 166, 80, 158, 137, 168, 18, 210, 37, 73, 117, 226, 7, 15, 235, 1, 151, 74, 102, 53, 171, 50, 157, 167, 78, 162, 137, 98, 15, 85, 65, 197, 82, 16, 31, 71, 176, 160, 99, 166, 240, 28, 28, 76, 155, 60, 172, 226, 179, 78, 159, 241, 164, 145, 41, 130, 228, 118, 13, 141, 79, 163, 52, 74, 204, 28, 199, 24, 72, 142, 254, 24, 121, 8, 135, 40, 142, 36, 183, 107, 56, 242, 88, 1, 45, 168, 88, 14, 156, 84, 41, 207, 161, 174, 10, 210, 59, 74, 16, 248, 216, 25, 49, 125, 243, 174, 27, 144, 210, 47, 22, 199, 229, 59, 204, 239, 225, 225, 44, 134, 0, 221, 53, 103, 141, 37, 252, 237, 50, 31, 169, 26, 18, 109, 176, 247, 61, 182, 31, 232, 129, 77, 54, 231, 37, 48, 33, 144, 134, 48, 14, 238, 64, 190, 110, 218, 193, 58, 175, 242, 236, 40, 44, 241, 205, 68, 152, 114, 218, 205, 198, 217, 223, 247, 238, 136, 4, 225, 98, 0, 8, 14, 205, 22, 55, 171, 249, 245, 20, 170, 46, 0, 78, 248, 24, 65, 11, 217, 111, 155, 250, 173, 43, 84, 86, 46, 127, 44, 59, 106, 130, 161, 124, 124, 166, 143, 102, 94, 231, 207, 131, 185, 234, 252, 226, 49, 212, 16, 243, 244, 34, 236, 115, 20, 79, 148, 120, 121, 143, 30, 41, 234, 95, 33, 30, 8, 55, 184, 246, 154, 45, 197, 54, 52, 193, 151, 220, 117, 178, 173, 215, 227, 4, 167, 192, 201, 28, 26, 0, 233, 45, 111, 214, 141, 188, 115, 82, 192, 138, 182, 186, 44, 166, 125, 123, 111, 244, 71, 26, 114, 233, 178, 48, 125, 212, 211, 9, 156, 101, 176, 208, 23, 207, 252, 242, 255, 70, 210, 166, 67, 17, 118, 43, 229, 29, 229, 201, 71, 47, 75, 27, 221, 154, 253, 157, 32, 182, 67, 26, 100, 227, 104, 243, 33, 87, 104, 212, 4, 143, 195, 206, 175, 163, 171, 105, 163, 60, 52, 190, 31, 132, 168, 14, 116, 203, 183, 230, 177, 57, 141, 104, 0, 58, 155, 156, 177, 9, 28, 125, 82, 21, 18, 166, 176, 131, 211, 64, 71, 155, 228, 34, 227, 110, 48, 196, 252, 111, 79, 254, 159, 81, 20, 19, 87, 241, 235, 37, 247, 149, 76, 146, 182, 60, 208, 52, 121, 89, 51, 32, 190, 184, 191, 224, 10, 210, 119, 188, 67, 92, 125, 252, 225, 89, 0, 222, 90, 125, 68, 17, 172, 19, 242, 100, 132, 79, 93, 162, 196, 54, 215, 35, 250, 248, 209, 20, 141, 249, 221, 23, 29, 82, 65, 34, 245, 26, 66, 57, 254, 54, 213, 10, 16, 1, 210, 234, 18, 130, 90, 72, 210, 148, 149, 10, 247, 171, 112, 247, 242, 152, 137, 161, 104, 249, 225, 214, 225, 189, 146, 56, 69, 95, 25, 226, 234, 70, 118, 197, 195, 242, 180, 159, 112, 83, 9, 63, 184, 6, 58, 243, 70, 200, 106, 205, 10, 227, 240, 170, 52, 217, 114, 152, 52, 35, 209, 150, 140, 50, 50, 59, 0, 163, 158, 79, 237, 188, 151, 212, 74, 38, 21, 150, 29, 14, 54, 184, 238, 134, 69, 87, 4, 109, 43, 192, 219, 145, 10, 70, 206, 124, 31, 60, 58, 129, 148, 34, 38, 130, 109, 131, 189, 19, 45, 150, 145, 83, 108, 38, 12, 68, 254, 189, 238, 218, 204, 189, 82, 166, 17, 62, 16, 66, 32, 96, 235, 95, 91, 13, 124, 187, 128, 172, 47, 185, 249, 210, 74, 235, 84, 128, 96, 98, 133, 229, 26, 240, 48, 69, 183, 68, 130, 239, 58, 12, 224, 229, 148, 250, 253, 46, 217, 235, 141, 90, 194, 239, 57, 81, 113, 146, 250, 219, 239, 20, 136, 0, 255, 227, 246, 181, 52, 52, 64, 245, 187, 200, 211, 181, 189, 246, 207, 199, 177, 249, 24, 61, 162, 116, 239, 64, 188, 107, 57, 242, 200, 110, 0, 100, 120, 82, 136, 19, 244, 39, 116, 20, 143, 206, 52, 94, 249, 224, 109, 71, 252, 56, 109, 176, 3, 237, 108, 246, 104, 0, 172, 43, 254, 115, 44, 148, 158, 63, 23, 12, 51, 185, 143, 51, 52, 222, 5, 24, 225, 43, 185, 66, 63, 95, 162, 180, 30, 233, 69, 243, 56, 67, 187, 142, 176, 10, 148, 57, 238, 255, 154, 244, 45, 108, 75, 102, 177, 30, 15, 194, 24, 50, 225, 116, 255, 144, 148, 242, 56, 221, 86, 220, 120, 145, 150, 209, 4, 3, 9, 33, 57, 178, 212, 204, 42, 168, 171, 232, 153, 28, 231, 228, 67, 59, 88, 193, 89, 84, 232, 189, 156, 40, 198, 129, 252, 173, 51, 79, 36, 22, 160, 71, 209, 76, 77, 57, 122, 193, 247, 29, 4, 207, 231, 174, 23, 113, 217, 55, 220, 156, 10, 14, 157, 14, 4, 215, 36, 193, 80, 12, 73, 227, 188, 202, 152, 137, 85, 134, 115, 241, 195, 141, 143, 153, 52, 247, 75, 231, 105, 10, 176, 193, 47, 133, 151, 191, 195, 253, 208, 98, 117, 177, 173, 243, 4, 243, 243, 119, 6, 170, 119, 90, 231, 235, 103, 63, 181, 64, 161, 156, 83, 150, 253, 133, 83, 110, 237, 82, 5, 59, 110, 137, 239, 149, 152, 182, 102, 52, 208, 138, 63, 68, 234, 6, 134, 19, 57, 239, 32, 173, 228, 115, 44, 97, 119, 16, 61, 185, 11, 194, 12, 253, 242, 153, 216, 177, 87, 131, 27, 36, 166, 160, 171, 151, 62, 229, 9, 7, 63, 67, 237, 18, 227, 54, 206, 22, 88, 242, 120, 0, 99, 247, 103, 220, 217, 95, 13, 170, 62, 154, 163, 131, 114, 254, 186, 146, 233, 212, 34, 240, 56, 56, 97, 226, 121, 155, 94, 138, 98, 39, 89, 132, 113, 192, 235, 149, 40, 13, 52, 203, 171, 37, 198, 59, 188, 82, 165, 202, 107, 147, 202, 35, 109, 53, 135, 65, 135, 62, 72, 185, 223, 14, 253, 48, 184, 209, 184, 16, 104, 61, 188, 9, 4, 49, 148, 92, 145, 175, 108 };
        static void Main(string[] args)
        {
            if (args.Length == 1)
            {
                FileStream fs = null;
                try
                {
                    fs = new FileStream(args[0], FileMode.Open);
                }
                catch
                {
                    Console.WriteLine("Can not access file : " + args[0]);
                    Console.Read();
                    Environment.Exit(0);
                }
                _BinaryReader pak = new _BinaryReader(fs, true);
                var pathParts = fs.Name.Split('\\');
                readPakFile(pak, pathParts[pathParts.Length - 1]);
            }
            else
            {
                error(false, "");
            }
        }

        public static void error(bool fileWrong, string fileName)
        {
            if (!fileWrong)
            {
                Console.WriteLine("-= Pak2Zip Comverter v1.0 by pixfid =-");
                Console.WriteLine(" Usage Drag & Drop *.pak file on app icon.");
            }
            else
            {
                Console.WriteLine("Wrong input file : " + fileName);
            }
            Console.Read();
            Environment.Exit(0);
        }

        public static void readPakFile(_BinaryReader pak, string fileName)
        {
            while (pak.BaseStream.CanRead)
            {
                if (pak.BaseStream.Length - pak.BaseStream.Position == 0)
                    break;

                if (pak.ReadH() == -19281)
                {
                    short type = pak.ReadH();
                    switch (type)
                    {
                        case -1028: /** ReadFile **/
                            PakFile file = new PakFile(pak);
                            pak.ReadBytes((int)file.getOtherDataSize());
                            file.setType(PakDataType.FILE);
                            pbList.Add(file);
                            break;
                        case -514: /** ReadDir **/
                            PakDir dir = new PakDir(pak);
                            pak.ReadBytes((int)dir.getOtherDataSize());
                            dir.setType(PakDataType.DIR);
                            pbList.Add(dir);
                            break;
                        case -1542:/** ReadEOF **/
                            PakEnd end = new PakEnd(pak);
                            end.setType(PakDataType.END);
                            pbList.Add(end);
                            break;
                    }
                }
                else
                {
                    error(true, fileName);
                }
            }

            if (pbList.Count == 0)
            {
                error(true, fileName);
            }
            var outFileName = fileName.Split('.')[0] + ".zip";

            BinaryWriter wr = new BinaryWriter(new FileStream(outFileName, FileMode.Create));

            foreach (PakBase entry in pbList)
            {
                int cDataSize = 0;
                /** ZIP HEADER **/
                wr.Write((short)19280);
                /** Write File Entry **/
                if (entry.type == PakDataType.FILE)
                {
                    PakFile file = (PakFile)entry;
                    wr.Write((short)1027);
                    file.writeFile(wr);
                    pak.BaseStream.Position = wr.BaseStream.Position;
                    if (file.extraFieldLength > 0)
                    {
                        wr.Write(pak.ReadBytes(file.extraFieldLength));
                    }
                    cDataSize = file.compressedSize;
                    byte[] data = pak.ReadBytes(cDataSize >= 32 ? 32 : cDataSize);
                    int _val = cDataSize & 0x3FF;
                    for (int s = 0; s < data.Length; s++)
                    {
                        int l = s;
                        byte[] _bVal = data;
                        _bVal[s] = (byte)(_bVal[s] ^ table[(_val + s)]);
                    }
                    wr.Write(data);
                    cDataSize -= data.Length;

                }
                /** Write Dir Entry **/
                if (entry.type == PakDataType.DIR)
                {
                    PakDir dir = (PakDir)entry;
                    wr.Write((short)513);
                    dir.writeDir(wr);
                    if ((cDataSize = dir.getOtherDataSize()) > 0)
                    {
                        pak.BaseStream.Position = wr.BaseStream.Position;
                    }
                }
                /** Write EOF Entry **/
                if (entry.type == PakDataType.END)
                {
                    PakEnd end = (PakEnd)entry;
                    wr.Write((short)1541);
                    end.writeEnd(wr);
                }
                if (cDataSize != 0)
                {
                    wr.Write(pak.ReadBytes(cDataSize));
                }
                wr.Flush();
            }
            wr.Close();
        }
    }
}
