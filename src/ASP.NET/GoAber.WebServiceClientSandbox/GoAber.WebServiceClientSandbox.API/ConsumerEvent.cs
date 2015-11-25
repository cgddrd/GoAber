using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.WebServiceClientSandbox.Consumer
{
    public class ConsumerEvent : EventArgs
    {
        public bool result { get; set; }
    }
}
