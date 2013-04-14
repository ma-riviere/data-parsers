using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace bynaryXML
{
    class BinaryXmlNode
    {
        public string Name;
        public string Value;
        public Dictionary<string, string> Attributes;
        public List<BinaryXmlNode> Children;

        public void Read(Stream input, BinaryXmlStringTable table)
        {
            this.Name = table[BinaryXmlFileHelpers.ReadPackedS32(input)];
            this.Attributes = new Dictionary<string, string>();
            this.Children = new List<BinaryXmlNode>();
            this.Value = (string)null;
            byte num1 = StreamHelpers.ReadU8(input);
            if (((int)num1 & 1) == 1)
                this.Value = table[BinaryXmlFileHelpers.ReadPackedS32(input)];
            if (((int)num1 & 2) == 2)
            {
                int num2 = BinaryXmlFileHelpers.ReadPackedS32(input);
                for (int index = 0; index < num2; ++index)
                    this.Attributes[table[BinaryXmlFileHelpers.ReadPackedS32(input)]] = table[BinaryXmlFileHelpers.ReadPackedS32(input)];
            }
            if (((int)num1 & 4) != 4)
                return;
            int num3 = BinaryXmlFileHelpers.ReadPackedS32(input);
            for (int index = 0; index < num3; ++index)
            {
                BinaryXmlNode binaryXmlNode = new BinaryXmlNode();
                binaryXmlNode.Read(input, table);
                this.Children.Add(binaryXmlNode);
            }
        }
    }
}
