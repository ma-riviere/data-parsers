using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Xml;

namespace bynaryXML
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Environment.Exit(0);
            }
            else
            {
                var fp = args[0].Split('\\');
                Stream input = File.OpenRead(fp[fp.Length - 1]);
                var outFile = fp[fp.Length - 1].Split('.')[0] + ".unpacked.xml";
                BinaryXmlFile binaryXmlFile = new BinaryXmlFile();
                try
                {
                    binaryXmlFile.Read(input);
                    input.Close();
                    XmlTextWriter xmlTextWriter = new XmlTextWriter(outFile, Encoding.Unicode);
                    xmlTextWriter.Formatting = Formatting.Indented;
                    xmlTextWriter.WriteStartDocument();
                    WriteNode(xmlTextWriter, binaryXmlFile.Root);
                    xmlTextWriter.WriteEndDocument();
                    xmlTextWriter.Flush();
                    xmlTextWriter.Close();
                }
                catch
                {
                    File.Copy(args[0], outFile);
                }
            }
        }

        private static void WriteNode(XmlWriter writer, BinaryXmlNode node)
        {
            writer.WriteStartElement(node.Name);
            foreach (KeyValuePair<string, string> keyValuePair in node.Attributes)
                writer.WriteAttributeString(keyValuePair.Key, keyValuePair.Value);
            foreach (BinaryXmlNode node1 in node.Children)
                Program.WriteNode(writer, node1);
            if (node.Value != null)
                writer.WriteValue(node.Value);
            writer.WriteEndElement();
        }
    }
}
