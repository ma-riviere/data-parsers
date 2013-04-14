using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace bynaryXML
{
    public static class ByteHelpers
    {
        public static string ReadUTF16Z(this byte[] data, int offset)
        {
            int startIndex = offset;
            while (startIndex < data.Length && (int)BitConverter.ToUInt16(data, startIndex) != 0)
                startIndex += 2;
            if (startIndex == offset)
                return "";
            else
                return Encoding.Unicode.GetString(data, offset, startIndex - offset);
        }

        public static string ReadUTF16Z(this byte[] data, uint offset)
        {
            return ByteHelpers.ReadUTF16Z(data, (int)offset);
        }
    }
}
