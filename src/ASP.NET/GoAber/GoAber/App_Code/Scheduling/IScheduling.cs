using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;


namespace GoAber.Scheduling
{
    public interface IScheduling
    {
        void Init(IAppBuilder ao_app);
        void Init(IAppBuilder ao_app, string as_constring);

        void CreateRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, string as_cronexp);
        void CreateRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, Func<string> minutely);
    }
}