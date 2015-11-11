using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;


namespace GoAber.Scheduling.Interfaces
{
    public interface IScheduler
    {

        void Init(IAppBuilder ao_app);
        void Init(IAppBuilder ao_app, string as_constring);

        void CreateRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, string as_cronexp);
        void CreateRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, Func<string> ao_cronexp);

        void RemoveJob(string as_id);

        void EditRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, string as_cronexp);
        void EditRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, Func<string> ao_cronexp);
    }
}