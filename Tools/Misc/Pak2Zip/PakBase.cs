using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Pak2Zip
{
    public class PakBase
    {
        PakDataType _type;

        public void setType(PakDataType type)
        {
            _type = type;
        }

        public PakDataType type
        {
            get
            {
                return _type;
            }
        }
    }
}
