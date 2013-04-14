using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace bynaryXML
{
   public static class BinaryXmlFileHelpers
    {
        public static int ReadPackedS32(this Stream stream)
        {
            byte num1 = StreamHelpers.ReadU8(stream);
            int num2 = 0;
            int num3 = 0;
            for (; (int)num1 >= 128; num1 = StreamHelpers.ReadU8(stream))
            {
                num2 |= ((int)num1 & (int)sbyte.MaxValue) << num3;
                num3 += 7;
            }
            return num2 | (int)num1 << num3;
        }

        public static string ReadTable(this byte[] data, int offset)
        {
            if (offset == 0)
                return "";
            else
                return ByteHelpers.ReadUTF16Z(data, 2 * offset);
        }
    }
}
