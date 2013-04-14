using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Pak2Zip.IO;
using System.IO;

namespace Pak2Zip
{
    public class PakDir : PakBase
    {
        byte createVersion;
        byte createSystem;
        byte extractVersion;
        byte extractSystem;
        short flagBits;
        short compressType;
        short time;
        short date;
        int crc;
        int compressedSize;
        int uncompressedSize;
        short filenameLength;
        short extraFieldLength;
        short commentLength;
        short diskNumberStart;
        short internalFileAttributes;
        int externalFileAttributes;
        int localHeaderOffset;
        byte[] filename;

        public PakDir(_BinaryReader br)
        {
            createVersion = br.ReadByte();
            createSystem = br.ReadByte();
            extractVersion = br.ReadByte();
            extractSystem = br.ReadByte();
            flagBits = br.ReadH();
            compressType = br.ReadH();
            time = br.ReadH();
            date = br.ReadH();
            crc = br.ReadD();
            compressedSize = br.ReadD();
            uncompressedSize = br.ReadD();
            filenameLength = br.ReadH();
            extraFieldLength = br.ReadH();
            commentLength = br.ReadH();
            diskNumberStart = br.ReadH();
            internalFileAttributes = br.ReadH();
            externalFileAttributes = br.ReadD();
            localHeaderOffset = br.ReadD();
            filename = br.ReadBytes(filenameLength);
        }

        public void writeDir(BinaryWriter bw)
        {
            bw.Write(createVersion);
            bw.Write(createSystem);
            bw.Write(extractVersion);
            bw.Write(extractSystem);
            bw.Write(flagBits);
            bw.Write(compressType);
            bw.Write(time);
            bw.Write(date);
            bw.Write(crc);
            bw.Write(compressedSize);
            bw.Write(uncompressedSize);
            bw.Write(filenameLength);
            bw.Write(extraFieldLength);
            bw.Write(commentLength);
            bw.Write(diskNumberStart);
            bw.Write(internalFileAttributes);
            bw.Write(externalFileAttributes);
            bw.Write(localHeaderOffset);
            bw.Write(filename);
        }

        internal int getOtherDataSize()
        {
            return extraFieldLength + commentLength;
        }
    }
}
