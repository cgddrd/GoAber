using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.WebServiceClientSandbox.Consumer
{
    public delegate void ConsumerEventHandler(object sender, ConsumerEvent e);
    public class GoAberClient
    {
        public event ConsumerEventHandler ConsumerUpdate;
        public void AddData(string ls_authtoken)
        {
            ConsumerEvent lo_event = new ConsumerEvent();
            try {
                GoAberWS.AuthHeader lo_authentication = new GoAberWS.AuthHeader();
                lo_authentication.authtoken = ls_authtoken;
                GoAberWS.ActivityData[] lo_data = new GoAberWS.ActivityData[1];
                lo_data[0] = new GoAberWS.ActivityData();
                lo_data[0].value = 4;
                lo_data[0].date = DateTime.Now;
                lo_data[0].categoryUnitId = 1;

                GoAberWS.GoAberWSSoapClient lo_service = new GoAberWS.GoAberWSSoapClient();
                bool lb_res = lo_service.AddActivityData(lo_authentication, lo_data);
                lo_event.result = lb_res;
            } catch
            {
                lo_event.result = false;
            }
            ConsumerUpdate(this, lo_event);
        }
    }
}
