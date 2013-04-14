using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace bynaryXML
{
    public static class NumberHelpers
    {
        public static short Swap(this short value)
        {
            return (short)(ushort)((int)byte.MaxValue & (int)value >> 8 | 65280 & (int)value << 8);
        }

        public static ushort Swap(this ushort value)
        {
            return (ushort)((int)byte.MaxValue & (int)value >> 8 | 65280 & (int)value << 8);
        }

        public static int Swap(this int value)
        {
            uint num = (uint)value;
            return (int)byte.MaxValue & (int)(num >> 24) | 65280 & (int)(num >> 8) | 16711680 & (int)num << 8 | -16777216 & (int)num << 24;
        }

        public static int Swap24(this int value)
        {
            return (int)byte.MaxValue & value >> 16 | 65280 & value | 16711680 & value << 16;
        }

        public static uint Swap(this uint value)
        {
            return (uint)((int)byte.MaxValue & (int)(value >> 24) | 65280 & (int)(value >> 8) | 16711680 & (int)value << 8 | -16777216 & (int)value << 24);
        }

        public static uint Swap24(this uint value)
        {
            return (uint)((int)byte.MaxValue & (int)(value >> 16) | 65280 & (int)value | 16711680 & (int)value << 16);
        }

        public static long Swap(this long value)
        {
            ulong num = (ulong)value;
            return (long)byte.MaxValue & (long)(num >> 56) | 65280L & (long)(num >> 40) | 16711680L & (long)(num >> 24) | 4278190080L & (long)(num >> 8) | 1095216660480L & (long)num << 8 | 280375465082880L & (long)num << 24 | 71776119061217280L & (long)num << 40 | -72057594037927936L & (long)num << 56;
        }

        public static ulong Swap(this ulong value)
        {
            return (ulong)((long)byte.MaxValue & (long)(value >> 56) | 65280L & (long)(value >> 40) | 16711680L & (long)(value >> 24) | 4278190080L & (long)(value >> 8) | 1095216660480L & (long)value << 8 | 280375465082880L & (long)value << 24 | 71776119061217280L & (long)value << 40 | -72057594037927936L & (long)value << 56);
        }
    }
}

