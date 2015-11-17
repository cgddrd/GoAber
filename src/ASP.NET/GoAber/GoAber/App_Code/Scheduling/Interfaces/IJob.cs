using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.Scheduling.Interfaces
{
    public interface IJob
    {
        string GetID();
        void Run();
    }
}
