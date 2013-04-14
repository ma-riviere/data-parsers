using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Pak2Zip.IO;
using System.IO;

namespace Pak2Zip
{
    public class PakFile : PakBase
    {
        byte extractVersion;
        byte extractSystem;
        short generalPurposeFlagBits;
        short compressionMethod;
        short lastModTime;
        short lastModDate;
        int crc;
        int _compressedSize;
        int uncompressedSize;
        short filenameLength;
        short _extraFieldLength;
        byte[] filename;

        public PakFile(_BinaryReader br)
        {
            extractVersion = br.ReadC();
            extractSystem = br.ReadC();
            generalPurposeFlagBits = br.ReadH();
            compressionMethod = br.ReadH();
            lastModTime = br.ReadH();
            lastModDate = br.ReadH();
            crc = br.ReadD();
            _compressedSize = br.ReadD();
            uncompressedSize = br.ReadD();
            filenameLength = br.ReadH();
            _extraFieldLength = br.ReadH();
            filename = br.ReadBytes(filenameLength);

        }

        public short extraFieldLength
        {
            get
            {
                return _extraFieldLength;
            }
        }

        public int compressedSize
        {
            get
            {
                return _compressedSize;
            }
        }

        public long getOtherDataSize()
        {
            return _extraFieldLength + _compressedSize;
        }

        public void writeFile(BinaryWriter bw)
        {
            bw.Write(extractVersion);
            bw.Write(extractSystem);
            bw.Write(generalPurposeFlagBits);
            bw.Write(compressionMethod);
            bw.Write(lastModTime);
            bw.Write(lastModDate);
            bw.Write(crc);
            bw.Write(_compressedSize);
            bw.Write(uncompressedSize);
            bw.Write(filenameLength);
            bw.Write(_extraFieldLength);
            bw.Write(filename);
        }
    }
}
