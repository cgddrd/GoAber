using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;


namespace GoAber.Scheduling.Interfaces
{
    public interface IScheduler
    {

        void Init(IAppBuilder ao_app);
        void Init(IAppBuilder ao_app, string as_constring);

        void CreateRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, int ai_minutes);
        string CreateOnceJob(Expression<Action> am_methodcall, int ai_minutes);

        void RemoveRecurringJob(string as_id);

        void RemoveOnceJob(string as_id);

        void EditRecurringJob(string as_id, System.Linq.Expressions.Expression<System.Action> am_methodcall, int ai_minutes);

        void EditOnceJob(string as_id, Expression<Action> am_methodcall, Func<string> ao_cronexp);
    }
}