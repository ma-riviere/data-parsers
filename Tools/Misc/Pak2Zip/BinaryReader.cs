﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Pak2Zip.IO
{
    public class _BinaryReader : BinaryReader
    {
        private bool isLittleEndian;
        private byte[] buffer = new byte[8];

        public _BinaryReader(Stream input, Encoding encoding, bool isLittleEndian)
            : base(input, encoding)
        {
            this.isLittleEndian = isLittleEndian;
        }

        public _BinaryReader(Stream input, bool isLittleEndian)
            : this(input, Encoding.UTF8, isLittleEndian)
        {
        }

        public bool IsLittleEndian
        {
            get { return isLittleEndian; }
            set { isLittleEndian = value; }
        }

        public override double ReadDouble()
        {
            if (isLittleEndian)
                return base.ReadDouble();
            FillMyBuffer(8);
            return BitConverter.ToDouble(buffer.Take(8).Reverse().ToArray(), 0);
        }

        public byte ReadC()
        {
            return base.ReadByte();
        }

        public short ReadH()
        {
            if (isLittleEndian)
                return base.ReadInt16();
            FillMyBuffer(2);
            return BitConverter.ToInt16(buffer.Take(2).Reverse().ToArray(), 0);

        }

        public int ReadD()
        {
            if (isLittleEndian)
                return base.ReadInt32();
            FillMyBuffer(4);
            return BitConverter.ToInt32(buffer.Take(4).Reverse().ToArray(), 0);

        }

        public long ReadQ()
        {
            if (isLittleEndian)
                return base.ReadInt64();
            FillMyBuffer(8);
            return BitConverter.ToInt64(buffer.Take(8).Reverse().ToArray(), 0);

        }

        public float ReadF()
        {
            if (isLittleEndian)
                return base.ReadSingle();
            FillMyBuffer(4);
            return BitConverter.ToSingle(buffer.Take(4).Reverse().ToArray(), 0);
        }

        public override ushort ReadUInt16()
        {
            if (isLittleEndian)
                return base.ReadUInt16();
            FillMyBuffer(2);
            return BitConverter.ToUInt16(buffer.Take(2).Reverse().ToArray(), 0);
        }


        public override uint ReadUInt32()
        {
            if (isLittleEndian)
                return base.ReadUInt32();
            FillMyBuffer(4);
            return BitConverter.ToUInt32(buffer.Take(4).Reverse().ToArray(), 0);
        }

        public override ulong ReadUInt64()
        {
            if (isLittleEndian)
                return base.ReadUInt64();
            FillMyBuffer(8);
            return BitConverter.ToUInt64(buffer.Take(8).Reverse().ToArray(), 0);
        }

        private void FillMyBuffer(int numBytes)
        {
            int offset = 0;
            int num2 = 0;
            if (numBytes == 1)
            {
                num2 = BaseStream.ReadByte();
                if (num2 == -1)
                {
                    throw new EndOfStreamException("Attempted to read past the end of the stream.");
                }
                buffer[0] = (byte)num2;
            }
            else
            {
                do
                {
                    num2 = BaseStream.Read(buffer, offset, numBytes - offset);
                    if (num2 == 0)
                    {
                        throw new EndOfStreamException("Attempted to read past the end of the stream.");
                    }
                    offset += num2;
                }
                while (offset < numBytes);
            }
        }
    }
}
