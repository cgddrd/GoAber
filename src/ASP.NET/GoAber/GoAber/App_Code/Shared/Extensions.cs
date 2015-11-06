using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Extensions
{
    public static class Extensions
    {
        public static void ConstructionFactory(this device dev, string as_accesstoken, string as_refreshtoken, int ai_devicetid, DateTime? ada_tokexp, int ai_userid)
        {
            dev.accessToken = as_accesstoken;
            dev.refreshToken = as_refreshtoken;
            dev.deviceTypeId = ai_devicetid;
            dev.tokenExpiration = ada_tokexp;
            dev.userId = ai_userid;
        }
    }
}