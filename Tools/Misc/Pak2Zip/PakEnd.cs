using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Pak2Zip.IO;
using System.IO;

namespace Pak2Zip
{
    public class PakEnd : PakBase
    {
        byte[] headerdata;

        public PakEnd(_BinaryReader br)
        {
            headerdata = br.ReadBytes(18);
        }

        public int getOtherDataSize()
        {
            return 0;
        }

        public void writeEnd(BinaryWriter bw)
        {
            bw.Write(headerdata);
        }
    }
}
