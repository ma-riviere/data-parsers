using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace bynaryXML
{
    class BinaryXmlFile
    {
        public BinaryXmlNode Root;

        public void Read(Stream input)
        {
            if (StreamHelpers.ReadU8(input) != 128)
                throw new InvalidDataException("not a binary XML file");
            BinaryXmlStringTable table = new BinaryXmlStringTable();
            table.Read(input);
            Root = new BinaryXmlNode();
            Root.Read(input, table);
        }
    }
}
