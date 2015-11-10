using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.Scheduling.Tasks
{
    public interface ITask
    {
        string GetID();
        void Run();
    }
}
