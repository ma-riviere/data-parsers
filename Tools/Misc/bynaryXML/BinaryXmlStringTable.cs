using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace bynaryXML
{
    class BinaryXmlStringTable
    {
        protected byte[] Data;

        public string this[int index]
        {
            get
            {
                if (index == 0)
                    return "";
                else
                    return ByteHelpers.ReadUTF16Z(this.Data, index * 2);
            }
        }

        public void Read(Stream input)
        {
            int count = BinaryXmlFileHelpers.ReadPackedS32(input);
            this.Data = new byte[count];
            input.Read(this.Data, 0, count);
        }
    }
}
