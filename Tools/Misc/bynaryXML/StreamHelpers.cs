using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace bynaryXML
{
    public static class StreamHelpers
    {
        internal static bool ShouldSwap(bool littleEndian)
        {
            return littleEndian && !BitConverter.IsLittleEndian || !littleEndian && BitConverter.IsLittleEndian;
        }

        public static MemoryStream ReadToMemoryStream(this Stream stream, long size)
        {
            MemoryStream memoryStream = new MemoryStream();
            long val1 = size;
            byte[] buffer = new byte[4096];
            while (val1 > 0L)
            {
                int count = (int)Math.Min(val1, 4096L);
                stream.Read(buffer, 0, count);
                memoryStream.Write(buffer, 0, count);
                val1 -= (long)count;
            }
            memoryStream.Seek(0L, SeekOrigin.Begin);
            return memoryStream;
        }

        public static void WriteFromStream(this Stream stream, Stream input, long size)
        {
            long val1 = size;
            byte[] buffer = new byte[4096];
            while (val1 > 0L)
            {
                int count = (int)Math.Min(val1, 4096L);
                input.Read(buffer, 0, count);
                stream.Write(buffer, 0, count);
                val1 -= (long)count;
            }
        }

        public static int ReadAligned(this Stream stream, byte[] buffer, int offset, int size, int align)
        {
            if (size == 0)
                return 0;
            int num1 = stream.Read(buffer, offset, size);
            int num2 = size % align;
            if (num2 > 0)
                stream.Seek((long)(align - num2), SeekOrigin.Current);
            return num1;
        }

        public static void WriteAligned(this Stream stream, byte[] buffer, int offset, int size, int align)
        {
            if (size == 0)
                return;
            stream.Write(buffer, offset, size);
            int num = size % align;
            if (num <= 0)
                return;
            byte[] buffer1 = new byte[align - num];
            stream.Write(buffer1, 0, align - num);
        }

        public static bool ReadBoolean(this Stream stream)
        {
            return (int)StreamHelpers.ReadU8(stream) > 0;
        }

        public static void WriteBoolean(this Stream stream, bool value)
        {
            StreamHelpers.WriteU8(stream, value ? (byte)1 : (byte)0);
        }

        public static float ReadF32(this Stream stream)
        {
            return StreamHelpers.ReadF32(stream, true);
        }

        public static float ReadF32(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[4];
            Debug.Assert(stream.Read(buffer, 0, 4) == 4);
            if (StreamHelpers.ShouldSwap(littleEndian))
                return BitConverter.ToSingle(BitConverter.GetBytes(NumberHelpers.Swap(BitConverter.ToInt32(buffer, 0))), 0);
            else
                return BitConverter.ToSingle(buffer, 0);
        }

        public static void WriteF32(this Stream stream, float value)
        {
            StreamHelpers.WriteF32(stream, value, true);
        }

        public static void WriteF32(this Stream stream, float value, bool littleEndian)
        {
            byte[] buffer = !StreamHelpers.ShouldSwap(littleEndian) ? BitConverter.GetBytes(value) : BitConverter.GetBytes(NumberHelpers.Swap(BitConverter.ToInt32(BitConverter.GetBytes(value), 0)));
            Debug.Assert(buffer.Length == 4);
            stream.Write(buffer, 0, 4);
        }

        public static double ReadF64(this Stream stream)
        {
            return StreamHelpers.ReadF64(stream, true);
        }

        public static double ReadF64(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[8];
            Debug.Assert(stream.Read(buffer, 0, 8) == 8);
            if (StreamHelpers.ShouldSwap(littleEndian))
                return BitConverter.Int64BitsToDouble(NumberHelpers.Swap(BitConverter.ToInt64(buffer, 0)));
            else
                return BitConverter.ToDouble(buffer, 0);
        }

        public static void WriteF64(this Stream stream, double value)
        {
            StreamHelpers.WriteF64(stream, value, true);
        }

        public static void WriteF64(this Stream stream, double value, bool littleEndian)
        {
            byte[] buffer = !StreamHelpers.ShouldSwap(littleEndian) ? BitConverter.GetBytes(value) : BitConverter.GetBytes(NumberHelpers.Swap(BitConverter.DoubleToInt64Bits(value)));
            Debug.Assert(buffer.Length == 8);
            stream.Write(buffer, 0, 8);
        }

        public static short ReadS16(this Stream stream)
        {
            return StreamHelpers.ReadS16(stream, true);
        }

        public static short ReadS16(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[2];
            Debug.Assert(stream.Read(buffer, 0, 2) == 2);
            short num = BitConverter.ToInt16(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteS16(this Stream stream, short value)
        {
            StreamHelpers.WriteS16(stream, value, true);
        }

        public static void WriteS16(this Stream stream, short value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 2);
            stream.Write(bytes, 0, 2);
        }

        public static int ReadS32(this Stream stream)
        {
            return StreamHelpers.ReadS32(stream, true);
        }

        public static int ReadS32(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[4];
            Debug.Assert(stream.Read(buffer, 0, 4) == 4);
            int num = BitConverter.ToInt32(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteS32(this Stream stream, int value)
        {
            StreamHelpers.WriteS32(stream, value, true);
        }

        public static void WriteS32(this Stream stream, int value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 4);
            stream.Write(bytes, 0, 4);
        }

        public static long ReadS64(this Stream stream)
        {
            return StreamHelpers.ReadS64(stream, true);
        }

        public static long ReadS64(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[8];
            Debug.Assert(stream.Read(buffer, 0, 8) == 8);
            long num = BitConverter.ToInt64(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteS64(this Stream stream, long value)
        {
            StreamHelpers.WriteS64(stream, value, true);
        }

        public static void WriteS64(this Stream stream, long value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 8);
            stream.Write(bytes, 0, 8);
        }

        public static char ReadS8(this Stream stream)
        {
            return (char)stream.ReadByte();
        }

        public static void WriteS8(this Stream stream, char value)
        {
            stream.WriteByte((byte)value);
        }

        public static ushort ReadU16(this Stream stream)
        {
            return StreamHelpers.ReadU16(stream, true);
        }

        public static ushort ReadU16(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[2];
            Debug.Assert(stream.Read(buffer, 0, 2) == 2);
            ushort num = BitConverter.ToUInt16(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteU16(this Stream stream, ushort value)
        {
            StreamHelpers.WriteU16(stream, value, true);
        }

        public static void WriteU16(this Stream stream, ushort value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 2);
            stream.Write(bytes, 0, 2);
        }

        public static uint ReadU24(this Stream stream)
        {
            return StreamHelpers.ReadU24(stream, true);
        }

        public static uint ReadU24(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[4];
            Debug.Assert(stream.Read(buffer, 0, 3) == 3);
            uint num = BitConverter.ToUInt32(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap24(num);
            return num & 16777215U;
        }

        public static void WriteU24(this Stream stream, uint value)
        {
            StreamHelpers.WriteU24(stream, value, true);
        }

        public static void WriteU24(this Stream stream, uint value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap24(value);
            value &= 16777215U;
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 4);
            stream.Write(bytes, 0, 3);
        }

        public static uint ReadU32(this Stream stream)
        {
            return StreamHelpers.ReadU32(stream, true);
        }

        public static uint ReadU32(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[4];
            Debug.Assert(stream.Read(buffer, 0, 4) == 4);
            uint num = BitConverter.ToUInt32(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteU32(this Stream stream, uint value)
        {
            StreamHelpers.WriteU32(stream, value, true);
        }

        public static void WriteU32(this Stream stream, uint value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 4);
            stream.Write(bytes, 0, 4);
        }

        public static ulong ReadU64(this Stream stream)
        {
            return StreamHelpers.ReadU64(stream, true);
        }

        public static ulong ReadU64(this Stream stream, bool littleEndian)
        {
            byte[] buffer = new byte[8];
            Debug.Assert(stream.Read(buffer, 0, 8) == 8);
            ulong num = BitConverter.ToUInt64(buffer, 0);
            if (StreamHelpers.ShouldSwap(littleEndian))
                num = NumberHelpers.Swap(num);
            return num;
        }

        public static void WriteU64(this Stream stream, ulong value)
        {
            StreamHelpers.WriteU64(stream, value, true);
        }

        public static void WriteU64(this Stream stream, ulong value, bool littleEndian)
        {
            if (StreamHelpers.ShouldSwap(littleEndian))
                value = NumberHelpers.Swap(value);
            byte[] bytes = BitConverter.GetBytes(value);
            Debug.Assert(bytes.Length == 8);
            stream.Write(bytes, 0, 8);
        }

        public static byte ReadU8(this Stream stream)
        {
            return (byte)stream.ReadByte();
        }

        public static void WriteU8(this Stream stream, byte value)
        {
            stream.WriteByte(value);
        }

        public static string ReadASCII(this Stream stream, uint size, bool trailingNull)
        {
            byte[] numArray = new byte[size];
            stream.Read(numArray, 0, numArray.Length);
            int length = numArray.Length;
            if (trailingNull)
            {
                while (length > 0 && (int)numArray[length - 1] == 0)
                    --length;
            }
            return Encoding.ASCII.GetString(numArray, 0, length);
        }

        public static string ReadASCII(this Stream stream, uint size)
        {
            return StreamHelpers.ReadASCII(stream, size, false);
        }

        public static string ReadASCIIZ(this Stream stream)
        {
            int index = 0;
            byte[] array = new byte[64];
            while (true)
            {
                if (index >= array.Length)
                {
                    if (array.Length < 4096)
                        Array.Resize<byte>(ref array, array.Length + 64);
                    else
                        break;
                }
                stream.Read(array, index, 1);
                if ((int)array[index] != 0)
                    ++index;
                else
                    goto label_7;
            }
            throw new InvalidOperationException();
        label_7:
            if (index == 0)
                return "";
            else
                return Encoding.ASCII.GetString(array, 0, index);
        }

        public static void WriteASCII(this Stream stream, string value)
        {
            byte[] bytes = Encoding.ASCII.GetBytes(value);
            stream.Write(bytes, 0, bytes.Length);
        }

        public static void WriteASCIIZ(this Stream stream, string value)
        {
            byte[] bytes = Encoding.ASCII.GetBytes(value);
            stream.Write(bytes, 0, bytes.Length);
            stream.WriteByte((byte)0);
        }

        public static string ReadUTF16(this Stream stream, uint size)
        {
            return StreamHelpers.ReadUTF16(stream, size, true);
        }

        public static string ReadUTF16(this Stream stream, uint size, bool littleEndian)
        {
            byte[] numArray = new byte[size];
            stream.Read(numArray, 0, numArray.Length);
            if (littleEndian)
                return Encoding.Unicode.GetString(numArray);
            else
                return Encoding.BigEndianUnicode.GetString(numArray);
        }

        public static string ReadUTF16Z(this Stream stream)
        {
            return StreamHelpers.ReadUTF16Z(stream, true);
        }

        public static string ReadUTF16Z(this Stream stream, bool littleEndian)
        {
            int num = 0;
            byte[] array = new byte[128];
            while (true)
            {
                stream.Read(array, num, 2);
                if ((int)BitConverter.ToUInt16(array, num) != 0)
                {
                    if (num >= array.Length)
                    {
                        if (array.Length < 8192)
                            Array.Resize<byte>(ref array, array.Length + 128);
                        else
                            break;
                    }
                    num += 2;
                }
                else
                    goto label_7;
            }
            throw new InvalidOperationException();
        label_7:
            if (num == 0)
                return "";
            if (littleEndian)
                return Encoding.Unicode.GetString(array, 0, num);
            else
                return Encoding.BigEndianUnicode.GetString(array, 0, num);
        }

        public static void WriteUTF16(this Stream stream, string value)
        {
            StreamHelpers.WriteUTF16(stream, value, true);
        }

        public static void WriteUTF16(this Stream stream, string value, bool littleEndian)
        {
            byte[] buffer = !littleEndian ? Encoding.BigEndianUnicode.GetBytes(value) : Encoding.Unicode.GetBytes(value);
            stream.Write(buffer, 0, buffer.Length);
        }

        public static void WriteUTF16Z(this Stream stream, string value)
        {
            StreamHelpers.WriteUTF16Z(stream, value, true);
        }

        public static void WriteUTF16Z(this Stream stream, string value, bool littleEndian)
        {
            byte[] buffer = !littleEndian ? Encoding.BigEndianUnicode.GetBytes(value) : Encoding.Unicode.GetBytes(value);
            stream.Write(buffer, 0, buffer.Length);
            StreamHelpers.WriteU16(stream, (ushort)0);
        }

        public static string ReadUTF8(this Stream stream, uint size)
        {
            byte[] numArray = new byte[size];
            stream.Read(numArray, 0, numArray.Length);
            return Encoding.UTF8.GetString(numArray);
        }

        public static string ReadUTF8Z(this Stream stream)
        {
            int index = 0;
            byte[] array = new byte[64];
            while (true)
            {
                stream.Read(array, index, 1);
                if ((int)array[index] != 0)
                {
                    if (index >= array.Length)
                    {
                        if (array.Length < 4096)
                            Array.Resize<byte>(ref array, array.Length + 64);
                        else
                            break;
                    }
                    ++index;
                }
                else
                    goto label_7;
            }
            throw new InvalidOperationException();
        label_7:
            if (index == 0)
                return "";
            else
                return Encoding.UTF8.GetString(array, 0, index);
        }

        public static void WriteUTF8(this Stream stream, string value)
        {
            byte[] bytes = Encoding.UTF8.GetBytes(value);
            stream.Write(bytes, 0, bytes.Length);
        }

        public static void WriteUTF8Z(this Stream stream, string value)
        {
            byte[] bytes = Encoding.UTF8.GetBytes(value);
            stream.Write(bytes, 0, bytes.Length);
            stream.WriteByte((byte)0);
        }

        public static T ReadStructure<T>(this Stream stream)
        {
            int count = Marshal.SizeOf(typeof(T));
            byte[] buffer = new byte[count];
            if (stream.Read(buffer, 0, count) != count)
                throw new Exception();
            GCHandle gcHandle = GCHandle.Alloc((object)buffer, GCHandleType.Pinned);
            T obj = (T)Marshal.PtrToStructure(gcHandle.AddrOfPinnedObject(), typeof(T));
            gcHandle.Free();
            return obj;
        }

        public static T ReadStructure<T>(this Stream stream, int size)
        {
            int length = Marshal.SizeOf(typeof(T));
            if (size > length)
                throw new Exception();
            byte[] buffer = new byte[length];
            if (stream.Read(buffer, 0, size) != size)
                throw new Exception();
            GCHandle gcHandle = GCHandle.Alloc((object)buffer, GCHandleType.Pinned);
            T obj = (T)Marshal.PtrToStructure(gcHandle.AddrOfPinnedObject(), typeof(T));
            gcHandle.Free();
            return obj;
        }

        public static void WriteStructure<T>(this Stream stream, T structure)
        {
            byte[] buffer = new byte[Marshal.SizeOf(typeof(T))];
            GCHandle gcHandle = GCHandle.Alloc((object)buffer, GCHandleType.Pinned);
            Marshal.StructureToPtr((object)structure, gcHandle.AddrOfPinnedObject(), false);
            gcHandle.Free();
            stream.Write(buffer, 0, buffer.Length);
        }
    }
}
